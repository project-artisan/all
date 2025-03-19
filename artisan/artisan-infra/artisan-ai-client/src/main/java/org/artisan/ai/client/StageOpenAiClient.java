package org.artisan.ai.client;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.ai.client.OpenAiClient.FeedbackResponse;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

// 성능 테스트 용도의 client
@Slf4j
@Service
@Profile("stage")
@RequiredArgsConstructor
public class StageOpenAiClient implements AIClient {

    @Override
    public FeedbackResponse execute(AiFeedbackRequest request) {

        try {
            // 1~10초 사이로 대기
            Thread.sleep(new Random().nextInt(9000) + 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new FeedbackResponse(10, "", "", List.of());
    }
}
