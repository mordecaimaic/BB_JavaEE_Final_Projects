-- 校园小助手数据库初始化脚本
-- 版本: 1.0
-- 作者: [您的名字]
-- 创建日期: [当前日期]

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `users`, `courses`, `user_courses`, `assignments`, `submissions`, `announcements`, `schedules`, `materials`, `material_comments`;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. 用户表
CREATE TABLE `users` (
`user_id` INT PRIMARY KEY AUTO_INCREMENT,
`username` VARCHAR(50) NOT NULL UNIQUE,
`password` VARCHAR(255) NOT NULL COMMENT '存储加密后的密码',
`phone` VARCHAR(20),
`email` VARCHAR(100),
`student_id` VARCHAR(20) UNIQUE COMMENT '学号/工号',
`department` VARCHAR(50) COMMENT '院系',
`role` ENUM('student', 'teacher', 'admin') DEFAULT 'student',
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
INDEX `idx_department` (`department`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 课程表
CREATE TABLE `courses` (
`course_id` INT PRIMARY KEY AUTO_INCREMENT,
`course_name` VARCHAR(100) NOT NULL,
`teacher_id` INT,
`classroom` VARCHAR(50),
`schedule` VARCHAR(100) COMMENT '如"周一 1-2节"',
`credit` TINYINT,
`type` ENUM('必修', '选修'),
`description` TEXT,
FOREIGN KEY (`teacher_id`) REFERENCES `users`(`user_id`),
INDEX `idx_teacher` (`teacher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 用户-课程关联表
CREATE TABLE `user_courses` (
`id` INT PRIMARY KEY AUTO_INCREMENT,
`user_id` INT NOT NULL,
`course_id` INT NOT NULL,
`semester` VARCHAR(20) COMMENT '如"2023-2024-1"',
FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`) ON DELETE CASCADE,
UNIQUE KEY `uniq_user_course` (`user_id`, `course_id`, `semester`),
INDEX `idx_semester` (`semester`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 作业表
CREATE TABLE `assignments` (
`assignment_id` INT PRIMARY KEY AUTO_INCREMENT,
`course_id` INT NOT NULL,
`title` VARCHAR(100) NOT NULL,
`description` TEXT,
`deadline` DATETIME NOT NULL,
`max_score` INT,
`created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`) ON DELETE CASCADE,
INDEX `idx_deadline` (`deadline`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 作业提交表
CREATE TABLE `submissions` (
`submission_id` INT PRIMARY KEY AUTO_INCREMENT,
`assignment_id` INT NOT NULL,
`user_id` INT NOT NULL,
`file_path` VARCHAR(255),
`submit_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
`status` ENUM('未提交', '已提交', '已逾期') DEFAULT '未提交',
`score` INT,
`feedback` TEXT,
FOREIGN KEY (`assignment_id`) REFERENCES `assignments`(`assignment_id`) ON DELETE CASCADE,
FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
UNIQUE KEY `uniq_assignment_user` (`assignment_id`, `user_id`),
INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6. 公告表
CREATE TABLE `announcements` (
`announcement_id` INT PRIMARY KEY AUTO_INCREMENT,
`title` VARCHAR(100) NOT NULL,
`content` TEXT NOT NULL,
`publisher_id` INT NOT NULL,
`scope` ENUM('全校', '院系', '班级') NOT NULL,
`department` VARCHAR(50),
`is_urgent` BOOLEAN DEFAULT FALSE,
`publish_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`publisher_id`) REFERENCES `users`(`user_id`),
INDEX `idx_scope` (`scope`, `department`),
INDEX `idx_publish_time` (`publish_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7. 日程表
CREATE TABLE `schedules` (
`schedule_id` INT PRIMARY KEY AUTO_INCREMENT,
`user_id` INT NOT NULL,
`title` VARCHAR(100) NOT NULL,
`description` TEXT,
`start_time` DATETIME NOT NULL,
`end_time` DATETIME NOT NULL,
`type` ENUM('学习', '生活') DEFAULT '学习',
`remind_time` DATETIME,
`repeat_rule` VARCHAR(50),
FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`) ON DELETE CASCADE,
INDEX `idx_user_time` (`user_id`, `start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 8. 资料表
CREATE TABLE `materials` (
`material_id` INT PRIMARY KEY AUTO_INCREMENT,
`course_id` INT NOT NULL,
`uploader_id` INT NOT NULL,
`file_name` VARCHAR(100) NOT NULL,
`file_path` VARCHAR(255) NOT NULL,
`file_type` VARCHAR(20) NOT NULL,
`file_size` INT NOT NULL COMMENT '文件大小(KB)',
`description` TEXT,
`upload_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
`download_count` INT DEFAULT 0,
FOREIGN KEY (`course_id`) REFERENCES `courses`(`course_id`) ON DELETE CASCADE,
FOREIGN KEY (`uploader_id`) REFERENCES `users`(`user_id`),
INDEX `idx_course` (`course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9. 资料评论表（可选）
CREATE TABLE `material_comments` (
`comment_id` INT PRIMARY KEY AUTO_INCREMENT,
`material_id` INT NOT NULL,
`user_id` INT NOT NULL,
`content` TEXT NOT NULL,
`rating` TINYINT CHECK (`rating` BETWEEN 1 AND 5),
`comment_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (`material_id`) REFERENCES `materials`(`material_id`) ON DELETE CASCADE,
FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
INDEX `idx_material` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入示例数据 --------------------------------------------------

-- 用户数据
INSERT INTO `users` (`username`, `password`, `phone`, `email`, `student_id`, `department`, `role`) VALUES
('student1', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Zu', '13800138001', 'student1@school.edu', '20230001', '计算机学院', 'student'),
('student2', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Zu', '13800138002', 'student2@school.edu', '20230002', '计算机学院', 'student'),
('teacher1', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Zu', '13900139001', 'teacher1@school.edu', 'T1001', '计算机学院', 'teacher'),
('admin1', '$2a$10$xJwL5v5Jz5U5Z5U5Z5U5Zu', '13700137001', 'admin@school.edu', 'A1001', '教务处', 'admin');

-- 课程数据
INSERT INTO `courses` (`course_name`, `teacher_id`, `classroom`, `schedule`, `credit`, `type`, `description`) VALUES
('数据库系统', 3, 'A101', '周一 1-2节', 4, '必修', '学习关系型数据库原理与设计'),
('Web开发', 3, 'B205', '周三 3-4节', 3, '选修', '前端与后端开发技术'),
('数据结构', NULL, 'C302', '周五 5-6节', 4, '必修', '基本数据结构和算法');

-- 用户-课程关联
INSERT INTO `user_courses` (`user_id`, `course_id`, `semester`) VALUES
(1, 1, '2023-2024-1'),
(1, 2, '2023-2024-1'),
(2, 1, '2023-2024-1');

-- 作业数据
INSERT INTO `assignments` (`course_id`, `title`, `description`, `deadline`, `max_score`) VALUES
(1, '数据库设计作业', '设计一个校园管理系统的ER图', '2023-11-15 23:59:59', 100),
(1, 'SQL实践', '完成10个复杂SQL查询', '2023-11-30 23:59:59', 100),
(2, '个人主页开发', '使用HTML/CSS/JS开发个人主页', '2023-12-10 23:59:59', 100);

-- 作业提交数据
INSERT INTO `submissions` (`assignment_id`, `user_id`, `file_path`, `status`, `score`) VALUES
(1, 1, '/uploads/assignment1_student1.pdf', '已提交', 85),
(1, 2, '/uploads/assignment1_student2.docx', '已提交', 90);

-- 公告数据
INSERT INTO `announcements` (`title`, `content`, `publisher_id`, `scope`, `department`, `is_urgent`) VALUES
('期末考试安排', '12月20-30日为期末考试周', 4, '全校', NULL, TRUE),
('计算机学院讲座通知', '本周五下午2点有AI技术讲座', 3, '院系', '计算机学院', FALSE);

-- 日程数据
INSERT INTO `schedules` (`user_id`, `title`, `description`, `start_time`, `end_time`, `type`, `remind_time`) VALUES
(1, '小组会议', '数据库项目讨论', '2023-11-10 15:00:00', '2023-11-10 16:30:00', '学习', '2023-11-10 14:45:00'),
(1, '朋友聚会', '周末聚餐', '2023-11-12 18:00:00', '2023-11-12 20:00:00', '生活', NULL);

-- 资料数据
INSERT INTO `materials` (`course_id`, `uploader_id`, `file_name`, `file_path`, `file_type`, `file_size`, `description`) VALUES
(1, 3, '数据库课件1.pdf', '/materials/db_chapter1.pdf', 'pdf', 2500, '第一章：数据库概述'),
(1, 1, '学习笔记.docx', '/materials/db_notes.docx', 'docx', 500, '个人整理的数据库笔记');

-- 资料评论数据
INSERT INTO `material_comments` (`material_id`, `user_id`, `content`, `rating`) VALUES
(1, 1, '课件非常清晰，很有帮助！', 5),
(1, 2, '第二章的内容可以再详细些', 4);

-- 创建视图 --------------------------------------------------

-- 学生课程视图
CREATE VIEW student_course_view AS
SELECT u.username, c.course_name, uc.semester
FROM users u
JOIN user_courses uc ON u.user_id = uc.user_id
JOIN courses c ON uc.course_id = c.course_id
WHERE u.role = 'student';

-- 作业截止提醒视图
CREATE VIEW assignment_reminder_view AS
SELECT a.title, c.course_name, a.deadline,
DATEDIFF(a.deadline, NOW()) AS days_left
FROM assignments a
JOIN courses c ON a.course_id = c.course_id
WHERE a.deadline > NOW();

-- 结束 --------------------------------------------------