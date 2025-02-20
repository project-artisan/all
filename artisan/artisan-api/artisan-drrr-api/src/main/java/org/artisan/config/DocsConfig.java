package org.artisan.config;

import org.artisan.docs.DocumentSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocsConfig {

    @Bean
    public DocumentSpec documentSpec(){
        return DocumentSpec.builder()
                .title("artisan")
                .description("Artisan Swagger UI")
                .version("0.0.1")
                .build();
    }
}
