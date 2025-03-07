package org.artisan.service;


import java.time.LocalDate;
import org.artisan.DrrrDomainTestContext;
import org.artisan.core.TechBlogCode;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.TechBlogRepository;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.post.BlogMetadata;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = "spring.jpa.show-sql=true")
class TechBlogServiceTest extends DrrrDomainTestContext {

    @Autowired
    private TechBlogRepository techBlogRepository;

    @Test
    @Transactional
    void 동시증가쿼리_테스트(){

        techBlogRepository.save(TechBlogPost.from(
                new BlogMetadata("", "", LocalDate.now(), "", ExternalURL.from(""), ExternalURL.from(""), TechBlogCode.BASE)
        ));

        techBlogRepository.findByIdWithLock(1L);
    }


}