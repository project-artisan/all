package org.artisan.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("""
                select new org.artisan.domain.QuestionSetProblemCount(Q.questionSet.id, count(Q.questionSet.id)) from Question Q
                where Q.questionSet.id in :questionSetIds
                group by Q.questionSet.id
            """)
    List<QuestionSetProblemCount> countByQuestionIds(@Param("questionSetIds") List<Long> questionSetIds);


    @Query("select Q from Question Q where Q.id = :questionSetId")
    List<Question> findByQuestionSetId(@Param("questionSetId") Long questionSetId);
}
