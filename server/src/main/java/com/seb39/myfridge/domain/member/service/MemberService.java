package com.seb39.myfridge.domain.member.service;


import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.domain.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.domain.fridge.service.FridgeService;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.image.upload.FileUploadService;
import com.seb39.myfridge.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final FridgeService fridgeService;
    private final FileUploadService fileUploadService;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. id = " + id));
    }

    public Member findGeneralByEmail(String email) {
        return memberRepository.findGeneralByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. email = " + email));
    }

    public Member findOAuth2Member(String provider, String providerId) {
        return memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. provider = " + provider + " " + "providerId = " + providerId));
    }

    public boolean existsGeneralByEmail(String email) {
        return memberRepository.findGeneralByEmail(email).isPresent();
    }

    public boolean existById(Long id) {
        return memberRepository.existsById(id);
    }

    @Transactional
    public void signUpGeneral(Member member) {
        verifyBeforeSignUpGeneral(member);
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.saveEncryptedPassword(encryptedPassword);
        memberRepository.save(member);
        fridgeService.createFridge(member);
    }

    private void verifyBeforeSignUpGeneral(Member member) {
        if (existsGeneralByEmail(member.getEmail()))
            throw new AppAuthenticationException(AppAuthExceptionCode.EXISTS_EMAIL);

        if (existsByName(member.getName()))
            throw new AppAuthenticationException(AppAuthExceptionCode.EXISTS_NAME);
    }

    private boolean existsByName(String name) {
        return memberRepository.existsByName(name);
    }

    @Transactional
    public void signUpOauth2IfNotExists(Member member) {
        String provider = member.getProvider();
        String providerId = member.getProviderId();
        if (existOAuth2Member(provider, providerId))
            return;
        memberRepository.save(member);
        fridgeService.createFridge(member);
    }

    public boolean existOAuth2Member(String provider, String providerId) {
        return memberRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public Member signUpGuest() {
        String generatedName = generateGuestName();
        Member member = Member.generalBuilder()
                .name(generatedName)
                .email(generatedName + "@seb39myfridge.ml")
                .password("guestpassword")
                .buildGeneralMember();
        signUpGeneral(member);
        return member;
    }

    private String generateGuestName() {
        for (int i = 0; i < 3; i++) {
            String generatedName = "GUEST_" + UUID.randomUUID();
            if (!existsByName(generatedName))
                return generatedName;
        }
        throw new RuntimeException("Failed to generate guest name");
    }

    @Transactional
    public void updateName(Member member, String newName) {
        if (member.getName().equals(newName))
            return;

        if (existsByName(newName))
            throw new IllegalArgumentException("UpdateName Error. name already exist. name = " + newName);

        member.changeName(newName);
    }

    @Transactional
    public void updateProfileImage(Member member, MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty())
            throw new IllegalArgumentException("profileImage null or empty.");

        String uploadedUri = fileUploadService.uploadImage(profileImage);
        member.changeProfileImagePath(uploadedUri);
    }
}
