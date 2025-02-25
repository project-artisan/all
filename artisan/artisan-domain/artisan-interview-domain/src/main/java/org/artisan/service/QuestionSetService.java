package org.artisan.service;


import lombok.RequiredArgsConstructor;
import org.artisan.domain.Question;
import org.artisan.domain.QuestionSet;
import org.artisan.domain.QuestionSetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionSetService {

    private final QuestionSetRepository questionSetRepository;

    @Transactional(readOnly = true)
    public Slice<QuestionSet> find(Pageable pageable) {
        return questionSetRepository.findAll(pageable);
    }


}
