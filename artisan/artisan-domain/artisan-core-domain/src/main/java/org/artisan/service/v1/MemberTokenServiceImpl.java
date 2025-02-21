package org.artisan.service.v1;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.artisan.domain.Member;
import org.artisan.domain.MemberToken;
import org.artisan.domain.MemberTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberTokenServiceImpl {

    private final MemberTokenRepository memberTokenRepository;

    public MemberToken write(Member member, LocalDateTime expiredAt) {
        return memberTokenRepository.save(MemberToken.from(member, expiredAt));
    }

    public MemberToken write(Long memberId) {
        return null;
    }

    public void updateExpire(Long tokenId, LocalDateTime expiredAt) {
        memberTokenRepository.getById(tokenId)
                .updateExpiredAt(expiredAt);
    }


    public MemberToken read(Long tokenId) {
        return memberTokenRepository.getById(tokenId);
    }

    public void delete(MemberToken memberToken) {

    }

    public void delete(Long tokenId) {
        memberTokenRepository.getById(tokenId)
                        .expire();
    }
}
