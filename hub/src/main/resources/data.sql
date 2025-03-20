-- 중앙 허브 (Central Hubs) 추가
--
INSERT INTO p_hub (hub_id, name, hub_address, location, user_id, user_name, user_phone_number, parent_id, created_at, modified_at)
VALUES
    ('11111111-1111-1111-1111-111111111111', '서울 중앙 허브', '서울특별시 강남구', ST_GeomFromText('POINT(127.0276 37.4979)', 4326), 1001, '김철수', '010-1111-2222', NULL, NOW(), NOW()),
    ('22222222-2222-2222-2222-222222222222', '부산 중앙 허브', '부산광역시 해운대구', ST_GeomFromText('POINT(129.1580 35.1605)', 4326), 1002, '이영희', '010-2222-3333', NULL, NOW(), NOW()),
    ('33333333-3333-3333-3333-333333333333', '대구 중앙 허브', '대구광역시 수성구', ST_GeomFromText('POINT(128.6130 35.8574)', 4326), 1003, '박민수', '010-3333-4444', NULL, NOW(), NOW()),
    ('44444444-4444-4444-4444-444444444444', '인천 중앙 허브', '인천광역시 연수구', ST_GeomFromText('POINT(126.7052 37.3943)', 4326), 1004, '최지현', '010-4444-5555', NULL, NOW(), NOW()),
    ('55555555-5555-5555-5555-555555555555', '광주 중앙 허브', '광주광역시 서구', ST_GeomFromText('POINT(126.8514 35.1603)', 4326), 1005, '강도현', '010-5555-6666', NULL, NOW(), NOW()),
    ('66666666-6666-6666-6666-666666666666', '대전 중앙 허브', '대전광역시 유성구', ST_GeomFromText('POINT(127.3848 36.3505)', 4326), 1006, '송혜진', '010-6666-7777', NULL, NOW(), NOW()),
    ('77777777-7777-7777-7777-777777777777', '울산 중앙 허브', '울산광역시 남구', ST_GeomFromText('POINT(129.3114 35.5384)', 4326), 1007, '정우성', '010-7777-8888', NULL, NOW(), NOW()),
    ('88888888-8888-8888-8888-888888888888', '세종 중앙 허브', '세종특별자치시', ST_GeomFromText('POINT(127.2902 36.4801)', 4326), 1008, '한지민', '010-8888-9999', NULL, NOW(), NOW()),
    ('99999999-9999-9999-9999-999999999999', '제주 중앙 허브', '제주특별자치도 제주시', ST_GeomFromText('POINT(126.5312 33.4996)', 4326), 1009, '배수진', '010-9999-0000', NULL, NOW(), NOW()),
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '춘천 중앙 허브', '강원도 춘천시', ST_GeomFromText('POINT(127.7235 37.8806)', 4326), 1010, '남궁민', '010-0000-1111', NULL, NOW(), NOW())
    ON CONFLICT (hub_id) DO NOTHING;
-- # --
-- 스포크 허브 (Spoke Hubs) 추가 (UUID 수정)
INSERT INTO p_hub (hub_id, name, hub_address, location, user_id, user_name, user_phone_number, parent_id, created_at, modified_at)
VALUES
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '경기남부 허브', '경기도 수원시', ST_GeomFromText('POINT(127.0286 37.2636)', 4326), 1011, '김민재', '010-1111-3333', '11111111-1111-1111-1111-111111111111', NOW(), NOW()),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '경기북부 허브', '경기도 의정부시', ST_GeomFromText('POINT(127.0342 37.7381)', 4326), 1012, '이수현', '010-2222-4444', '11111111-1111-1111-1111-111111111111', NOW(), NOW()),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', '충청남도 허브', '충청남도 천안시', ST_GeomFromText('POINT(127.1190 36.8151)', 4326), 1013, '박지훈', '010-3333-5555', '66666666-6666-6666-6666-666666666666', NOW(), NOW()),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '충청북도 허브', '충청북도 청주시', ST_GeomFromText('POINT(127.4890 36.6357)', 4326), 1014, '최은서', '010-4444-6666', '66666666-6666-6666-6666-666666666666', NOW(), NOW()),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', '전라북도 허브', '전라북도 전주시', ST_GeomFromText('POINT(127.1500 35.8242)', 4326), 1015, '정하영', '010-5555-7777', '55555555-5555-5555-5555-555555555555', NOW(), NOW()),
    ('99999999-9999-9999-9999-999999999998', '전라남도 허브', '전라남도 목포시', ST_GeomFromText('POINT(126.3914 34.8118)', 4326), 1016, '장성호', '010-6666-8888', '55555555-5555-5555-5555-555555555555', NOW(), NOW()),
    ('99999999-9999-9999-9999-999999999997', '경상북도 허브', '경상북도 포항시', ST_GeomFromText('POINT(129.3634 36.0190)', 4326), 1017, '한채영', '010-7777-9999', '33333333-3333-3333-3333-333333333333', NOW(), NOW()),
    ('99999999-9999-9999-9999-999999999996', '경상남도 허브', '경상남도 창원시', ST_GeomFromText('POINT(128.6811 35.2278)', 4326), 1018, '노지훈', '010-8888-0000', '33333333-3333-3333-3333-333333333333', NOW(), NOW())
    ON CONFLICT (hub_id) DO NOTHING;

CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- generate_v4 사용 가능
INSERT INTO p_hub_product (hub_product_id, hub_id, producer_id, producer_name, product_id, product_name, product_type, product_stock, created_at, modified_at)

VALUES
    -- 서울 중앙 허브 상품
    (uuid_generate_v4(), '11111111-1111-1111-1111-111111111111', uuid_generate_v4(), '삼성전자', uuid_generate_v4(), '갤럭시 S24', 'NORMAL', 100, NOW(), NOW()),
    (uuid_generate_v4(), '11111111-1111-1111-1111-111111111111', uuid_generate_v4(), 'LG전자', uuid_generate_v4(), 'LG 냉장고', 'NORMAL', 50, NOW(), NOW()),

    -- 부산 중앙 허브 상품
    (uuid_generate_v4(), '22222222-2222-2222-2222-222222222222', uuid_generate_v4(), '농심', uuid_generate_v4(), '신라면', 'NORMAL', 500, NOW(), NOW()),
    (uuid_generate_v4(), '22222222-2222-2222-2222-222222222222', uuid_generate_v4(), '오뚜기', uuid_generate_v4(), '3분 카레', 'NORMAL', 300, NOW(), NOW()),

    -- 대구 중앙 허브 상품
    (uuid_generate_v4(), '33333333-3333-3333-3333-333333333333', uuid_generate_v4(), '한샘', uuid_generate_v4(), '책상 세트', 'NORMAL', 80, NOW(), NOW()),

    -- 광주 중앙 허브 상품
    (uuid_generate_v4(), '55555555-5555-5555-5555-555555555555', uuid_generate_v4(), '코웨이', uuid_generate_v4(), '정수기', 'NORMAL', 120, NOW(), NOW()),

    -- 대전 중앙 허브 상품
    (uuid_generate_v4(), '66666666-6666-6666-6666-666666666666', uuid_generate_v4(), 'CJ제일제당', uuid_generate_v4(), '햇반', 'FRESH', 700, NOW(), NOW()),

    -- 스포크 허브 상품 (경기남부 허브)
    (uuid_generate_v4(), 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', uuid_generate_v4(), '네스프레소', uuid_generate_v4(), '커피머신', 'NORMAL', 30, NOW(), NOW()),

    -- 스포크 허브 상품 (충청남도 허브)
    (uuid_generate_v4(), 'dddddddd-dddd-dddd-dddd-dddddddddddd', uuid_generate_v4(), '매일유업', uuid_generate_v4(), '우유', 'NORMAL', 400, NOW(), NOW()),

    -- 스포크 허브 상품 (전라북도 허브)
    (uuid_generate_v4(), 'ffffffff-ffff-ffff-ffff-ffffffffffff', uuid_generate_v4(), '이케아', uuid_generate_v4(), '책장', 'NORMAL', 50, NOW(), NOW()),

    -- 스포크 허브 상품 (경상남도 허브)
    (uuid_generate_v4(), '99999999-9999-9999-9999-999999999996', uuid_generate_v4(), '쿠쿠', uuid_generate_v4(), '전기밥솥', 'NORMAL', 60, NOW(), NOW())
ON CONFLICT (hub_product_id) DO NOTHING;
