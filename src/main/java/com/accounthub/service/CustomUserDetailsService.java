package com.accounthub.service;

import com.accounthub.entity.User;
import com.accounthub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Service xác thực người dùng cho Spring Security,
 * hỗ trợ đăng nhập linh hoạt bằng cả Username hoặc Email.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.isBlank()) {
            throw new UsernameNotFoundException("Tên đăng nhập hoặc email không được để trống");
        }

        User user = userRepository.findByUsernameOrEmailIgnoreCase(identifier.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Tài khoản hoặc mật khẩu không chính xác"));

        boolean isLocked = "LOCKED".equalsIgnoreCase(user.getStatus());
        boolean isEnabled = "ACTIVE".equalsIgnoreCase(user.getStatus());

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .disabled(!isEnabled && !isLocked)
                .accountLocked(isLocked)
                .accountExpired(false)
                .credentialsExpired(false)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }
}
