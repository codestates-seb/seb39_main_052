package com.seb39.myfridge.member.service;


import com.seb39.myfridge.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.fridge.service.FridgeService;
import com.seb39.myfridge.image.upload.FileUploadService;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
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

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. email = " + email));
    }

    public Member findOAuth2Member(String provider, String providerId) {
        return memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseThrow(() -> new IllegalArgumentException("Member not exist. provider = " + provider + " " + "providerId = " + providerId));
    }

    public boolean existByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public boolean existById(Long id) {
        return memberRepository.existsById(id);
    }

    public void signUpGeneral(Member member) {
        verifyBeforeSignUpGeneral(member);
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.saveEncryptedPassword(encryptedPassword);
        memberRepository.save(member);
        fridgeService.createFridge(member);
    }

    private void verifyBeforeSignUpGeneral(Member member) {
        String email = member.getEmail();
        if (existByEmail(email))
            throw new AppAuthenticationException(AppAuthExceptionCode.EXISTS_MEMBER);
    }

    public void signUpOauth2IfNotExists(Member member) {
        String provider = member.getProvider();
        String providerId = member.getProviderId();
        if (existOAuth2Member(provider, providerId))
            return;
        memberRepository.save(member);
        //회원가입시 냉장고 생성
        fridgeService.createFridge(member);
    }

    public boolean existOAuth2Member(String provider, String providerId) {
        return memberRepository.existsByProviderAndProviderId(provider, providerId);
    }

    @Transactional
    public void updateName(Member member, String newName) {
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
