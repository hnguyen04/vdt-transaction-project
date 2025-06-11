INSERT INTO users (id, user_name, full_name, code, phone_number, cmnd, role, password, created_at, updated_at, is_deleted)
VALUES
    -- 5 USER
    ('3f2504e0-4f89-11d3-9a0c-0305e82c3301', 'user1', 'User One', '00000001', '0900000001', '123456789', 'USER', '123456', now(), now(), false),
    ('3f2504e1-4f89-11d3-9a0c-0305e82c3302', 'user2', 'User Two', '00000002', '0900000002', '123456780', 'USER', '123456', now(), now(), false),
    ('3f2504e2-4f89-11d3-9a0c-0305e82c3303', 'user3', 'User Three', '00000003', '0900000003', '123456781', 'USER', '123456', now(), now(), false),
    ('3f2504e3-4f89-11d3-9a0c-0305e82c3304', 'user4', 'User Four', '00000004', '0900000004', '123456782', 'USER', '123456', now(), now(), false),
    ('3f2504e4-4f89-11d3-9a0c-0305e82c3305', 'user5', 'User Five', '00000005', '0900000005', '123456783', 'USER', '123456', now(), now(), false),

    -- 5 STAFF
    ('3f2504e5-4f89-11d3-9a0c-0305e82c3306', 'staff1', 'Staff One', '00000006', '0900000006', '223456789', 'STAFF', '123456', now(), now(), false),
    ('3f2504e6-4f89-11d3-9a0c-0305e82c3307', 'staff2', 'Staff Two', '00000007', '0900000007', '223456780', 'STAFF', '123456', now(), now(), false),
    ('3f2504e7-4f89-11d3-9a0c-0305e82c3308', 'staff3', 'Staff Three', '00000008', '0900000008', '223456781', 'STAFF', '123456', now(), now(), false),
    ('3f2504e8-4f89-11d3-9a0c-0305e82c3309', 'staff4', 'Staff Four', '00000009', '0900000009', '223456782', 'STAFF', '123456', now(), now(), false),
    ('3f2504e9-4f89-11d3-9a0c-0305e82c3310', 'staff5', 'Staff Five', '00000010', '0900000010', '223456783', 'STAFF', '123456', now(), now(), false),

    -- 5 ADMIN
    ('3f2504ea-4f89-11d3-9a0c-0305e82c3311', 'admin1', 'Admin One', '00000011', '0900000011', '323456789', 'ADMIN', '123456', now(), now(), false),
    ('3f2504eb-4f89-11d3-9a0c-0305e82c3312', 'admin2', 'Admin Two', '00000012', '0900000012', '323456780', 'ADMIN', '123456', now(), now(), false),
    ('3f2504ec-4f89-11d3-9a0c-0305e82c3313', 'admin3', 'Admin Three', '00000013', '0900000013', '323456781', 'ADMIN', '123456', now(), now(), false),
    ('3f2504ed-4f89-11d3-9a0c-0305e82c3314', 'admin4', 'Admin Four', '00000014', '0900000014', '323456782', 'ADMIN', '123456', now(), now(), false),
    ('3f2504ee-4f89-11d3-9a0c-0305e82c3315', 'admin5', 'Admin Five', '00000015', '0900000015', '323456783', 'ADMIN', '123456', now(), now(), false);
