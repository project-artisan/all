package org.artisan.domain;

import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Long> {

    List<InterviewQuestion> findByInterviewIdAndMemberId(Long interviewId, Long memberId, Pageable pageable);

    List<InterviewQuestion> findByInterviewIdAndMemberId(Long interviewId, Long memberId);


    @Query("""
                select new org.artisan.domain.InterviewQuestionCount(I.interview.id, count(I.interview.id))
                from InterviewQuestion I
                where I.interview.id in :interviewIds
                group by I.interview.id
            """)
    List<InterviewQuestionCount> countByInterviewIds(@Param("interviewIds") List<Long> interviewIds);

    @Query("""
      select iq from InterviewQuestion iq
        left join fetch iq.tailQuestions.value
      where iq.interview.id = :interviewId
      """)
    List<InterviewQuestion> findAllByAssociate(@Param("interviewId") Long interviewId);

    default InterviewQuestion getById(@NonNull Long interviewQuestionId){
        return findById(interviewQuestionId)
                .orElseThrow();
    }
}
