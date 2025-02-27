package org.artisan.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TemporalTechBlogPostRepository extends JpaRepository<TemporalTechBlogPost, Long> {

    @Query("SELECT T FROM TemporalTechBlogPost T where T.techBlogCode = :code")
    void t();
}
