-- SSO 数据库
CREATE DATABASE `sso_db` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';

-- SSO 用户表
CREATE TABLE IF NOT EXISTS `sso_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` VARCHAR(64) NOT NULL COMMENT '业务用户ID',
    `username` VARCHAR(64) NOT NULL COMMENT '用户名',
    `password` VARCHAR(128) NOT NULL COMMENT 'BCrypt加密密码',
    `email` VARCHAR(128) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(32) DEFAULT NULL COMMENT '手机号',
    `real_name` VARCHAR(64) DEFAULT NULL COMMENT '真实姓名',
    `department` VARCHAR(128) DEFAULT NULL COMMENT '部门',
    `avatar` VARCHAR(256) DEFAULT NULL COMMENT '头像URL',
    `enabled` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '账户状态: 1启用 0禁用',
    `locked` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '锁定状态: 1锁定 0未锁定',
    `expired` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '过期状态: 1已过期 0未过期',
    `credentials_expired` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '凭证过期: 1已过期 0未过期',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SSO用户表';

-- 初始化管理员账号 (密码: admin123 的 BCrypt 哈希)
INSERT INTO `sso_user` (`user_id`, `username`, `password`, `email`, `real_name`, `enabled`)
VALUES ('admin001', 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'admin@tengo.com', '系统管理员', 1)
ON DUPLICATE KEY UPDATE `username` = VALUES(`username`);
