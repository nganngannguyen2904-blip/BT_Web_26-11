package com.accounthub;

import com.accounthub.dto.RegisterDto;
import com.accounthub.entity.User;
import com.accounthub.repository.UserRepository;
import com.accounthub.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("TC 1: Đăng ký tài khoản thành công")
    void testRegisterUser_Success() {
        RegisterDto dto = new RegisterDto(
                "Trần Văn Bình",
                "binhtran@example.com",
                "tranbinh",
                "Secret@123",
                "Secret@123"
        );

        User savedUser = userService.registerUser(dto);

        assertNotNull(savedUser.getId(), "User ID phải được tự động sinh");
        assertEquals("Trần Văn Bình", savedUser.getFullName());
        assertEquals("binhtran@example.com", savedUser.getEmail());
        assertEquals("tranbinh", savedUser.getUsername());
        assertEquals("ACTIVE", savedUser.getStatus());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }

    @Test
    @DisplayName("TC 4: Đăng ký email đã tồn tại - Ném ngoại lệ")
    void testRegisterUser_DuplicateEmail_ThrowsException() {
        RegisterDto dto1 = new RegisterDto(
                "Người Dùng 1",
                "trungemail@example.com",
                "userone",
                "Secret@123",
                "Secret@123"
        );
        userService.registerUser(dto1);

        RegisterDto dto2 = new RegisterDto(
                "Người Dùng 2",
                "trungemail@example.com",
                "usertwo",
                "Secret@123",
                "Secret@123"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(dto2);
        });
        assertTrue(ex.getMessage().contains("Email"));
    }

    @Test
    @DisplayName("TC 5: Đăng ký username đã tồn tại - Ném ngoại lệ")
    void testRegisterUser_DuplicateUsername_ThrowsException() {
        RegisterDto dto1 = new RegisterDto(
                "Người Dùng 1",
                "email1@example.com",
                "sameusername",
                "Secret@123",
                "Secret@123"
        );
        userService.registerUser(dto1);

        RegisterDto dto2 = new RegisterDto(
                "Người Dùng 2",
                "email2@example.com",
                "sameusername",
                "Secret@123",
                "Secret@123"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(dto2);
        });
        assertTrue(ex.getMessage().contains("Tên đăng nhập"));
    }

    @Test
    @DisplayName("TC 7: Mật khẩu và xác nhận mật khẩu không trùng khớp - Ném ngoại lệ")
    void testRegisterUser_PasswordMismatch_ThrowsException() {
        RegisterDto dto = new RegisterDto(
                "Nguyễn Văn A",
                "nguyenvana@example.com",
                "nguyenvana",
                "Password@123",
                "DifferentPassword@123"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(dto);
        });
        assertTrue(ex.getMessage().contains("không trùng khớp"));
    }

    @Test
    @DisplayName("TC 8: Kiểm tra mật khẩu trong database đã được BCrypt mã hóa")
    void testRegisterUser_PasswordIsBCryptHashed() {
        String plainPassword = "MySecurePassword@2026";
        RegisterDto dto = new RegisterDto(
                "Kiểm Tra BCrypt",
                "bcrypt@example.com",
                "bcrpytuser",
                plainPassword,
                plainPassword
        );

        User user = userService.registerUser(dto);

        // Mật khẩu lưu trữ không được là mật khẩu gốc dạng text thuần
        assertNotEquals(plainPassword, user.getPassword());
        // Mật khẩu BCrypt bắt đầu bằng $2a$ hoặc $2b$ và có độ dài 60 ký tự
        assertTrue(user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$"));
        assertEquals(60, user.getPassword().length());
        // Kiểm tra giải mã thành công với BCryptPasswordEncoder
        assertTrue(passwordEncoder.matches(plainPassword, user.getPassword()));
    }
}
