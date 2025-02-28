package org.artisan.batch;


import com.google.common.collect.Maps;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.TechBlogRepository;
import org.artisan.domain.TemporalTechBlogPost;
import org.artisan.domain.TemporalTechBlogPostRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class MigrationBatchConfiguration {
    public static final String BATCH_NAME = "Migration";
    public static final String JOB_NAME = BATCH_NAME + "Job";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;
    private final TechBlogRepository techBlogPostRepository;
    private final TemporalTechBlogPostRepository temporalTechBlogPostRepository;

    @Bean(name = JOB_NAME)
    public Job migrationJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(migrationStep(null, null))
                .build();
    }

    @JobScope
    @Bean(name = BATCH_NAME + "Step")
    public Step migrationStep(
            @Value("#{jobParameters[techBlogCode]}") Integer code,
            @Value("#{jobParameters[requestDate]}") String requestDate
    ) {
        return new StepBuilder(BATCH_NAME + "Step", jobRepository)
                .<TemporalTechBlogPost, TemporalTechBlogPost>chunk(100, transactionManager)
                .reader(temporalTechBlogPostJpaCursorItemReader(null))
                .processor(temporalTechBlogPostItemProcessor())
                .writer(temporalTechBlogPostItemWriter())
                .build();
    }

    @Bean
    @StepScope
    JpaCursorItemReader<TemporalTechBlogPost> temporalTechBlogPostJpaCursorItemReader(
            @Value("#{jobParameters[techBlogCode]}") Integer code
    ){
        return new JpaCursorItemReaderBuilder<TemporalTechBlogPost>()
                .name("migrationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select T from TemporalTechBlogPost T where T.techBlogCode = :techBlogCode ")
                .parameterValues(Map.of("techBlogCode", TechBlogCode.from(code)))
                .build();
    }

    @Bean
    ItemProcessor<TemporalTechBlogPost, TemporalTechBlogPost> temporalTechBlogPostItemProcessor(){
        return temporalTechBlogPost -> {
            if (techBlogPostRepository.existsByBlogMetadataCodeAndBlogMetadataUrlSuffix(
                    temporalTechBlogPost.getTechBlogCode(),
                    temporalTechBlogPost.getUrlSuffix()
            )){
                return null;
            }

            return temporalTechBlogPost.isRegistrationCompleted() ? temporalTechBlogPost : null;
        };
    }

    @Bean
    ItemWriter<TemporalTechBlogPost> temporalTechBlogPostItemWriter() {
        return chunk -> {
            final var deleteList = new ArrayList<TemporalTechBlogPost>() ;
            for (var temporalTechBlogPost : chunk.getItems()) {
                techBlogPostRepository.save(temporalTechBlogPost.toTechBlogPost());
//                deleteList.add(temporalTechBlogPost);
            }

//            temporalTechBlogPostRepository.deleteAll(deleteList);
        };
    }
}