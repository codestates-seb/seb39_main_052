package com.seb39.myfridge.domain.auth;

import com.seb39.myfridge.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {

    private final Member member;
    private Map<String, Object> attributes;

    private PrincipalDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    public static PrincipalDetails general(Member member){
        return new PrincipalDetails(member,null);
    }

    public static PrincipalDetails oauth2(Member member, Map<String,Object> attributes){
        return new PrincipalDetails(member,attributes);
    }

    public Long getMemberId() {
        return member.getId();
    }

    //region UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        member.getRoleList()
                .forEach(role -> authorities.add(() -> role));
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        if(member.isGeneralMember())
            return member.getEmail();

        return member.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    //endregion

    //region OAuth2User
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return member.getName();
    }
    //endregion
}