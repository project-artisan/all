package org.artisan.domain;

import java.util.Optional;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewRepository extends JpaRepository<Interview, Long> {

    Optional<Interview> findByIdAndMemberId(Long interviewId, Long memberId);

    Page<Interview> findByMemberId(Long memberId, Pageable pageable);

    @Query("""
        select i from Interview i
            join fetch i.interviewQuestions.value iq
            join fetch iq.question q
            
            where i.id = :interviewId and i.member.id = :memberId 
    """)
    Optional<Interview> findAllWithQuestion(
            @Param("memberId") Long memberId,
            @Param("interviewId") Long interviewId
    );

    default Interview getById(Long interviewId){
        return findById(interviewId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_INTERVIEW));
    }

    default Interview getByIdAndMemberId(Long interviewId, Long memberId){
        return findByIdAndMemberId(interviewId, memberId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_INTERVIEW));
    }

    @Query("""
        select i from Interview i
            join fetch i.interviewQuestions.value iq
            join fetch iq.question q
            join fetch iq.tailQuestions.value itq
    """)
    Optional<Interview> findAllWithAssociations(Long memberId, Long interviewId);
}
