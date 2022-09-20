package com.seb39.myfridge.auth.service;

import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
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
        if(!memberService.existByEmail(email))
            throw new AppAuthenticationException(AppAuthExceptionCode.INVALID_EMAIL_OR_PASSWORD);

        Member member = memberService.findByEmail(email);
        return PrincipalDetails.general(member);
    }
}
