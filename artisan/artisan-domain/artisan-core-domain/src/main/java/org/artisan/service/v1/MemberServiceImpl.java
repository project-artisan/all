package org.artisan.service.v1;

import lombok.RequiredArgsConstructor;
import org.artisan.domain.Member;
import org.artisan.domain.MemberRepository;
import org.artisan.domain.member.MemberProfile;
import org.artisan.domain.member.OAuthExternalCredentials;
import org.artisan.service.MemberService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;


    public boolean isMember(OAuthExternalCredentials credentials) {
        return memberRepository.existsByCredentials(credentials);
    }

    public Member create(MemberProfile profile, OAuthExternalCredentials credentials) {
        return memberRepository.save(new Member(profile, credentials));
    }

    public Member read(OAuthExternalCredentials credentials) {
        return memberRepository.findByCredentials(credentials)
                .orElseThrow();
    }
}
