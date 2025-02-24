package org.artisan.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findByIdAndMemberId(Long interviewId, Long memberId);

    Page<Interview> findByMemberId(Long memberId, Pageable pageable);

}
