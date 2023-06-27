package com.svanty.security;

import com.svanty.module.core.db.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


public class UserDetailsSecurity implements UserDetails {
    private User user;

    public UserDetailsSecurity(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> ret = new ArrayList<>();

        String roleStr = "ROLE_USER";
        switch (user.getRole()) {
            case admin:
                roleStr = "ROLE_ADMIN";
                break;
        }

        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(roleStr);

        ret.add(grantedAuthority);

        return ret;
    }

    public int getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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

    public User getUserDetails() {
        return user;
    }
}