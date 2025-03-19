package org.artisan.config;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class InterviewAsyncConfig {

//    @Bean(name = "asyncInterviewExecutor")
    @Bean(name = "virtualAsyncInterviewExecutor")
    public Executor executor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(Integer.MAX_VALUE);
        executor.setThreadNamePrefix("asyncInterview-");
        executor.initialize();
        return executor;
    }

    @Bean(name = "asyncInterviewExecutor")
//    @Bean(name = "virtualAsyncInterviewExecutor")
    public Executor virtualTaskExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
