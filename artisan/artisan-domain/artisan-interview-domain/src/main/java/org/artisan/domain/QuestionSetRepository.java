package org.artisan.domain;

import java.util.Optional;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long> {

    default QuestionSet getById(Long questionSetId){
        return findById(questionSetId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_QUESTION_SET));
    }

    @Query("select qs from QuestionSet qs order by qs.createdAt desc")
    Page<QuestionSet> findAllOrderBy(Pageable pageable);


}
