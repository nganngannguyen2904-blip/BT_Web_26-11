package com.accounthub.config;

import com.accounthub.entity.Category;
import com.accounthub.entity.User;
import com.accounthub.repository.CategoryRepository;
import com.accounthub.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Khởi tạo dữ liệu mẫu an toàn khi ứng dụng khởi chạy nếu chưa có dữ liệu.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           CategoryRepository categoryRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            logger.info("Chưa có dữ liệu người dùng. Tiến hành tạo các tài khoản mẫu thử nghiệm...");

            // 1. Tài khoản sinh viên / người dùng bình thường
            User user1 = new User();
            user1.setFullName("Nguyễn Bảo Ngân");
            user1.setEmail("nguyenbaongan@example.com");
            user1.setUsername("nguyenbao");
            user1.setPassword(passwordEncoder.encode("Password@123"));
            user1.setStatus("ACTIVE");
            userRepository.save(user1);

            // 2. Tài khoản quản trị
            User user2 = new User();
            user2.setFullName("Quản Trị Viên");
            user2.setEmail("admin@accounthub.vn");
            user2.setUsername("admin");
            user2.setPassword(passwordEncoder.encode("Admin@123"));
            user2.setStatus("ACTIVE");
            userRepository.save(user2);

            // 3. Tài khoản bị khóa (dùng để kiểm thử không cho phép đăng nhập)
            User user3 = new User();
            user3.setFullName("Tài Khoản Bị Khóa");
            user3.setEmail("locked@example.com");
            user3.setUsername("lockeduser");
            user3.setPassword(passwordEncoder.encode("Password@123"));
            user3.setStatus("LOCKED");
            userRepository.save(user3);

            logger.info("Khởi tạo thành công 3 tài khoản mẫu:");
            logger.info("1. nguyenbao / Password@123 (Trạng thái: ACTIVE)");
            logger.info("2. admin / Admin@123 (Trạng thái: ACTIVE)");
            logger.info("3. lockeduser / Password@123 (Trạng thái: LOCKED)");
        }

        if (categoryRepository.count() == 0) {
            logger.info("Chưa có danh mục sản phẩm. Tiến hành tạo danh mục mẫu để test tìm kiếm phân trang...");
            categoryRepository.save(new Category("Điện thoại di động", "phone.png", 1));
            categoryRepository.save(new Category("Laptop & Máy tính xách tay", "laptop.png", 1));
            categoryRepository.save(new Category("Đồng hồ thông minh", "smartwatch.png", 1));
            categoryRepository.save(new Category("Phụ kiện công nghệ", "accessories.png", 1));
            categoryRepository.save(new Category("Thiết bị âm thanh", "audio.png", 1));
            categoryRepository.save(new Category("Máy tính bảng (Tablet)", "tablet.png", 1));
            categoryRepository.save(new Category("Màn hình máy tính", "monitor.png", 1));
            categoryRepository.save(new Category("Bàn phím cơ & Chuột", "keyboard.png", 1));
            logger.info("Đã khởi tạo 8 danh mục mẫu thành công.");
        }
    }
}
