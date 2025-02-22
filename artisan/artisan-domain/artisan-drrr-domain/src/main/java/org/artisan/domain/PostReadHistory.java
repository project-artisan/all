package org.artisan.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.artisan.core.domain.BaseEntity;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "posts_history")
public class PostReadHistory extends BaseEntity {

    private Long memberId;

    private Long postId;

    @Builder
    public PostReadHistory(Long memberId, Long postId) {
        this.memberId = memberId;
        this.postId = postId;
    }
}
