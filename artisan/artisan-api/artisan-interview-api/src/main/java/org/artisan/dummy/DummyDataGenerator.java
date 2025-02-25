package org.artisan.dummy;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.artisan.domain.Question;
import org.artisan.domain.QuestionMetadata;
import org.artisan.domain.QuestionRepository;
import org.artisan.domain.QuestionSet;
import org.artisan.domain.QuestionSetMetadata;
import org.artisan.domain.QuestionSetRepository;
import org.artisan.domain.QuestionSetRule;
import org.artisan.domain.file.ExternalURL;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile(value = {"local-dev8"})
public class DummyDataGenerator implements ApplicationRunner {
    private final QuestionSetRepository questionSetRepository;
    private final QuestionRepository questionRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        var q = QuestionSet.of(
                new QuestionSetMetadata("자바 기본 문제집", "10문제로 구성", ExternalURL.from(""), 10),
                new QuestionSetRule(3, 10, 10)
        );


        questionSetRepository.save(q);

        AtomicReference<Integer> a = new AtomicReference<>(1);
        questionRepository.saveAll(
                Arrays.stream("""
                                Java의 주요 특징을 설명하세요.
                                Java와 C++의 차이점을 설명하세요.
                                JVM, JRE, JDK의 차이점을 설명하세요.
                                Java에서 final 키워드의 용도는 무엇인가요?
                                Java에서 예외 처리 방법을 설명하세요.
                                Java의 메모리 관리와 Garbage Collection에 대해 설명하세요.
                                스레드와 프로세스의 차이점을 설명하고, Java에서 스레드를 생성하고 관리하는 방법을 설명하세요.
                                Java의 Stream API에 대해 설명하고, 예제를 통해 사용법을 보여주세요.
                                Java의 람다 표현식에 대해 설명하고, 사용 사례를 들어보세요.
                                Java의 디자인 패턴 중 하나를 선택하여 설명하고, 예제를 들어보세요. 
                                """.split("\n"))
                        .map(text -> Question.of(new QuestionMetadata(text, a.getAndSet(a.get() + 1), 10),q))
                        .toList()
        );
    }

}
