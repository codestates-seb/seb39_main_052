package com.seb39.myfridge.member.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    private String roles;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    private String provider;

    private String providerId;

    private enum MemberStatus{
        ACTIVE,
        INACTIVE
    }

    @Builder(builderMethodName = "generalBuilder", buildMethodName = "buildGeneralMember")
    private static Member createGeneralMember(String name, String email, String password){
        Member member = new Member();
        member.initDefaultRolesAndStatus();
        member.name = name;
        member.email = email;
        member.password = password;
        return member;
    }

    @Builder(builderMethodName = "oauth2Builder", buildMethodName = "buildOAuth2Member")
    private static Member createOAuth2Member(String name, String email, String provider, String providerId){
        Member member = new Member();
        member.initDefaultRolesAndStatus();
        member.name = name;
        member.email = email;
        member.provider = provider;
        member.providerId = providerId;
        return member;
    }

    private void initDefaultRolesAndStatus(){
        this.roles = "ROLE_USER";
        this.status = MemberStatus.ACTIVE;
    }

    public List<String> getRoleList() {
        if (!StringUtils.hasText(roles))
            return List.of();

        return Arrays.stream(this.roles.split(","))
                .collect(Collectors.toList());
    }

    public void saveEncryptedPassword(String encryptedPassword){
        this.password = encryptedPassword;
    }
}
