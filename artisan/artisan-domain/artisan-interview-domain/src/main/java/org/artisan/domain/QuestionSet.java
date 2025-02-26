package org.artisan.domain;

import static java.util.Comparator.comparing;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Comparator;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.domain.BaseEntity;

@Slf4j
@Getter
@Entity
@Table(name = "question_sets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSet extends BaseEntity {

    @Embedded
    private QuestionSetMetadata metadata;

    @Embedded
    private QuestionSetRule rules;

    @Embedded
    private Questions questions;

    @ManyToOne
    private Category category;

    public List<Question> extractQuestions(int count) {
        // TODO 에러 처리해주기

//        INTERVIEW_CREATE_FAIL.invokeByCondition(count <= 0);
//        INTERVIEW_CREATE_FAIL.invokeByCondition(questions.isEmpty());
//        INTERVIEW_CREATE_FAIL.invokeByCondition(questions.size() < count);

        if (questions.hasSameSize(count)) {
            return questions.getOrderedSequenceValue();
        }

        log.info("{}", questions.getValue());

        return questions.shuffle()
                .subList(0, count)
                .stream()
                .sorted(comparing(question -> question.getMetadata().sequence()))
                .toList();

    }

    public QuestionSet(QuestionSetMetadata metadata, QuestionSetRule rules) {
        this.metadata = metadata;
        this.rules = rules;
    }

    public static QuestionSet of(QuestionSetMetadata metadata, QuestionSetRule rule) {
        return new QuestionSet(metadata, rule);
    }
}
