package org.artisan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {


    default MemberToken getById(Long id){
        return findById(id)
                .orElseThrow();
    }}
