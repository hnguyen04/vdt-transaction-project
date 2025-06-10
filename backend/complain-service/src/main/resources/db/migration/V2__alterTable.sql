CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- V2__seeding.sql
-- Seed dữ liệu complains: mỗi user 5 bản ghi, tuân thủ quy tắc resolver_id

-- User 1: 0933acdc-d3d8-4e94-915c-08ff85112388
INSERT INTO complains (id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted) VALUES
    (gen_random_uuid(), '0933acdc-d3d8-4e94-915c-08ff85112388', 'Giao dịch bị trừ tiền nhưng không nhận được hàng', NULL, 'PENDING', NULL, '2023-05-10 14:30:00', NOW(), NULL, false),
    (gen_random_uuid(), '0933acdc-d3d8-4e94-915c-08ff85112388', 'Ứng dụng bị treo khi thanh toán', '14702ab1-2e73-4819-90cc-d683127b875a', 'PENDING', NULL, '2023-05-11 09:15:00', NOW(), NULL, false),
    (gen_random_uuid(), '0933acdc-d3d8-4e94-915c-08ff85112388', 'Nhầm số tài khoản khi chuyển tiền', '9efc747c-068b-4229-9a29-618abef718cd', 'RESOLVED', 'Đã hoàn tiền cho khách hàng', '2023-05-12 16:45:00', NOW(), NOW(), false),
    (gen_random_uuid(), '0933acdc-d3d8-4e94-915c-08ff85112388', 'Phí giao dịch không rõ ràng', 'd754ab5d-60f1-4ada-b364-dd6f2d33270a', 'RESOLVED', 'Giải thích phí và bồi hoàn 50%', '2023-05-13 11:20:00', NOW(), NOW(), false),
    (gen_random_uuid(), '0933acdc-d3d8-4e94-915c-08ff85112388', 'Yêu cầu hỗ trợ giao dịch lỗi', 'e3c7e79a-7130-4822-8215-381a16ac489b', 'REJECTED', 'Giao dịch đã hoàn thành đúng quy trình', '2023-05-14 10:00:00', NOW(), NOW(), false);

-- User 2: 1b5ef43c-4df1-4a2b-81af-b09fbc6146ad
INSERT INTO complains (id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted) VALUES
    (gen_random_uuid(), '1b5ef43c-4df1-4a2b-81af-b09fbc6146ad', 'Không nhận được OTP xác nhận', NULL, 'PENDING', NULL, '2023-05-15 08:30:00', NOW(), NULL, false),
    (gen_random_uuid(), '1b5ef43c-4df1-4a2b-81af-b09fbc6146ad', 'Thông báo lỗi không rõ ràng', 'f9953e7e-705b-4928-b3c2-9ebb3d9e0e93', 'PENDING', NULL, '2023-05-16 13:45:00', NOW(), NULL, false),
    (gen_random_uuid(), '1b5ef43c-4df1-4a2b-81af-b09fbc6146ad', 'Giao dịch bị trùng lặp', '14702ab1-2e73-4819-90cc-d683127b875a', 'RESOLVED', 'Đã hoàn tiền giao dịch trùng', '2023-05-17 17:20:00', NOW(), NOW(), false),
    (gen_random_uuid(), '1b5ef43c-4df1-4a2b-81af-b09fbc6146ad', 'Chuyển tiền nhầm người nhận', '9efc747c-068b-4229-9a29-618abef718cd', 'RESOLVED', 'Đã liên hệ người nhận hoàn tiền', '2023-05-18 10:15:00', NOW(), NOW(), false),
    (gen_random_uuid(), '1b5ef43c-4df1-4a2b-81af-b09fbc6146ad', 'Thẻ bị khóa không rõ lý do', NULL, 'PENDING', NULL, '2023-05-19 14:50:00', NOW(), NULL, false);

-- User 3: 6879f071-9910-4b6a-8534-f4facd9454e2
INSERT INTO complains (id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted) VALUES
    (gen_random_uuid(), '6879f071-9910-4b6a-8534-f4facd9454e2', 'Giao dịch thất bại nhưng bị trừ tiền', 'd754ab5d-60f1-4ada-b364-dd6f2d33270a', 'RESOLVED', 'Đã hoàn tiền và bồi thường', '2023-05-20 09:30:00', NOW(), NOW(), false),
    (gen_random_uuid(), '6879f071-9910-4b6a-8534-f4facd9454e2', 'Yêu cầu xuất hóa đơn điện tử', NULL, 'PENDING', NULL, '2023-05-21 11:25:00', NOW(), NULL, false),
    (gen_random_uuid(), '6879f071-9910-4b6a-8534-f4facd9454e2', 'Thông tin giao dịch hiển thị sai', 'e3c7e79a-7130-4822-8215-381a16ac489b', 'RESOLVED', 'Đã cập nhật lại thông tin', '2023-05-22 16:40:00', NOW(), NOW(), false),
    (gen_random_uuid(), '6879f071-9910-4b6a-8534-f4facd9454e2', 'Phản ánh nhân viên hỗ trợ', 'f9953e7e-705b-4928-b3c2-9ebb3d9e0e93', 'PENDING', NULL, '2023-05-23 14:15:00', NOW(), NULL, false),
    (gen_random_uuid(), '6879f071-9910-4b6a-8534-f4facd9454e2', 'Lỗi xác thực sinh trắc học', '14702ab1-2e73-4819-90cc-d683127b875a', 'RESOLVED', 'Đã hướng dẫn cài đặt lại', '2023-05-24 10:05:00', NOW(), NOW(), false);

-- User 4: 709ce9a8-0fb4-452a-944f-0ebc5041b38c
INSERT INTO complains (id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted) VALUES
    (gen_random_uuid(), '709ce9a8-0fb4-452a-944f-0ebc5041b38c', 'Yêu cầu xóa dữ liệu cá nhân', NULL, 'PENDING', NULL, '2023-05-25 08:50:00', NOW(), NULL, false),
    (gen_random_uuid(), '709ce9a8-0fb4-452a-944f-0ebc5041b38c', 'Tài khoản bị đăng nhập bất thường', '9efc747c-068b-4229-9a29-618abef718cd', 'RESOLVED', 'Đã reset mật khẩu và bảo mật', '2023-05-26 13:30:00', NOW(), NOW(), false),
    (gen_random_uuid(), '709ce9a8-0fb4-452a-944f-0ebc5041b38c', 'Thắc mắc về phí dịch vụ', NULL, 'PENDING', NULL, '2023-05-27 15:45:00', NOW(), NULL, false),
    (gen_random_uuid(), '709ce9a8-0fb4-452a-944f-0ebc5041b38c', 'Giao dịch quốc tế bị từ chối', 'd754ab5d-60f1-4ada-b364-dd6f2d33270a', 'RESOLVED', 'Đã hướng dẫn thủ tục cần thiết', '2023-05-28 11:20:00', NOW(), NOW(), false),
    (gen_random_uuid(), '709ce9a8-0fb4-452a-944f-0ebc5041b38c', 'Yêu cầu nâng hạn mức giao dịch', 'e3c7e79a-7130-4822-8215-381a16ac489b', 'REJECTED', 'Chưa đủ điều kiện theo quy định', '2023-05-29 09:10:00', NOW(), NOW(), false);

-- User 5: c123ba86-07ff-4b74-85a2-46a8a3543d59
INSERT INTO complains (id, user_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted) VALUES
    (gen_random_uuid(), 'c123ba86-07ff-4b74-85a2-46a8a3543d59', 'Lỗi hiển thị số dư tài khoản', 'f9953e7e-705b-4928-b3c2-9ebb3d9e0e93', 'RESOLVED', 'Đã khắc phục lỗi hiển thị', '2023-05-30 14:35:00', NOW(), NOW(), false),
    (gen_random_uuid(), 'c123ba86-07ff-4b74-85a2-46a8a3543d59', 'Thẻ bị nuốt tại ATM', '14702ab1-2e73-4819-90cc-d683127b875a', 'RESOLVED', 'Đã thu hồi và trả thẻ mới', '2023-05-31 16:50:00', NOW(), NOW(), false),
    (gen_random_uuid(), 'c123ba86-07ff-4b74-85a2-46a8a3543d59', 'Không nhận được thông báo giao dịch', NULL, 'PENDING', NULL, '2023-06-01 10:25:00', NOW(), NULL, false),
    (gen_random_uuid(), 'c123ba86-07ff-4b74-85a2-46a8a3543d59', 'Yêu cầu đổi số điện thoại nhận OTP', '9efc747c-068b-4229-9a29-618abef718cd', 'RESOLVED', 'Đã cập nhật số điện thoại mới', '2023-06-02 13:40:00', NOW(), NOW(), false),
    (gen_random_uuid(), 'c123ba86-07ff-4b74-85a2-46a8a3543d59', 'Khiếu nại dịch vụ khách hàng', NULL, 'PENDING', NULL, '2023-06-03 15:15:00', NOW(), NULL, true);