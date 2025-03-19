package org.artisan.service;

import lombok.RequiredArgsConstructor;
import org.artisan.core.User;
import org.artisan.domain.AIFeedback;
import org.artisan.domain.Answer;
import org.artisan.domain.InterviewQuestion;
import org.artisan.domain.InterviewQuestionRepository;
import org.artisan.domain.InterviewRepository;
import org.artisan.domain.TailQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewQuestionService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final TailQuestionRepository tailQuestionRepository;

    public InterviewQuestion submit(
            User user,
            Long interviewId,
            Answer answer
    ) {
        var interview = interviewRepository.getByIdAndMemberId(interviewId, user.id());
        var interviewQuestionId = interview.submit(answer);

        return interviewQuestionRepository.getById(interviewQuestionId);
    }

    public void mark(User user, Long interviewId, AIFeedback aiFeedback){
        var interview = interviewRepository.getByIdAndMemberId(interviewId, user.id());

        var tailQuestion = interview.mark(aiFeedback);

        if(tailQuestion != null) {
            tailQuestionRepository.save(tailQuestion);
        }
    }

    public InterviewQuestion checkState(User user, Long interviewQuestionId) {
        return interviewQuestionRepository.getById(interviewQuestionId, user.id());
    }

}
