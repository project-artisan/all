package org.artisan.domain;


import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InterviewQuestions {

    @OneToMany(mappedBy = "interview")
    private List<InterviewQuestion> value = new ArrayList<>();

    public static InterviewQuestions empty() {
        return new InterviewQuestions();
    }

    public InterviewQuestion get(int index) {
        return value.get(index);
    }

    public List<InterviewQuestion> getValue(){
        return Collections.unmodifiableList(value);
    }
}
