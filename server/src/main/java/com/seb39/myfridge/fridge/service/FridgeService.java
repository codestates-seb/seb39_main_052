package com.seb39.myfridge.fridge.service;

import com.seb39.myfridge.fridge.entity.Fridge;
import com.seb39.myfridge.fridge.repository.FridgeRepository;
import com.seb39.myfridge.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FridgeService {

    private final FridgeRepository fridgeRepository;

    @Transactional
    public Fridge createFridge(Member member) {
        Fridge fridge = new Fridge();
        fridge.setMember(member);
        return fridgeRepository.save(fridge);
    }
}
