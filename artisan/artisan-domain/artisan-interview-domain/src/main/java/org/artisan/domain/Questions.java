package org.artisan.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Embeddable
public class Questions {

    @OneToMany(mappedBy = "questionSet", fetch = FetchType.EAGER)
    List<Question> value = new ArrayList<>();


    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean hasSameSize(int count) {
        return value.size() == count;
    }

    public List<Question> getValue() {
        return Collections.unmodifiableList(value);
    }

    public int size() {
        return value.size();
    }

    public List<Question> shuffle() {

        var temp = new ArrayList<>(this.getValue());

        Collections.shuffle(temp);

        return temp;
    }

    public List<Question> getOrderedSequenceValue() {
        return this.value.stream()
                .sorted(Comparator.comparingInt(question -> question.getMetadata().sequence()))
                .toList();
    }
}
