package org.artisan.event;

import lombok.Getter;
import org.artisan.core.User;
import org.springframework.context.ApplicationEvent;


@Getter
public class MemberTokenClearEvent extends ApplicationEvent {

    private final User user;

    public MemberTokenClearEvent(Object source, User user) {
        super(source);
        this.user = user;
    }
}


