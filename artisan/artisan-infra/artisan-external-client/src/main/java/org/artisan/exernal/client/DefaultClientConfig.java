package org.artisan.exernal.client;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class DefaultClientConfig {

    @Bean("githubClient")
    @ConditionalOnMissingBean
    RestClient githubClient(){
        return RestClient.builder()
                .build();
    }
}
