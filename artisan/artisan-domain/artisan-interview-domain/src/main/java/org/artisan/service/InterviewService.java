package org.artisan.service;


import lombok.RequiredArgsConstructor;
import org.artisan.core.User;
import org.artisan.domain.AIFeedback;
import org.artisan.domain.Answer;
import org.artisan.domain.CurrentInterview;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewMetadata;
import org.artisan.domain.InterviewProgress;
import org.artisan.domain.InterviewQuestion;
import org.artisan.domain.InterviewQuestionRepository;
import org.artisan.domain.InterviewRepository;
import org.artisan.domain.InterviewSetting;
import org.artisan.domain.MemberRepository;
import org.artisan.domain.QuestionSetRepository;
import org.artisan.domain.TailQuestion;
import org.artisan.domain.TailQuestionRepository;
import org.artisan.exception.InterviewDomainException;
import org.artisan.exception.InterviewDomainExceptionCode;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository interviewRepository;
    private final InterviewQuestionRepository interviewQuestionRepository;
    private final QuestionSetRepository questionSetRepository;
    private final MemberRepository memberRepository;
    private final TailQuestionRepository tailQuestionRepository;

    public Interview createNewInterview(
            User user,
            Long questionSetId,
            InterviewProgress interviewProgress,
            InterviewSetting setting
    ){
        var member = memberRepository.getById(user.id());
        var questionSet = questionSetRepository.getById(questionSetId);
        var questions = questionSet.extractQuestions(interviewProgress.getSize());

        var interview = new Interview(
                member,
                InterviewMetadata.from(questionSet),
                interviewProgress,
                setting
        );

        interviewRepository.save(interview);
        interviewQuestionRepository.saveAll(questions.stream()
                .map(question -> InterviewQuestion.of(interview, member, question, setting.tailQuestionDepth()))
                .toList()
        );

        return interview;
    }

    @Nullable
    public TailQuestion submit(
            User user,
            Long interviewId,
            Answer answer,
            AIFeedback aiFeedback
    ) {
        var interview = interviewRepository.getByIdAndMemberId(interviewId, user.id());

        var tailQuestion = interview.submit(answer, aiFeedback);

        if(tailQuestion == null) {
            return null;
        }

        return tailQuestionRepository.save(tailQuestion);
    }


    @Transactional(readOnly = true)
    public CurrentInterview loadByCurrentProblem(User user, Long interviewId) {
        var interview = interviewRepository.getByIdAndMemberId(interviewId, user.id());
        var interviewQuestion = interview.getCurrentProblem();

        if(interviewQuestion == null){
            return new CurrentInterview(interview, null);
        }

        return new CurrentInterview(
                interview,
                interview.getCurrentProblem()
        );
    }

    @Transactional(readOnly = true)
    public Page<Interview> search(User user, Pageable pageable) {
        return interviewRepository.findByMemberId(user.id(), pageable);
    }


    @Transactional(readOnly = true)
    public Interview getDetail(User user, Long interviewId){
        // TODO validation
        return interviewRepository.findAllWithQuestion(user.id(), interviewId)
                .orElseThrow(() -> new InterviewDomainException(InterviewDomainExceptionCode.NOT_FOUND_INTERVIEW));
    }

    @Transactional(readOnly = true)
    public Interview getInterviewResult(User user, Long interviewId) {
        var interview = interviewRepository.getByIdAndMemberId(interviewId, user.id());

        interview.setInterviewQuestions(interviewQuestionRepository.findAllByAssociate(interviewId));

        return interview;
    }
}
