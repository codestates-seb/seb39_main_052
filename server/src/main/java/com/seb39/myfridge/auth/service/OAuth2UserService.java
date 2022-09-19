package com.seb39.myfridge.auth.service;

import com.seb39.myfridge.auth.PrincipalDetails;
import com.seb39.myfridge.auth.userinfo.GoogleUserInfo;
import com.seb39.myfridge.auth.userinfo.KakaoUserInfo;
import com.seb39.myfridge.auth.userinfo.OAuth2UserInfo;
import com.seb39.myfridge.member.entity.Member;
import com.seb39.myfridge.member.service.MemberService;
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

    /**
     * 인증 서비스 제공자로부터 받은 userRequest 데이터를 후처리하기 위한 함수.
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo userInfo;
        if(provider.equals("google")){
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(provider.equals("kakao")){
            userInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }else{
            throw new RuntimeException("OAuth2 provider is not supported. Provider = " + provider);
        }

        String username = userInfo.getUsername();
        String email = userInfo.getEmail();
        String providerId = userInfo.getProviderId();

        if(!memberService.existOAuth2Member(provider, providerId)){
            Member member = Member.oauth2Builder()
                    .name(username)
                    .email(email)
                    .provider(provider)
                    .providerId(providerId)
                    .buildOAuth2Member();
            memberService.signUpOauth2(member);
        }

        Member member = memberService.findByEmail(email);
        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
