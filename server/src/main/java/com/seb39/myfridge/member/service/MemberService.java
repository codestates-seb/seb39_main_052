package com.seb39.myfridge.member.service;


import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. email = " + email));
    }

    public boolean exist(String email){
        return memberRepository.existsByEmail(email);
    }
}
