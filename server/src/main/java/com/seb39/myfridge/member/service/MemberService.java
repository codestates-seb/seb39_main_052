package com.seb39.myfridge.member.service;


import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findByEmail(String email){
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        return optionalMember
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. email = " + email));
    }

    public boolean exist(String email){
        return memberRepository.existsByEmail(email);
    }

    public void signUpGeneral(Member member){
        verifyBeforeSignUp(member);

        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.saveEncryptedPassword(encryptedPassword);
        memberRepository.save(member);
    }

    public void signUpOauth2(Member member){
        verifyBeforeSignUp(member);
        memberRepository.save(member);
    }

    private void verifyBeforeSignUp(Member member){
        String email = member.getEmail();
        if(exist(email))
            throw new AppAuthenticationException(AppAuthExceptionCode.EXISTS_EMAIL);
    }

    public boolean existOAuth2Member(String provider, String providerId){
        return memberRepository.existsByProviderAndProviderId(provider,providerId);
    }

    //findById 추가
    public Member findById(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        return optionalMember.orElseThrow(() -> new IllegalArgumentException("Member not exist. id = " + id));
    }
}
