package org.artisan.test.fixtures;

import static org.artisan.core.OAuth2State.*;

import org.artisan.domain.Member;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.file.FileType;
import org.artisan.domain.member.MemberProfile;
import org.artisan.domain.member.OAuthExternalCredentials;

public class MemberFixture {

    public static Member create(){
        return new Member(
                new MemberProfile("sample nickname", new ExternalURL(FileType.AWS_STORAGE_URL, "")),
                new OAuthExternalCredentials(GITHUB, "provider id")
        );
    }
}
