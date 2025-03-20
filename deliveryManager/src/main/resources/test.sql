INSERT INTO p_consumer_delivery_manager (hub_delivery_manager_id, user_id, user_name, hub_id, user_phone_number, assign_number, platform_type, channel_id, is_avaliable, created_at, modified_at)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 2001, '김민수', '11111111-1111-1111-1111-111111111111', '010-1234-5678', 1, 'SLACK', 30001, TRUE, NOW(), NOW()),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 2002, '박지영', '22222222-2222-2222-2222-222222222222', '010-5678-1234', 2, 'SLACK', 30002, TRUE, NOW(), NOW())
ON CONFLICT (hub_delivery_manager_id) DO NOTHING;


INSERT INTO p_hub_delivery_manager (hub_delivery_manager_id, user_id, user_name, user_phone_number, assign_number, platform_type, channel_id, is_avaliable, created_at, modified_at)
VALUES
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 3001, '최은서', '010-9876-5432', 1, 'SLACK', 40001, TRUE, NOW(), NOW()),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 3002, '이정훈', '010-8765-4321', 2, 'SLACK', 40002, TRUE, NOW(), NOW())
ON CONFLICT (hub_delivery_manager_id) DO NOTHING;