package com.seb39.myfridge.domain.fridge.repository;

import com.seb39.myfridge.domain.fridge.entity.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FridgeRepository extends JpaRepository<Fridge, Long> {

    Fridge findFridgeByMemberId(Long id);

}
