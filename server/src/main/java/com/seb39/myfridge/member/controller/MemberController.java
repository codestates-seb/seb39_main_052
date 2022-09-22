package com.seb39.myfridge.member.controller;

import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.mapper.MemberMapper;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static com.seb39.myfridge.member.mapper.MemberMapper.*;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<MemberDto.Response> getMember(@PathVariable Long memberId){
        Member member = memberService.findById(memberId);
        MemberDto.Response dto = memberToResponseDto(member);
        return ResponseEntity.ok(dto);
    }
}
