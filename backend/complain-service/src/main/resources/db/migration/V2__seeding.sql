INSERT INTO complains (
    id, transaction_id, content, resolver_id, status, resolving_note, time_submit, created_at, updated_at, is_deleted
) VALUES

-- Transaction: 1111...0003
('a1f0c7b3-35d4-4f30-9c9a-bcfe5e3f1001', '11111111-1111-1111-1111-000000000003', 'Giao dịch bị lỗi mạng.', '3f2504e5-4f89-11d3-9a0c-0305e82c3306', 'RESOLVED', 'Đã kiểm tra, hoàn tiền cho người dùng.', NOW(), NOW(), NOW(), FALSE),
('a1f0c7b3-35d4-4f30-9c9a-bcfe5e3f1002', '11111111-1111-1111-1111-000000000003', 'Không nhận được thông báo xác nhận.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),

-- Transaction: 1111...0007
('b2f1d1a4-ae1b-43c2-840e-c5ae6ecb2001', '11111111-1111-1111-1111-000000000007', 'Số dư bị trừ nhưng không nhận tiền.', '3f2504e6-4f89-11d3-9a0c-0305e82c3307', 'RESOLVED', 'Đã đối soát, chuyển lại tiền.', NOW(), NOW(), NOW(), FALSE),

-- Transaction: 2222...0004
('c3a5e2c5-fde3-4b56-a389-2d8e4e6c3001', '22222222-2222-2222-2222-000000000004', 'Không rõ lý do từ chối giao dịch.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),
('c3a5e2c5-fde3-4b56-a389-2d8e4e6c3002', '22222222-2222-2222-2222-000000000004', 'Giao dịch treo quá lâu.', '3f2504e7-4f89-11d3-9a0c-0305e82c3308', 'RESOLVED', 'Sự cố hệ thống, đã xử lý.', NOW(), NOW(), NOW(), FALSE),

-- Transaction: 2222...0007
('d4b9f7e1-6e87-4956-8b0a-49dff3b74001', '22222222-2222-2222-2222-000000000007', 'Giao dịch bị từ chối không lý do.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),

-- Transaction: 3333...0003
('e5cd4f4a-50e1-45f6-bd9b-59ae8b9c9001', '33333333-3333-3333-3333-000000000003', 'Thanh toán thất bại khi quét QR.', '3f2504e8-4f89-11d3-9a0c-0305e82c3309', 'RESOLVED', 'Lỗi đối tác, đã hoàn tiền.', NOW(), NOW(), NOW(), FALSE),

-- Transaction: 3333...0007
('f6dace88-b8e4-4b8d-b084-14672f2f7001', '33333333-3333-3333-3333-000000000007', 'Không thể xác minh thông tin giao dịch.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),
('f6dace88-b8e4-4b8d-b084-14672f2f7002', '33333333-3333-3333-3333-000000000007', 'Hệ thống báo lỗi không rõ nguyên nhân.', '3f2504e9-4f89-11d3-9a0c-0305e82c3310', 'RESOLVED', 'Đã kiểm tra log, xử lý xong.', NOW(), NOW(), NOW(), FALSE),

-- Transaction: 4444...0003
('a7f0b112-cd2d-4f00-b2e9-7123e2df0001', '44444444-4444-4444-4444-000000000003', 'Thao tác chuyển tiền bị lỗi timeout.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),

-- Transaction: 4444...0007
('b8e5c223-d431-49e6-91c0-73a9acdf0001', '44444444-4444-4444-4444-000000000007', 'Không thấy giao dịch trong lịch sử.', '3f2504e5-4f89-11d3-9a0c-0305e82c3306', 'RESOLVED', 'Đã đồng bộ dữ liệu và hiển thị lại.', NOW(), NOW(), NOW(), FALSE),

-- Transaction: 5555...0003
('c9f6d334-bad1-42db-a3f2-d7a3f6df0001', '55555555-5555-5555-5555-000000000003', 'Tài khoản bị trừ phí nhiều lần.', NULL, 'PENDING', NULL, NOW(), NOW(), NOW(), FALSE),

-- Transaction: 5555...0007
('da07e445-16fa-49b4-82be-cf8c76ef0001', '55555555-5555-5555-5555-000000000007', 'Không nhận được OTP khi xác nhận giao dịch.', '3f2504e6-4f89-11d3-9a0c-0305e82c3307', 'RESOLVED', 'Xác minh lỗi OTP, hỗ trợ người dùng.', NOW(), NOW(), NOW(), FALSE);
