package com.seb39.myfridge.member.service;


import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. id = " + id));
    }

    public Member findByEmail(String email){
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. email = " + email));
    }

    public Member findOAuth2Member(String provider, String providerId){
        return memberRepository.findByProviderAndProviderId(provider,providerId)
                .orElseThrow(()-> new IllegalArgumentException("Member not exist. provider = " + provider + " " + "providerId = " + providerId));
    }

    public boolean existByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    public boolean existById(Long id){
        return memberRepository.existsById(id);
    }

    public void signUpGeneral(Member member){
        verifyBeforeSignUpGeneral(member);
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.saveEncryptedPassword(encryptedPassword);
        memberRepository.save(member);
    }

    private void verifyBeforeSignUpGeneral(Member member){
        String email = member.getEmail();
        if(existByEmail(email))
            throw new AppAuthenticationException(AppAuthExceptionCode.EXISTS_MEMBER);
    }

    public void signUpOauth2IfNotExists(Member member){
        String provider = member.getProvider();
        String providerId = member.getProviderId();
        if(existOAuth2Member(provider,providerId))
            return;
        memberRepository.save(member);
    }

    public boolean existOAuth2Member(String provider, String providerId){
        return memberRepository.existsByProviderAndProviderId(provider,providerId);
    }
}
