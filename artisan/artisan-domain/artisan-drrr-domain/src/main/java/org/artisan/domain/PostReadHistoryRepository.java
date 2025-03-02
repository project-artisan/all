package org.artisan.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostReadHistoryRepository extends JpaRepository<PostReadHistory, Long> {

    Long countByMemberId(Long memberId);

    List<PostReadHistory> findDistinctByMemberIdAndPostIdIn(Long memberId, List<Long> postIds);

}
