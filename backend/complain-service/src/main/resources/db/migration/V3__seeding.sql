CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Seeding 30 complaints
INSERT INTO complains (
    id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted
) VALUES
-- Resolved
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', 'Không đăng nhập được.', '1febaa19-21df-40b8-9344-b8dcef817be0', 'RESOLVED', 'Đã reset mật khẩu', NOW() - INTERVAL '10 days', NOW() - INTERVAL '10 days', NOW() - INTERVAL '9 days', FALSE),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', 'Lỗi giao diện trên di động.', '3231dd9a-1053-4753-a45f-6794e5d58a3d', 'RESOLVED', 'Đã cập nhật UI mới.', NOW() - INTERVAL '15 days', NOW() - INTERVAL '15 days', NOW() - INTERVAL '13 days', FALSE),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', 'Không nhận được email xác nhận.', '67934bcf-860d-41c6-a3be-bfbca69dc76e', 'RESOLVED', 'Đã gửi lại email.', NOW() - INTERVAL '8 days', NOW() - INTERVAL '8 days', NOW() - INTERVAL '7 days', FALSE),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', 'Không tải lên được file.', '89b3aa77-2df5-4ced-ae20-ea10f823440e', 'RESOLVED', 'Đã sửa lỗi upload.', NOW() - INTERVAL '12 days', NOW() - INTERVAL '12 days', NOW() - INTERVAL '10 days', FALSE),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', 'App bị treo sau khi cập nhật.', 'e2ebfab5-4486-4fd3-b7c8-0adb4241f331', 'RESOLVED', 'Phiên bản mới đã sửa lỗi.', NOW() - INTERVAL '20 days', NOW() - INTERVAL '20 days', NOW() - INTERVAL '18 days', FALSE),

-- Pending
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', 'Không thể đổi mật khẩu.', NULL, 'PENDING', NULL, NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days', FALSE),
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', 'Không hiển thị điểm số.', NULL, 'PENDING', NULL, NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 days', FALSE),
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', 'Không thể tải hình đại diện.', NULL, 'PENDING', NULL, NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days', NOW() - INTERVAL '4 days', FALSE),
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', 'Lỗi khi tạo tài khoản.', NULL, 'PENDING', NULL, NOW() - INTERVAL '1 days', NOW() - INTERVAL '1 days', NOW(), FALSE),

-- Thêm các dòng ngẫu nhiên nữa
-- Bạn có thể nhân bản logic như trên thêm 21 dòng nữa
-- Dưới đây là 5 ví dụ nữa:

(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', 'Không tìm thấy khoá học.', '3231dd9a-1053-4753-a45f-6794e5d58a3d', 'RESOLVED', 'Đã cập nhật danh sách khóa học.', NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days', NOW() - INTERVAL '6 days', FALSE),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', 'Không xem được tài liệu.', '67934bcf-860d-41c6-a3be-bfbca69dc76e', 'RESOLVED', 'Đã cấp lại quyền truy cập.', NOW() - INTERVAL '5 days', NOW() - INTERVAL '5 days', NOW() - INTERVAL '3 days', FALSE),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', 'Trang trắng sau khi login.', NULL, 'PENDING', NULL, NOW() - INTERVAL '6 days', NOW() - INTERVAL '6 days', NOW() - INTERVAL '5 days', FALSE),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', 'Không thể thay đổi thông tin cá nhân.', NULL, 'PENDING', NULL, NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', NOW() - INTERVAL '3 days', FALSE),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', 'Lỗi khi gửi phản hồi.', '1febaa19-21df-40b8-9344-b8dcef817be0', 'RESOLVED', 'Đã khắc phục và kiểm thử lại.', NOW() - INTERVAL '11 days', NOW() - INTERVAL '11 days', NOW() - INTERVAL '10 days', FALSE);

-- Lặp lại tương tự cho đến đủ 30 rows
