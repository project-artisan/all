package org.artisan.consumer;

import lombok.RequiredArgsConstructor;
import org.artisan.domain.MemberTokenRepository;
import org.artisan.event.MemberTokenClearEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberTokenConsumer {

    private final MemberTokenRepository memberTokenRepository;

    // TODO 기능 마무리 할 것
    @EventListener
    public void clear(MemberTokenClearEvent event) {
        memberTokenRepository.delete(event.getUser().id());
    }
}
