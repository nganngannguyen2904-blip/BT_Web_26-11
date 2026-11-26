-- ====================================================================
-- ACCOUNT MANAGEMENT SYSTEM (ACCOUNT HUB)
-- Database: account_hub
-- Tables: users, categories
-- Character Set: utf8mb4 (Full Vietnamese Unicode support)
-- ====================================================================

CREATE DATABASE IF NOT EXISTS `account_hub`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE `account_hub`;

-- Drop tables if exist
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `users`;

-- 1. Create users table
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `full_name` VARCHAR(100) NOT NULL COMMENT 'Họ và tên người dùng',
    `email` VARCHAR(100) NOT NULL COMMENT 'Địa chỉ email (duy nhất)',
    `username` VARCHAR(50) NOT NULL COMMENT 'Tên đăng nhập (duy nhất)',
    `password` VARCHAR(255) NOT NULL COMMENT 'Mật khẩu đã được mã hóa bằng BCrypt',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Thời điểm tạo tài khoản',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Thời điểm cập nhật tài khoản',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT 'Trạng thái tài khoản: ACTIVE, LOCKED, INACTIVE',
    PRIMARY KEY (`id`),
    CONSTRAINT `uk_users_email` UNIQUE (`email`),
    CONSTRAINT `uk_users_username` UNIQUE (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng lưu thông tin tài khoản người dùng';

-- 2. Create categories table
CREATE TABLE `categories` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(200) NOT NULL COMMENT 'Tên danh mục sản phẩm',
    `images` VARCHAR(500) NULL COMMENT 'Hình ảnh danh mục',
    `status` INT DEFAULT 1 COMMENT 'Trạng thái: 1 = Hoạt động, 0 = Khóa',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Bảng lưu thông tin danh mục sản phẩm';
