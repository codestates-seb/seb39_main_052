package com.seb39.myfridge.domain.fridge.service;

import com.seb39.myfridge.domain.fridge.entity.Fridge;
import com.seb39.myfridge.domain.fridge.repository.FridgeRepository;
import com.seb39.myfridge.domain.member.entity.Member;
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

    public Fridge findFridge(Long memberId) {

        return fridgeRepository.findFridgeByMemberId(memberId);
    }

}
