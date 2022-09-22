package com.seb39.myfridge.member.mapper;

import com.seb39.myfridge.member.dto.MemberDto;
import com.seb39.myfridge.member.entity.Member;

public interface MemberMapper {
    static MemberDto.Response memberToResponseDto(Member member){
        MemberDto.Response dto = new MemberDto.Response();
        dto.setMemberId(member.getId());
        dto.setName(member.getName());
        dto.setProfileImagePath(member.getProfileImagePath());
        return dto;
    }
}
