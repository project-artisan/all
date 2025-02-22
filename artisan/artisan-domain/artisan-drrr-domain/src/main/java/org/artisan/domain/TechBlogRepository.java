package org.artisan.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.artisan.core.TechBlogCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TechBlogRepository extends JpaRepository<TechBlogPost, Long> {

    @Query("""
                 select P from TechBlogPost P
                       left join P.tags tag
                       left join tag.category category
                 where category.name like :name%
                 order by P.blogMetadata.writtenAt desc
            """)
    Slice<TechBlogPost> findByCategoryName(@Param("name") String name, Pageable pageable);

    Slice<TechBlogPost> findByBlogMetadataContainingOrderByBlogMetadataWrittenAtDesc(String title, Pageable pageable);

    Slice<TechBlogPost> findAllByOrderByBlogMetadataWrittenAtDesc(Pageable pageable);


    Slice<TechBlogPost> findByBlogMetadataCodeOrderByBlogMetadataWrittenAt(TechBlogCode code);

    @Query("""
                select new org.artisan.domain.CategoryCount(category.id, category.name, count(category.id) )
                from TechBlogPost p
                   left join p.tags tag
                   left join tag.category category
                where p.blogMetadata.code = :code
                group by category.id, category.name
                order by count(category.id) desc
            """)
    List<CategoryCount> groupingByCode(@Param("code") TechBlogCode code);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from TechBlogPost p where p.id = :id")
    Optional<TechBlogPost> findByIdWithLock(@Param("id") Long id);

}
