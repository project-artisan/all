package org.artisan.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;

@Entity
@Table(name = "question_sets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionSet extends BaseEntity {

    @Embedded
    private QuestionMetadata metadata;

    @Embedded
    private QuestionSetRule rules;

    @Embedded
    private Questions questions;

    @ManyToOne
    private Category category;

}
