INSERT INTO users (id, user_name, full_name, code, phone_number, cmnd, role, password, created_at, updated_at, is_deleted)
VALUES
    -- 5 USER
    (gen_random_uuid(), 'user1', 'User One', '00000001', '0900000001', '123456789', 'USER', '123456', now(), now(), false),
    (gen_random_uuid(), 'user2', 'User Two', '00000002', '0900000002', '123456780', 'USER', '123456', now(), now(), false),
    (gen_random_uuid(), 'user3', 'User Three', '00000003', '0900000003', '123456781', 'USER', '123456', now(), now(), false),
    (gen_random_uuid(), 'user4', 'User Four', '00000004', '0900000004', '123456782', 'USER', '123456', now(), now(), false),
    (gen_random_uuid(), 'user5', 'User Five', '00000005', '0900000005', '123456783', 'USER', '123456', now(), now(), false),

    -- 5 STAFF
    (gen_random_uuid(), 'staff1', 'Staff One', '00000006', '0900000006', '223456789', 'STAFF', '123456', now(), now(), false),
    (gen_random_uuid(), 'staff2', 'Staff Two', '00000007', '0900000007', '223456780', 'STAFF', '123456', now(), now(), false),
    (gen_random_uuid(), 'staff3', 'Staff Three', '00000008', '0900000008', '223456781', 'STAFF', '123456', now(), now(), false),
    (gen_random_uuid(), 'staff4', 'Staff Four', '00000009', '0900000009', '223456782', 'STAFF', '123456', now(), now(), false),
    (gen_random_uuid(), 'staff5', 'Staff Five', '00000010', '0900000010', '223456783', 'STAFF', '123456', now(), now(), false),

    -- 5 ADMIN
    (gen_random_uuid(), 'admin1', 'Admin One', '00000011', '0900000011', '323456789', 'ADMIN', '123456', now(), now(), false),
    (gen_random_uuid(), 'admin2', 'Admin Two', '00000012', '0900000012', '323456780', 'ADMIN', '123456', now(), now(), false),
    (gen_random_uuid(), 'admin3', 'Admin Three', '00000013', '0900000013', '323456781', 'ADMIN', '123456', now(), now(), false),
    (gen_random_uuid(), 'admin4', 'Admin Four', '00000014', '0900000014', '323456782', 'ADMIN', '123456', now(), now(), false),
    (gen_random_uuid(), 'admin5', 'Admin Five', '00000015', '0900000015', '323456783', 'ADMIN', '123456', now(), now(), false);
