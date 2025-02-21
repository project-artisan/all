package org.artisan.domain;

import java.util.Optional;
import org.artisan.domain.member.OAuthExternalCredentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {


    boolean existsByCredentials(OAuthExternalCredentials credentials);

    Optional<Member> findByCredentials(OAuthExternalCredentials credentials);

//    boolean existsByProviderId(String providerId);
//
//    boolean existsByEmail(String email);
//
//    Optional<Member> findByEmail(String email);
//
//    Optional<Member> findByProviderId(String providerId);
}
