package com.seb39.myfridge.member.controller;

import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<MemberDto.Response> getMember(@PathVariable Long memberId){
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(new MemberDto.Response(member));
    }
}