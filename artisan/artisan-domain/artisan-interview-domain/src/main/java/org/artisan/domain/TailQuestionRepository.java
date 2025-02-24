package org.artisan.domain;


import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TailQuestionRepository extends JpaRepository<TailQuestion, Long> {

    Optional<TailQuestion> findByIdAndMemberId(Long tailQuestionId, Long memberId);

    List<TailQuestion> findByInterviewId(Long interviewId);
}
