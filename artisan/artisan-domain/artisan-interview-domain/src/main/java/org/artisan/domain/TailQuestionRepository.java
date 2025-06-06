package org.artisan.domain;


import java.util.List;
import java.util.Optional;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TailQuestionRepository extends JpaRepository<TailQuestion, Long> {

    Optional<TailQuestion> findByIdAndMemberId(Long tailQuestionId, Long memberId);

    List<TailQuestion> findByInterviewId(Long interviewId);

    default TailQuestion getByIdAndMemberId(Long tailQuestionId, Long memberId){
        // TODO 에러 처리 할 것
        return findByIdAndMemberId(tailQuestionId, memberId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_TAIL_QUESTION));
    }


    @NonNull
    default TailQuestion getById(@NonNull Long tailQuestionId){
        return findById(tailQuestionId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_TAIL_QUESTION));
    }



}
