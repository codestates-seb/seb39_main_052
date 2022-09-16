package com.seb39.myfridge.step.service;

import com.seb39.myfridge.recipe.entity.Recipe;
import com.seb39.myfridge.step.entity.Step;
import com.seb39.myfridge.step.repository.StepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;

    @Transactional
    public Step createStep(Step step) {
        Step savedStep = stepRepository.save(step);
        return savedStep;
    }
}
