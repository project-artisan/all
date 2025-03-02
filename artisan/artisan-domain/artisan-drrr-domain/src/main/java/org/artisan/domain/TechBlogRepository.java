package org.artisan.domain;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.artisan.core.TechBlogCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TechBlogRepository extends JpaRepository<TechBlogPost, Long>, TechBlogReadRepository {

    Slice<TechBlogPost> findAllByOrderByBlogMetadataWrittenAtDesc(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from TechBlogPost p where p.id = :id")
    Optional<TechBlogPost> findByIdWithLock(@Param("id") Long id);


    boolean existsByBlogMetadataCodeAndBlogMetadataUrlSuffix(TechBlogCode techBlogCode, String urlSuffix);

    @Query("""
            select p from TechBlogPost p where
            p.blogMetadata.code in :codes and p.blogMetadata.title like CONCAT('%', :title, '%')
                order by p.blogMetadata.writtenAt desc
     """)
    Slice<TechBlogPost> searchBy(
            @Param("codes") List<TechBlogCode> select,
            @Param("title") String title,
            Pageable pageable
    );

    @Query("""
          select p from TechBlogPost p where
            p.blogMetadata.code in :codes
            order by p.blogMetadata.writtenAt desc
    """)
    Slice<TechBlogPost> searchBy(
            @Param("codes") List<TechBlogCode> select,
            Pageable pageable
    );

    Slice<TechBlogPost> searchByBlogMetadataTitleContaining(String title, Pageable pageable);


    default Slice<TechBlogPost> findBy(List<TechBlogCode> select, String title, Pageable pageable) {

        if(select == null || select.isEmpty()) {
            if(Strings.isBlank(title)) {
                return findAllByOrderByBlogMetadataWrittenAtDesc(pageable);
            }
            return searchByBlogMetadataTitleContaining(title, pageable);
        }
        if(Strings.isBlank(title)) {
            return searchBy(select, pageable);
        }
        return searchBy(select, title, pageable);
    }
}
