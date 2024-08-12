CREATE TABLE categories (
    category_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_time_stamps BIGINT,
    updated_time_stamps BIGINT
);

CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_price BIGINT NOT NULL,
    product_stock BIGINT NOT NULL,
    product_main_image TEXT,
    product_description TEXT,
    product_category_id BIGINT NOT NULL,
    product_version BIGINT,
    product_deleted BOOLEAN DEFAULT FALSE,
    product_created_time_stamps BIGINT,
    product_updated_time_stamps BIGINT,
    FOREIGN KEY (product_category_id) REFERENCES categories(category_id)
);
