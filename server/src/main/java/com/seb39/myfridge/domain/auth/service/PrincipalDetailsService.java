package com.seb39.myfridge.domain.auth.service;

import com.seb39.myfridge.domain.auth.PrincipalDetails;
import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.domain.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(!memberService.existsGeneralByEmail(email))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD);

        Member member = memberService.findGeneralByEmail(email);
        return PrincipalDetails.general(member);
    }
}
