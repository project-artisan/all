package org.artisan.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.artisan.core.User;
import org.artisan.domain.AIFeedback;
import org.artisan.domain.Answer;
import org.artisan.domain.TailQuestion;
import org.artisan.domain.TailQuestionRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TailQuestionService {

    private final TailQuestionRepository tailQuestionRepository;

    @Nullable
    public TailQuestion submit(User user, Long tailQuestionId, Answer answer, AIFeedback aiFeedback){

        var tailQuestion = tailQuestionRepository.getByIdAndMemberId(tailQuestionId, user.id());

        var newTailQuestion = tailQuestion.submit(answer, aiFeedback);

        if(newTailQuestion == null) {
            return null;
        }

        return tailQuestionRepository.save(newTailQuestion);
    }


}
