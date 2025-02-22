package org.artisan.service;

import lombok.RequiredArgsConstructor;
import org.artisan.domain.TechBlogPost;
import org.artisan.domain.TechBlogRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TechBlogService {

    private final TechBlogRepository techBlogRepository;

    @Transactional(readOnly = true)
    public Slice<TechBlogPost> readAll(String title, Pageable pageable){
        return techBlogRepository.findAllByOrderByBlogMetadataWrittenAtDesc(pageable);
    }
}
