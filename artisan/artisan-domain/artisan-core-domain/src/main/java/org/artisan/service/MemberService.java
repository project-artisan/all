package org.artisan.service;

import org.artisan.core.User;
import org.artisan.domain.Member;

public interface MemberService {

    Member read(User user);
}
