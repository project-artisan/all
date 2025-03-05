package org.artisan.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.TechBlogCode;
import org.artisan.core.User;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.TechBlogRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TechBlogService {

    private final TechBlogRepository techBlogRepository;

    @Transactional(readOnly = true)
    public Slice<TechBlogPost> readAll(List<TechBlogCode> select, String title, Pageable pageable){
        return techBlogRepository.findBy(select, title, pageable);
    }


    @Transactional
    public TechBlogPost read(Long techBlogId){
        // TODO null 처리 할 것

        var blog = techBlogRepository.findByIdWithLock(techBlogId)
                .orElseThrow();

        blog.increaseViewCount();

        return blog;
    }

}
