package org.artisan.test.testfixtures;

import java.util.List;
import org.artisan.domain.Interview;
import org.artisan.domain.InterviewMetadata;
import org.artisan.domain.InterviewProgress;
import org.artisan.domain.InterviewQuestion;
import org.artisan.domain.InterviewSetting;
import org.artisan.domain.Question;
import org.artisan.domain.QuestionMetadata;
import org.artisan.domain.QuestionSet;
import org.artisan.domain.QuestionSetMetadata;
import org.artisan.domain.QuestionSetRule;
import org.artisan.domain.file.ExternalURL;
import org.artisan.domain.file.FileType;
import org.artisan.test.fixtures.MemberFixture;

public class InterviewFixture {

    public static Interview create(){

        var member = MemberFixture.create();
        var interview = new Interview(
                member,
                new InterviewMetadata("인터뷰 제목"),
                InterviewProgress.of(10),
                new InterviewSetting(10, 10, 10)
        );

        var question = new Question(
                new QuestionMetadata("", 1, 1),
                new QuestionSet(new QuestionSetMetadata("", "", new ExternalURL(FileType.AWS_STORAGE_URL, ""), 1), new QuestionSetRule(1, 1, 1))
        );

        interview.setInterviewQuestions(List.of(
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2),
                InterviewQuestion.of(interview, member, question, 2)
        ));

        return interview;
    }
}
