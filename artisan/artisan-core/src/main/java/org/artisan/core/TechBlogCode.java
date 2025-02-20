package org.artisan.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


// TODO 블로그, url, 및 상태 분리하기
@Getter
@RequiredArgsConstructor
public enum TechBlogCode {
    BASE(100L, ""), // 기본 상태
    MARKET_KURLY(BASE.id + 1, "마켓 컬리", "https://helloworld.kurly.com/assets/logo2.png"),
    NAVER(BASE.id + 2, "네이버", "https://d2.naver.com/static/img/app/d2_logo.gif"),
    WOOWAHAN(BASE.id + 3, "우아한 형제들", "https://woowahan-cdn.woowahan.com/static/image/share_kor.jpg"),
    KAKAO(BASE.id + 4, "카카오", "https://t1.kakaocdn.net/kakao_tech/resources/static/ico_logo.png"),
    DEVOCEAN(BASE.id + 5, "데보션", "https://devocean.sk.com/resource/images/external/logo/devocean-og.png"),
    TECHOBLE(BASE.id + 6, "우테코 기술블로그",
            "https://tecoble.techcourse.co.kr/static/0b18bd94a62a12fdc81ea720c28722f6/af3f1/tecoble.png"),

    NHN_CLOUD(BASE.id + 7, "NHN 클라우드", "https://static.toastoven.net/toast/resources/img/logo_nhn_cloud_color.svg"),
    LINE(BASE.id + 8, "네이버 라인",
            "https://engineering.linecorp.com/icons/icon-512x512.png?v=6d6085f233d02c34273fa8a8849b502a"),
    DEV_SISTERS(BASE.id + 9, "데브시스터즈", "https://www.devsisters.com/devsisters-og-img.jpg"),
    BESPIN_GLOBAL(BASE.id + 10, "베스핀글로벌",
            "https://bespin-wordpress-bucket.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2022/12/%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8_%EC%8D%B8%EB%84%A4%EC%9D%BC.jpg"),
    DAANGN(BASE.id + 11, "당근마켓", "https://cdn-images-1.medium.com/v2/resize:fit:1200/1*Bm8_nGjfNiKV0PASwiPELg.png"),
    SARAMIN(BASE.id + 12, "사람인", "https://saramin.github.io/img/avatar-icon.png"),
    SQUARE_LAB(BASE.id + 13, "스퀘어 랩", "https://squarelab.co/image/upload/web/blog.jpg"),
    DRAMANCOMPANY(BASE.id + 14, "드라마 앤 컴퍼니(리멤버)",
            "https://blog.dramancompany.com/wp-content/uploads/2022/10/Group-3492.png"),
    KAKAO_PAY(BASE.id + 15, "카카오 페이", "https://tech.kakaopay.com/_astro/techlog.c831e159_Z12ejLo.png"),
    SMAIL_GATE_AI(BASE.id + 16, "스마일 게이트 ai");

    private static final Map<Long, TechBlogCode> cache = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(TechBlogCode::getId, Function.identity()));

    private final Long id;
    private final String name;
    private String blogLogo;

    TechBlogCode(Long id, String name, String blogLogo) {
        this.id = id;
        this.name = name;
        this.blogLogo = blogLogo;
    }

    public static TechBlogCode from(Long id) {
        return cache.get(id);
    }

    public static Collection<TechBlogCode> getAll() {
        return cache.values();
    }
}
