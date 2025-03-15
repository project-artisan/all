package org.artisan.batch;


import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.CrawledTechBlogPostRepository;
import org.artisan.domain.CrawledTechBlogPostRepository.Key;
import org.artisan.domain.ExternalBlogPosts;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.TechBlogRepository;
import org.artisan.domain.TemporalTechBlogPost;
import org.artisan.domain.TemporalTechBlogPostRepository;
import org.artisan.reader.CrawlerItemReaderFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebCrawlerBatchConfiguration {
    public static final String BATCH_NAME = "WebCrawlerBatch";

    public static final String JOB_NAME = BATCH_NAME + "Job";
    public static final String CRAWLING_STEP_NAME = "CrawlingStep";
    public static final String TECH_BLOG_PROCESS_STEP_NAME = "TechBlogProcessStep";
    public static final String TEMP_TECH_BLOG_PROCESS_STEP_NAME = "Temp" + TECH_BLOG_PROCESS_STEP_NAME;
    public static final String CRAWLER_DATA_SAVE_STEP_NAME = "CrawlerDataSaveStep";

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final CrawledTechBlogPostRepository crawledTechBlogPostRepository;
    private final TechBlogRepository techBlogRepository;
    private final TemporalTechBlogPostRepository temporalTechBlogPostRepository;
    private final CrawlerItemReaderFactory crawlerItemReaderFactory;
    private final EntityManagerFactory entityManagerFactory;

    /**
     * <p>
     * 크롤링 배치의 경우 다음 순서에 따라 처리됩니다.
     * <br>- 크롤링
     * <br>- 기술 블로그 검증
     * <br>- 임시 기술 블로그 검증
     * <br>- 저장
     * </P
     */
    @Bean(JOB_NAME)
    public Job webCrawleJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(crawlingStep(null, null))
                .next(techBlogProcessStep(null))
                .next(tempTechBlogProcessStep(null))
                .next(crawlingDataSaveStep())
                .build();
    }

    @Bean(CRAWLING_STEP_NAME)
    @JobScope
    public Step crawlingStep(
            @Value("#{jobParameters[techBlogCode]}") Integer code,
            @Value("#{jobParameters[requestDate]}") String requestDate
    ) {

        return new StepBuilder(CRAWLING_STEP_NAME, jobRepository)
                .<ExternalBlogPosts, ExternalBlogPosts>chunk(10, transactionManager)
                .reader(crawlerItemReaderFactory.createItemReader(TechBlogCode.from(code)))
                .writer(chunk -> chunk.getItems().forEach(crawledTechBlogPostRepository::insertAll))
                .build();
    }

    @JobScope
    @Bean(TECH_BLOG_PROCESS_STEP_NAME)
    public Step techBlogProcessStep(
            @Value("#{jobParameters[techBlogCode]}") Integer code
    ) {
        return new StepBuilder(TECH_BLOG_PROCESS_STEP_NAME, jobRepository)
                .<TechBlogPost, TechBlogPost>chunk(10, transactionManager)
                .reader(techBlogPostItemReader(null))
                .writer(techBlogPostItemWriter())
                .build();
    }

    @Bean(TEMP_TECH_BLOG_PROCESS_STEP_NAME)
    @JobScope
    public Step tempTechBlogProcessStep(
            @Value("#{jobParameters[techBlogCode]}") Integer code
    ) {
        return new StepBuilder(TEMP_TECH_BLOG_PROCESS_STEP_NAME, jobRepository)
                .<TemporalTechBlogPost, TemporalTechBlogPost>chunk(10, transactionManager)
                .reader(temporalTechBlogPostItemReader(null))
                .writer(temporalTechBlogPostItemWriter())
                .build();
    }


    @Bean(CRAWLER_DATA_SAVE_STEP_NAME)
    Step crawlingDataSaveStep() {
        return new StepBuilder(CRAWLER_DATA_SAVE_STEP_NAME, jobRepository)
                .tasklet(crawlerDataSaveTasklet(), transactionManager)
                .build();
    }

    @StepScope
    @Bean("TemporalTechBlogJpaCursorItemReader")
    JpaCursorItemReader<TemporalTechBlogPost> temporalTechBlogPostItemReader(
            @Value("#{jobParameters[techBlogCode]}") Integer code
    ){
        return new JpaCursorItemReaderBuilder<TemporalTechBlogPost>()
                .name("TempTechBlogReader")
                .queryString("SELECT T FROM TemporalTechBlogPost T where T.techBlogCode = :code")
                .parameterValues(Map.of("code", TechBlogCode.from(code)))
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @StepScope
    @Bean("TechBlogJpaCursorItemReader")
    JpaCursorItemReader<TechBlogPost> techBlogPostItemReader(
            @Value("#{jobParameters[techBlogCode]}") Integer code
    ){
        return new JpaCursorItemReaderBuilder<TechBlogPost>()
                .name("TechBlogReader")
                .queryString("SELECT T FROM TechBlogPost T where T.blogMetadata.code = :code")
                .parameterValues(Map.of("code", TechBlogCode.from(code)))
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    @Bean("TechBlogJpaCursorItemWriter")
    ItemWriter<TechBlogPost> techBlogPostItemWriter(){
        return chunk -> {
            chunk.getItems().forEach(techBlogPost -> {
                final Key key = new Key(techBlogPost.getBlogMetadata().urlSuffix(), techBlogPost.getBlogMetadata().code());
                crawledTechBlogPostRepository.ifPresentOrElse(key,
                        () -> crawledTechBlogPostRepository.remove(key),
                        () -> techBlogRepository.delete(techBlogPost));
            });
        };
    }

    @Bean("TemporalTechBlogJpaCursorItemWriter")
    ItemWriter<TemporalTechBlogPost> temporalTechBlogPostItemWriter(){
        return chunk -> {
//            log.info(">>>>>> {}", chunk);
//            chunk.getItems().forEach(temporalTechBlogPost -> {
//                log.info(">>>>>>> {}", temporalTechBlogPost);
//                Key key = new Key(temporalTechBlogPost.getUrlSuffix(), temporalTechBlogPost.getTechBlogCode());
//                crawledTechBlogPostRepository.ifPresentOrElse(key,
//                        () -> crawledTechBlogPostRepository.remove(key),
//                        () -> temporalTechBlogPostRepository.delete(temporalTechBlogPost));
//            });
        };
    }

    @Bean()
    Tasklet crawlerDataSaveTasklet() {
        return (_, _) -> {
            temporalTechBlogPostRepository.saveAll(crawledTechBlogPostRepository.toTemporalTechBlogPosts());
            return RepeatStatus.FINISHED;
        };
    }
}

