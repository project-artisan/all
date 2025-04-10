package org.artisan.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.artisan.test.testfixtures.InterviewFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InterviewTest {


    @Test
    void 면접을_생성하면_점수가_0점입니다(){

        var interview = InterviewFixture.create();

        assertAll(
                () -> assertThat(interview.getScoreGroup().getSuccess()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getFail()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getPass()).isEqualTo(0)
        );
    }


    @Test
    void 면접에_대한_답변을_체출할_떄_80점을_넘기면_정답_개수가_증가한다(){

        var interview = InterviewFixture.create();

        interview.submit(new Answer(AnswerState.COMPLETE, "답변", 10), new AIFeedback("꼬리질문", "피드백", 80, List.of()));

        assertAll(
                () -> assertThat(interview.getScoreGroup().getSuccess()).isEqualTo(1),
                () -> assertThat(interview.getScoreGroup().getFail()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getPass()).isEqualTo(0)
        );

    }

    @Test
    void 면접_답변_제출_시_80점을_넘기지_못하면_오답_개수가_증가한다(){

        var interview = InterviewFixture.create();

        interview.submit(new Answer(AnswerState.COMPLETE, "답변", 10), new AIFeedback("꼬리질문", "피드백", 70, List.of()));

        assertAll(
                () -> assertThat(interview.getScoreGroup().getSuccess()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getFail()).isEqualTo(1),
                () -> assertThat(interview.getScoreGroup().getPass()).isEqualTo(0)
        );

    }

    @Test
    void 면접_답변을_제출하지_않으면_통과_개수가_증가한다(){
        var interview = InterviewFixture.create();

        interview.submit(new Answer(AnswerState.PASS, "답변", 10), new AIFeedback(null, null, null, null));

        assertAll(
                () -> assertThat(interview.getScoreGroup().getSuccess()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getFail()).isEqualTo(0),
                () -> assertThat(interview.getScoreGroup().getPass()).isEqualTo(1)
        );
    }


}