package org.artisan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 빈 생성 테스트 용도
@SpringBootApplication
public class Application {

    public static void main(String...args) {
        SpringApplication.run(Application.class, args);
    }
}
