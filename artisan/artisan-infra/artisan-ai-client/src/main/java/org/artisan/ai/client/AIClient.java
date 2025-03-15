package org.artisan.ai.client;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.ai.property.PromptProperty;
import org.artisan.exception.OpenAiClientException;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIClient{
    private final OpenAiChatModel openAiChatModel;
    private final PromptProperty promptProperty;

    public FeedbackResponse execute(AiFeedbackRequest request) {
        return subExecute(request);
    }

    private FeedbackResponse subExecute(AiFeedbackRequest request) {
        try {
            var beanOutputConverter = new BeanOutputConverter<>(FeedbackResponse.class);

            var prompt = promptProperty.backendPromptTemplate()
                    .create(Map.of(
                            "format", beanOutputConverter.getFormat(),
                            "question", request.question(),
                            "answer", request.answer()
                    ));

            return beanOutputConverter.convert(
                    openAiChatModel.call(prompt)
                            .getResult()
                            .getOutput()
                            .getText()
            );
        }catch (Exception e) {
            throw new OpenAiClientException();
        }
    }

    public record FeedbackResponse(
            int score,
            String feedback,
            String tailQuestion,
            List<String> referenceLinks
    ) {

    }

}
