package org.artisan.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionSetRepository extends JpaRepository<QuestionSet, Long> {

//    @Query("""
//                select q from Question q
//                    join Q.questionSet.value questions
//                    where Q.id = :id
//                    order by questions.sequence asc
//            """)
//    Optional<QuestionSet> findByIdContainsQuestions(@Param("id") Long questionSetId);

    default QuestionSet getById(Long questionSetId){
        return findById(questionSetId)
                .orElseThrow();
    }


}
