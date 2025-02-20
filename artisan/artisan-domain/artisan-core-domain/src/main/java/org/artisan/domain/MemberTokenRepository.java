package org.artisan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTokenRepository extends JpaRepository<MemberToken, Long> {
}
