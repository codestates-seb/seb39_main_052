package com.seb39.myfridge.member.controller;

import com.seb39.myfridge.auth.annotation.AuthMemberId;
import com.seb39.myfridge.image.upload.FileUploadService;
import com.seb39.myfridge.image.upload.UploadService;
import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/members/{memberId}")
    public ResponseEntity<MemberDto.ResponseDetail> getMember(@PathVariable Long memberId) {
        Member member = memberService.findById(memberId);
        return ResponseEntity.ok(new MemberDto.ResponseDetail(member));
    }

    @PatchMapping(value = "/api/members", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Secured("ROLE_USER")
    public ResponseEntity<MemberDto.Response> patchMember(@Valid @RequestPart MemberDto.Patch requestBody,
                                                          @RequestPart(required = false) MultipartFile profileImage,
                                                          @AuthMemberId Long memberId) {
        Member member = memberService.findById(memberId);
        memberService.updateName(member, requestBody.getName());

        if(profileImage != null && !profileImage.isEmpty())
            memberService.updateProfileImage(member,profileImage);

        return ResponseEntity.ok(new MemberDto.Response(member));
    }
}
