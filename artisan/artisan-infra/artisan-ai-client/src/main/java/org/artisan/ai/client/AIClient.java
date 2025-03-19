package org.artisan.ai.client;

import org.artisan.ai.client.OpenAiClient.FeedbackResponse;

public interface AIClient{

    FeedbackResponse execute(AiFeedbackRequest request);

}
