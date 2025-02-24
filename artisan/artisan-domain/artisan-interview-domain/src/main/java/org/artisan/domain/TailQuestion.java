package org.artisan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.artisan.core.domain.BaseEntity;

@Entity
@Table(name = "tail_questions")
public class TailQuestion extends BaseEntity {


    @ManyToOne
    private Member member;

}
