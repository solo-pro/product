INSERT INTO categories(name, description, created_time_stamps, updated_time_stamps)
VALUES
    ('전자제품', '전자제품 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000),
    ( '가전제품', '가전제품 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000),
    ( '의류', '의류 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000),
    ( '건강', '건강 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000),
    ( '운동', '운동 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000),
    ( '아웃도어', '아웃도어 카테고리', EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000);

-- Compare this snippet from src/main/resources/schema/init.sql:
INSERT INTO products(product_name, product_price, product_stock, product_main_image, product_description, product_category_id, product_version, product_created_time_stamps, product_updated_time_stamps)
VALUES ('추천 제품', 10000, 50, 'image_url', '추천 제품 설명', 1, 1, EXTRACT(EPOCH FROM NOW()) * 1000, EXTRACT(EPOCH FROM NOW()) * 1000);