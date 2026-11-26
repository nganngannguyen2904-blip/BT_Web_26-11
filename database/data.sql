-- ====================================================================
-- ACCOUNT MANAGEMENT SYSTEM (ACCOUNT HUB)
-- Sample Data for Users and Categories
-- ====================================================================

USE `account_hub`;

-- 1. Sample Users (nguyenbao & lockeduser: Password@123 | admin: Admin@123)
INSERT INTO `users` (`full_name`, `email`, `username`, `password`, `created_at`, `updated_at`, `status`)
VALUES 
('Nguyễn Bảo Ngân', 'nguyenbaongan@example.com', 'nguyenbao', '$2a$10$Td4VrpcaaYAgjltKzsEY.eDSKsr6/JBgypkurNo40.GCxVNuczklu', NOW(), NOW(), 'ACTIVE'),
('Quản Trị Viên', 'admin@accounthub.vn', 'admin', '$2a$10$iRYJmgp5ObnfSqNjsXOyGObf83J1sR665F3qzoj7I6qBFeEduSJs6', NOW(), NOW(), 'ACTIVE'),
('Tài Khoản Bị Khóa', 'locked@example.com', 'lockeduser', '$2a$10$Td4VrpcaaYAgjltKzsEY.eDSKsr6/JBgypkurNo40.GCxVNuczklu', NOW(), NOW(), 'LOCKED')
ON DUPLICATE KEY UPDATE `updated_at` = NOW();

-- 2. Sample Categories for Search and Pagination testing
INSERT INTO `categories` (`name`, `images`, `status`) VALUES
('Điện thoại di động', 'phone.png', 1),
('Laptop & Máy tính xách tay', 'laptop.png', 1),
('Đồng hồ thông minh', 'smartwatch.png', 1),
('Phụ kiện công nghệ', 'accessories.png', 1),
('Thiết bị âm thanh', 'audio.png', 1),
('Máy tính bảng (Tablet)', 'tablet.png', 1),
('Màn hình máy tính', 'monitor.png', 1),
('Bàn phím cơ & Chuột', 'keyboard.png', 1);
