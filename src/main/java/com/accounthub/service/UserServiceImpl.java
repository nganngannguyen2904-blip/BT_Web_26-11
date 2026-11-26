package com.accounthub.service;

import com.accounthub.dto.RegisterDto;
import com.accounthub.entity.User;
import com.accounthub.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Triển khai các nghiệp vụ tài khoản: đăng ký, kiểm tra trùng lặp, mã hóa BCrypt.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerUser(RegisterDto registerDto) {
        // 1. Kiểm tra xác nhận mật khẩu trùng khớp
        if (registerDto.getPassword() == null || !registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Mật khẩu và xác nhận mật khẩu không trùng khớp");
        }

        // 2. Kiểm tra email đã tồn tại
        String cleanEmail = registerDto.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(cleanEmail)) {
            throw new IllegalArgumentException("Email '" + cleanEmail + "' đã được sử dụng");
        }

        // 3. Kiểm tra username đã tồn tại
        String cleanUsername = registerDto.getUsername().trim();
        if (userRepository.existsByUsernameIgnoreCase(cleanUsername)) {
            throw new IllegalArgumentException("Tên đăng nhập '" + cleanUsername + "' đã tồn tại");
        }

        // 4. Mã hóa mật khẩu bằng BCrypt
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());

        // 5. Khởi tạo đối tượng User và lưu vào cơ sở dữ liệu
        User user = new User();
        user.setFullName(registerDto.getFullName().trim());
        user.setEmail(cleanEmail);
        user.setUsername(cleanUsername);
        user.setPassword(encodedPassword);
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        if (username == null || username.isBlank()) {
            return false;
        }
        return userRepository.existsByUsernameIgnoreCase(username.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return userRepository.existsByEmailIgnoreCase(email.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByUsernameOrEmailIgnoreCase(identifier.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        if (username == null || username.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.trim().toLowerCase());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }
}
