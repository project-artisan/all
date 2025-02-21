package org.artisan.domain;

import org.artisan.core.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {


    default MemberToken getById(Long id){
        return findById(id)
                .orElseThrow();
    }

    @Modifying
    @Query("delete MemberToken mt where mt.member.id =: memberId")
    void delete(Long memberId);
}
