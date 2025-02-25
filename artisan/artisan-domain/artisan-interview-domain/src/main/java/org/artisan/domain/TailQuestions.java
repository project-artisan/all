package org.artisan.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class TailQuestions {

    @OneToMany(mappedBy = "interviewQuestion", fetch = FetchType.LAZY)
    private List<TailQuestion> value = new ArrayList<>();

    public List<TailQuestion> getValue(){
        return Collections.unmodifiableList(value);
    }

}
