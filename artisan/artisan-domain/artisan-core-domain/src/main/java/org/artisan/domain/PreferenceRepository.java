package org.artisan.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    @Query("select p from Preference p left join fetch p.category where p.memberId = :memberId")
    List<Preference> findByMemberId(@Param("memberId") Long memberId);


    @Modifying
    @Query("delete Preference p where p.memberId = :memberId and p.category.id in (:categoryIds)")
    void deleteByMemberIdAndCategoryIds(
            @Param("memberId") Long memberId,
            @Param("categoryIds") List<Long> categoryIds
    );


}
