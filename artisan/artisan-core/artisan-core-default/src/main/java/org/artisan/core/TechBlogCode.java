package org.artisan.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.artisan.core.exception.CoreException;


// TODO 블로그, url, 및 상태 분리하기
@Getter
@RequiredArgsConstructor
public enum TechBlogCode {
    BASE(100, ""), // 기본 상태
    MARKET_KURLY(BASE.id + 1, "마켓 컬리", TechBlogMetadata.MARKET_KURLY),
    NAVER(BASE.id + 2, "네이버", TechBlogMetadata.NAVER),
    WOOWAHAN(BASE.id + 3, "우아한 형제들", TechBlogMetadata.WOOWAHAN),
    KAKAO(BASE.id + 4, "카카오", TechBlogMetadata.KAKAO),
    DEVOCEAN(BASE.id + 5, "데보션", TechBlogMetadata.DEVOCEAN),
    TECHOBLE(BASE.id + 6, "우테코 기술블로그", TechBlogMetadata.TECHOBLE),
    NHN_CLOUD(BASE.id + 7, "NHN 클라우드", TechBlogMetadata.NHN_CLOUD),
    LINE(BASE.id + 8, "네이버 라인", TechBlogMetadata.LINE),
    DEV_SISTERS(BASE.id + 9, "데브시스터즈", TechBlogMetadata.DEV_SISTERS),
    BESPIN_GLOBAL(BASE.id + 10, "베스핀글로벌", TechBlogMetadata.BESPIN_GLOBAL),
    DAANGN(BASE.id + 11, "당근마켓", TechBlogMetadata.DAANGN),
    SARAMIN(BASE.id + 12, "사람인", TechBlogMetadata.SARAMIN),
    SQUARE_LAB(BASE.id + 13, "스퀘어 랩", TechBlogMetadata.SQUARE_LAB),
    DRAMANCOMPANY(BASE.id + 14, "드라마 앤 컴퍼니(리멤버)", TechBlogMetadata.DRAMANCOMPANY),
    KAKAO_PAY(BASE.id + 15, "카카오 페이", TechBlogMetadata.KAKAO_PAY);
//    SMAIL_GATE_AI(BASE.id + 16, "스마일 게이트 ai", null);

    private static final Map<Integer, TechBlogCode> cache = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(TechBlogCode::getId, Function.identity()));

    private final Integer id;
    private final String title;
    private final TechBlogMetadata metadata;

    TechBlogCode(Integer id, String title) {
        this.id = id;
        this.title = title;
        this.metadata = null;
    }

    public static TechBlogCode from(Integer id) {
        var blog = cache.get(id);
        if(blog == null) {
            throw new CoreException("존재하지 않는 기술블로그입니다.");
        }
        return blog;
    }

    public static Collection<TechBlogCode> getAll() {
        return cache.values();
    }
}
