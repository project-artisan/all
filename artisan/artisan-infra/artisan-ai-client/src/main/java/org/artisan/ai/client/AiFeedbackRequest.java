package org.artisan.ai.client;

public record AiFeedbackRequest(
        String question,
        String answer
){
}
