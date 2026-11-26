package com.accounthub.service;

import com.accounthub.dto.RegisterDto;
import com.accounthub.entity.User;

import java.util.Optional;

/**
 * Service định nghĩa các nghiệp vụ liên quan đến tài khoản người dùng.
 */
public interface UserService {

    /**
     * Đăng ký người dùng mới sau khi kiểm tra hợp lệ và mã hóa mật khẩu bằng BCrypt.
     */
    User registerUser(RegisterDto registerDto);

    /**
     * Kiểm tra username đã tồn tại chưa.
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại chưa.
     */
    boolean existsByEmail(String email);

    /**
     * Tìm tài khoản bằng username hoặc email.
     */
    Optional<User> findByUsernameOrEmail(String identifier);

    /**
     * Tìm tài khoản bằng username.
     */
    Optional<User> findByUsername(String username);

    /**
     * Tìm tài khoản bằng email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Tìm tài khoản theo ID.
     */
    Optional<User> findById(Long id);
}
