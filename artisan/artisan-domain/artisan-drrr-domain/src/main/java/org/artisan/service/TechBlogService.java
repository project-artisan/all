package org.artisan.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.artisan.core.TechBlogCode;
import org.artisan.core.User;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.TechBlogRepository;
import org.artisan.exception.DrrrDomainException;
import org.artisan.exception.DrrrDomainExceptionCode;
import org.hibernate.sql.results.DomainResultCreationException;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TechBlogService {

    private final TechBlogRepository techBlogRepository;

    @Transactional(readOnly = true)
    public Slice<TechBlogPost> readAll(List<TechBlogCode> select, String title, Pageable pageable){
        return techBlogRepository.findBy(select, title, pageable);
    }


    @Transactional
    public void updateViewCount(@Nullable User user, Long techBlogId) {
        var blog = techBlogRepository.findByIdWithLock(techBlogId)
                .orElseThrow(() -> new DrrrDomainException(DrrrDomainExceptionCode.NOT_FOUND_TECHBLOG_POST));

        blog.increaseViewCount();
    }

}
