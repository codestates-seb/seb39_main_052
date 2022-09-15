package com.seb39.myfridge.step.repository;

import com.seb39.myfridge.step.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRepository extends JpaRepository<Step, Long> {

}
