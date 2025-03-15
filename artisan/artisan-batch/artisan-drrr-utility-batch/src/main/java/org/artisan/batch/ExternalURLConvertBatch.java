package org.artisan.batch;


import jakarta.persistence.EntityManagerFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.artisan.domain.BatchTechBlogPost;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.file.FileType;
import org.artisan.infra.s3.ImageUploadRequest;
import org.artisan.infra.s3.ImageUploadRequest.ImageIdentifier;
import org.artisan.infra.s3.ImageUploadService;
import org.artisan.infra.s3.ImageUploadService.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ExternalURLConvertBatch {

    public static final String BATCH_NAME = "ExternalURLConvert";
    public static final String JOB_NAME = BATCH_NAME + "Job";
    public static final String STEP_NAME = BATCH_NAME + "Step";
    private static final Logger log = LoggerFactory.getLogger(ExternalURLConvertBatch.class);

    private final JobRepository jobRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final PlatformTransactionManager transactionManager;
    private final ImageUploadService imageUploadService;

    @Value("${storage.thumbnail}")
    private String prefix;

    @Bean(JOB_NAME)
    public Job convertJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(convertStep(null))
                .build();
    }

    @Bean(STEP_NAME)
    @JobScope
    public Step convertStep(
            @Value("#{jobParameters[requestDate]}") String requestDate
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
                .<BatchTechBlogPost, BatchTechBlogPost>chunk(100, transactionManager)
                .reader(techBlogItemReader())
                .processor(batchTechBlogPostBatchTechBlogPostItemProcessor())
                .writer(batchTechBlogPostItemWriter())
                .build();
    }

    @StepScope
    @Bean("BatchTechBlogPostPagingReader")
    JpaPagingItemReader<BatchTechBlogPost> techBlogItemReader(){
        return new JpaPagingItemReaderBuilder<BatchTechBlogPost>()
                .name("TechBlogPaginationReader")
                .queryString("SELECT T FROM BatchTechBlogPost T")
                .pageSize(100)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @StepScope
    @Bean("BatchTechBlogPostProcessor")
    ItemProcessor<BatchTechBlogPost, BatchTechBlogPost> batchTechBlogPostBatchTechBlogPostItemProcessor(){
        return techBlogPost -> {

            if(techBlogPost.getThumbnail().getId() == null
                    || techBlogPost.getThumbnail().getType() != FileType.URL
            ) {
                return techBlogPost;
            }


            var fileIdentifier = new ImageIdentifier(prefix, UUID.randomUUID().toString());
            var imageUploadRequest = new ImageUploadRequest(
                    techBlogPost.getThumbnail().toUrl(),
                    fileIdentifier,
                    techBlogPost
            );

            var result = imageUploadService.uploadByUrl(imageUploadRequest);
            if(result == Result.OK) {
                techBlogPost.changeThumbnail(new ExternalURL(FileType.AWS_STORAGE_URL, fileIdentifier.id()));
            }
            return techBlogPost;
        };
    }

    @Bean("BatchTechBlogPostItemWriter")
    JpaItemWriter<BatchTechBlogPost> batchTechBlogPostItemWriter(){
        return new JpaItemWriterBuilder<BatchTechBlogPost>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


}
