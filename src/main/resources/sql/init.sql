-- 초기 카테고리 데이터 삽입
INSERT INTO CATEGORIES (CATEGORY_NAME, CATEGORY_DESCRIPTION)
VALUES
    ('전자제품', '전자제품 카테고리'),
    ('가전제품', '가전제품 카테고리'),
    ('의류', '의류 카테고리'),
    ('건강', '건강 카테고리'),
    ('운동', '운동 카테고리'),
    ('아웃도어', '아웃도어 카테고리')
ON CONFLICT (CATEGORY_NAME) DO NOTHING;

INSERT INTO PRODUCTS(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_STOCK, PRODUCT_MAIN_IMAGE, PRODUCT_DESCRIPTION, PRODUCT_CATEGORY_ID, PRODUCT_VERSION)
VALUES ('추천 제품', 10000, 50, 'image_url', '추천 제품 설명', 1, 1);
