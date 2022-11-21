package com.seb39.myfridge.domain.auth.service;

import com.seb39.myfridge.domain.auth.PrincipalDetails;
import com.seb39.myfridge.domain.auth.enums.AppAuthExceptionCode;
import com.seb39.myfridge.domain.auth.exception.AppAuthenticationException;
import com.seb39.myfridge.domain.auth.userinfo.OAuth2UserInfo;
import com.seb39.myfridge.domain.auth.userinfo.UserInfoFactory;
import com.seb39.myfridge.domain.member.entity.Member;
import com.seb39.myfridge.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo = UserInfoFactory.create(provider,oAuth2User.getAttributes())
                .orElseThrow(()-> new AppAuthenticationException(AppAuthExceptionCode.INVALID_OAUTH2_PROVIDER));

        String email = userInfo.getEmail();
        String providerId = userInfo.getProviderId();
        String profileImagePath = userInfo.getProfileImagePath();
        String username = createDefaultUsername(provider,providerId);

        Member member = Member.oauth2Builder()
                .name(username)
                .email(email)
                .provider(provider)
                .providerId(providerId)
                .profileImagePath(profileImagePath)
                .buildOAuth2Member();
        memberService.signUpOauth2IfNotExists(member);

        Member findMember = memberService.findOAuth2Member(provider,providerId);
        return PrincipalDetails.oauth2(findMember, oAuth2User.getAttributes());
    }

    public String createDefaultUsername(String provider, String providerId){
        StringBuilder sb = new StringBuilder();
        sb.append(provider);
        sb.append("_");
        sb.append(providerId);
        return sb.toString();
    }
}
