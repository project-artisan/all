package org.artisan.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;


@Getter
@Entity
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseEntity {

    @Embedded
    private QuestionMetadata metadata;


    @ManyToOne(fetch = FetchType.LAZY)
    private QuestionSet questionSet;

    /**
     * 콤마로 링크를 구분합니다.
     */
    private String referenceLinks;



}
