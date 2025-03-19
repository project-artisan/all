package org.artisan.aop;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.User;
import org.artisan.exception.CoreApiExceptionCode;
import org.artisan.exception.UnAuthorizedException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class MemberOnlyValidator {

    @Before("@annotation(org.artisan.attributes.MemberOnly)")
    public void check(final JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs())
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(User::isMember)
                .findFirst()
                .orElseThrow(() -> new UnAuthorizedException(CoreApiExceptionCode.UNAUTHORIZED_MEMBER));
    }
}

