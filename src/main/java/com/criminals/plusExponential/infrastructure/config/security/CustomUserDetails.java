package com.criminals.plusExponential.infrastructure.config.security;

import com.criminals.plusExponential.domain.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;


public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User 테이블에 들어 있는 role (예: "PASSENGER") 을 가져온 후,
        // "ROLE_" prefix 를 붙여서 SimpleGrantedAuthority 객체에 담아 반환
        String userRole = user.getRole().name(); // 예: "PASSENGER"
        String roleWithPrefix = "ROLE_" + userRole; // "ROLE_PASSENGER"

        return Collections.singletonList(new SimpleGrantedAuthority(roleWithPrefix));
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // User 엔티티의 password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // 계정 만료 등 기타 설정들을 간단히 true로 처리
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
}
