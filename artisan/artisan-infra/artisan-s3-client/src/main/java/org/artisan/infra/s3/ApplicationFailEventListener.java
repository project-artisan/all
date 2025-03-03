package org.artisan.infra.s3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationFailEventListener {


    @Bean
    @ConditionalOnMissingBean(ImageUploadFailService.class)
    DefaultAwsImageUploadFailService defaultAwsImageUploadFailService(){
        return new DefaultAwsImageUploadFailService();
    }


    @Slf4j
    static class DefaultAwsImageUploadFailService implements ImageUploadFailService {

        @Override
        public void consume(ImageUploadRequest event) {
            log.error("이미지 업로드 실패 {}", event);
            log.error("이미지 업로드 실패에 대한 복구 처리를 하고 싶은 경우 ImageUploadFailService를 재정의할 것");
        }
    }


}
