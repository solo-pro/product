-- Category 테이블 생성
CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    created_time_stamps BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    updated_time_stamps BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000
);

-- Category 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);
CREATE INDEX IF NOT EXISTS idx_categories_created_time ON categories(created_time_stamps);
CREATE INDEX IF NOT EXISTS idx_categories_updated_time ON categories(updated_time_stamps);

-- Product 테이블 생성
CREATE TABLE IF NOT EXISTS products (
    product_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_price BIGINT NOT NULL,
    product_stock BIGINT NOT NULL,
    product_main_image TEXT DEFAULT '',
    product_description TEXT DEFAULT '',
    product_category_id BIGINT NOT NULL,
    product_version BIGINT DEFAULT 1,
    product_deleted BOOLEAN DEFAULT FALSE,
    product_created_time_stamps BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    product_updated_time_stamps BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    FOREIGN KEY (product_category_id) REFERENCES categories(category_id)
);

-- Product 테이블 인덱스
CREATE INDEX IF NOT EXISTS idx_products_name ON products(product_name);
CREATE INDEX IF NOT EXISTS idx_products_price ON products(product_price);
CREATE INDEX IF NOT EXISTS idx_products_stock ON products(product_stock);
CREATE INDEX IF NOT EXISTS idx_products_category_id ON products(product_category_id);
CREATE INDEX IF NOT EXISTS idx_products_created_time ON products(product_created_time_stamps);
CREATE INDEX IF NOT EXISTS idx_products_updated_time ON products(product_updated_time_stamps);
CREATE INDEX IF NOT EXISTS idx_products_deleted ON products(product_deleted);
