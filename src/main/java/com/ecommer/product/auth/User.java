package com.ecommer.product.auth;


import io.ecommer.grpc.UserResponse;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User implements UserDetails {
    private Long id;
    private String email;
    private String username;
    private String profileImageUrl;
    private String phoneNumber;
    private String address;
    @Builder.Default
    private String role = "ROLE_USER";


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(()-> role);
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
    public static User from(UserResponse userResponse) {
        return User.builder()
                .id((long) userResponse.getId())
                .email(userResponse.getEmail())
                .username(userResponse.getUsername())
                .phoneNumber(userResponse.getPhone())
                .address(userResponse.getAddress())
                .profileImageUrl(userResponse.getProfile())
                .build();
    }
}
