CREATE EXTENSION IF NOT EXISTS "pgcrypto";

INSERT INTO transactions (id, user_id, code, commit_time, amount, status, type, source_account, destination_account, bank_code, created_at, updated_at, is_deleted)
VALUES

-- User 1
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', '10000001', NOW(), 150000, 'PENDING', 'DEPOSIT', NULL, 'VN01-11111111', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', '10000002', NOW(), 250000, 'SUCCESS', 'WITHDRAW', 'VN01-22222222', NULL, 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', '10000003', NOW(), 500000, 'FAILED', 'TRANSFER', 'VN01-33333333', 'VN01-44444444', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', '10000004', NOW(), 100000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-55555555', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), '30a273d2-0c22-40c3-a005-f4844da6758f', '10000005', NOW(), 75000, 'PENDING', 'WITHDRAW', 'VN01-66666666', NULL, 'VCB', NOW(), NOW(), false),

-- User 2
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', '10000006', NOW(), 200000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-77777777', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', '10000007', NOW(), 120000, 'FAILED', 'WITHDRAW', 'VN01-88888888', NULL, 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', '10000008', NOW(), 180000, 'PENDING', 'TRANSFER', 'VN01-99999999', 'VN01-00000000', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', '10000009', NOW(), 60000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-12121212', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), '7a262f72-9320-4d35-aa4c-83544f31fe0d', '10000010', NOW(), 85000, 'FAILED', 'WITHDRAW', 'VN01-34343434', NULL, 'BIDV', NOW(), NOW(), false),

-- User 3
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', '10000011', NOW(), 70000, 'PENDING', 'DEPOSIT', NULL, 'VN01-56565656', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', '10000012', NOW(), 300000, 'SUCCESS', 'WITHDRAW', 'VN01-78787878', NULL, 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', '10000013', NOW(), 90000, 'FAILED', 'TRANSFER', 'VN01-90909090', 'VN01-12121213', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', '10000014', NOW(), 110000, 'PENDING', 'DEPOSIT', NULL, 'VN01-13131313', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'b5fb93cd-5ff0-4dcc-8d0d-09eb58c79cba', '10000015', NOW(), 51000, 'SUCCESS', 'WITHDRAW', 'VN01-14141414', NULL, 'ACB', NOW(), NOW(), false),

-- User 4
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', '10000016', NOW(), 140000, 'FAILED', 'DEPOSIT', NULL, 'VN01-15151515', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', '10000017', NOW(), 95000, 'PENDING', 'WITHDRAW', 'VN01-16161616', NULL, 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', '10000018', NOW(), 120000, 'SUCCESS', 'TRANSFER', 'VN01-17171717', 'VN01-18181818', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', '10000019', NOW(), 180000, 'FAILED', 'DEPOSIT', NULL, 'VN01-19191919', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'cbb1f52d-633f-4a6f-8ef4-9a0c8bec8ec7', '10000020', NOW(), 53000, 'PENDING', 'WITHDRAW', 'VN01-20202020', NULL, 'TCB', NOW(), NOW(), false),

-- User 5
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', '10000021', NOW(), 51000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-21212121', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', '10000022', NOW(), 230000, 'FAILED', 'WITHDRAW', 'VN01-22222223', NULL, 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', '10000023', NOW(), 360000, 'PENDING', 'TRANSFER', 'VN01-24242424', 'VN01-25252525', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', '10000024', NOW(), 57000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-26262626', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'd2756dfe-68a1-45eb-b4c0-ddf4de163cd0', '10000025', NOW(), 80000, 'FAILED', 'WITHDRAW', 'VN01-27272727', NULL, 'VCB', NOW(), NOW(), false),

-- User 6
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', '10000026', NOW(), 67000, 'PENDING', 'DEPOSIT', NULL, 'VN01-28282828', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', '10000027', NOW(), 120000, 'SUCCESS', 'WITHDRAW', 'VN01-29292929', NULL, 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', '10000028', NOW(), 54000, 'FAILED', 'TRANSFER', 'VN01-30303030', 'VN01-31313131', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', '10000029', NOW(), 88000, 'PENDING', 'DEPOSIT', NULL, 'VN01-32323232', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'd38edd0b-59dc-430c-a6b6-7ce5e8386583', '10000030', NOW(), 100000, 'SUCCESS', 'WITHDRAW', 'VN01-33333333', NULL, 'ACB', NOW(), NOW(), false),

-- User 7
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', '10000031', NOW(), 125000, 'FAILED', 'DEPOSIT', NULL, 'VN01-34343435', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', '10000032', NOW(), 78000, 'PENDING', 'WITHDRAW', 'VN01-35353535', NULL, 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', '10000033', NOW(), 200000, 'SUCCESS', 'TRANSFER', 'VN01-36363636', 'VN01-37373737', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', '10000034', NOW(), 300000, 'FAILED', 'DEPOSIT', NULL, 'VN01-38383838', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'de4b2f3c-eb2d-42a7-82cf-d37380e164b1', '10000035', NOW(), 65000, 'PENDING', 'WITHDRAW', 'VN01-39393939', NULL, 'TCB', NOW(), NOW(), false),

-- User 8
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', '10000036', NOW(), 90000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-40404040', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', '10000037', NOW(), 60000, 'FAILED', 'WITHDRAW', 'VN01-41414141', NULL, 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', '10000038', NOW(), 200000, 'PENDING', 'TRANSFER', 'VN01-42424242', 'VN01-43434343', 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', '10000039', NOW(), 100000, 'SUCCESS', 'DEPOSIT', NULL, 'VN01-44444444', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'e18fded3-3dd8-40a9-9ad2-9b253ff408e6', '10000040', NOW(), 75000, 'FAILED', 'WITHDRAW', 'VN01-45454545', NULL, 'VCB', NOW(), NOW(), false),

-- User 9
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', '10000041', NOW(), 85000, 'PENDING', 'DEPOSIT', NULL, 'VN01-46464646', 'BIDV', NOW(), NOW(), false),
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', '10000042', NOW(), 400000, 'SUCCESS', 'WITHDRAW', 'VN01-47474747', NULL, 'ACB', NOW(), NOW(), false),
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', '10000043', NOW(), 120000, 'FAILED', 'TRANSFER', 'VN01-48484848', 'VN01-49494949', 'TCB', NOW(), NOW(), false),
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', '10000044', NOW(), 50000, 'PENDING', 'DEPOSIT', NULL, 'VN01-50505050', 'VCB', NOW(), NOW(), false),
(gen_random_uuid(), 'fca1ca86-2b89-4619-9a0a-7ab2ebbb3439', '10000045', NOW(), 100000, 'SUCCESS', 'WITHDRAW', 'VN01-51515151', NULL, 'BIDV', NOW(), NOW(), false);
