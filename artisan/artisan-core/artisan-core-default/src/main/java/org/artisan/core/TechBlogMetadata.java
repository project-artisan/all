package org.artisan.core;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechBlogMetadata {
    MARKET_KURLY("https://helloworld.kurly.com/assets/logo2.png", "https://helloworld.kurly.com"),
    NAVER("https://d2.naver.com/static/img/app/d2_logo.gif", "https://d2.naver.com"),
    WOOWAHAN("https://woowahan-cdn.woowahan.com/static/image/share_kor.jpg", "https://techblog.woowahan.com"),
    KAKAO("https://t1.kakaocdn.net/kakao_tech/resources/static/ico_logo.png", ""),
    DEVOCEAN("https://devocean.sk.com/resource/images/external/logo/devocean-og.png", "https://devocean.sk.com"),
    TECHOBLE("https://tecoble.techcourse.co.kr/static/0b18bd94a62a12fdc81ea720c28722f6/af3f1/tecoble.png", "https://tecoble.techcourse.co.kr"),
    NHN_CLOUD("https://static.toastoven.net/toast/resources/img/logo_nhn_cloud_color.svg", "https://meetup.nhncloud.com"),
    LINE("https://engineering.linecorp.com/icons/icon-512x512.png?v=6d6085f233d02c34273fa8a8849b502a", "https://techblog.lycorp.co.jp"),
    DEV_SISTERS("https://www.devsisters.com/devsisters-og-img.jpg", ""),
    BESPIN_GLOBAL("https://bespin-wordpress-bucket.s3.ap-northeast-2.amazonaws.com/wp-content/uploads/2022/12/%ED%85%8C%ED%81%AC%EB%B8%94%EB%A1%9C%EA%B7%B8_%EC%8D%B8%EB%84%A4%EC%9D%BC.jpg", "https://blog.bespinglobal.com"),
    DAANGN("https://cdn-images-1.medium.com/v2/resize:fit:1200/1*Bm8_nGjfNiKV0PASwiPELg.png", ""),
    SARAMIN("https://saramin.github.io/img/avatar-icon.png", ""),
    SQUARE_LAB("https://squarelab.co/image/upload/web/blog.jpg", "https://squarelab.co"),
    DRAMANCOMPANY("https://blog.dramancompany.com/wp-content/uploads/2022/10/Group-3492.png", ""),
    KAKAO_PAY("https://tech.kakaopay.com/_astro/techlog.c831e159_Z12ejLo.png", "https://tech.kakaopay.com");

    private final String logo;
    private final String url;


}
