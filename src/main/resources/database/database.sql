CREATE DATABASE shopapp;

USE shopapp;
--custimer want to buy => must register => create users table    
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) DEFAULT '',    
    password VARCHAR(100) NOT NULL DEFAULT '',   
    created_at DATETIME, 
    updated_at DATETIME, 
    is_active tinyint DEFAULT 1, 
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0, 
    google_account_id INT DEFAULT 0
); 

ALTER TABLE users ADD COLUMN role_id INT;

CREATE TABLE roles(
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
);

ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id); 

CREATE TABLE tokens(
    id int PRIMARY KEY AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL, 
    expiration_date DATETIME, 
    revoked tinyint(1) NOT NULL, 
    expired tinyint(1) NOT NULL, 
    user_id int, -- foreign key   
    FOREIGN KEY (user_id) REFERENCES users(id)  
)

-- support sign in with facebook and google
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'Name of provider social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email of user',
    name VARCHAR(100) NOT NULL COMMENT 'Name of user',    
    user_id INT, 
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- category 
CREATE TABLE categories(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Name of category',
);

-- product 
CREATE TABLE products(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) NOT NULL DEFAULT '' COMMENT 'Name of product', 
    price FLOAT NOT NULL CHECK(price >= 0), 
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '', 
    created_at DATETIME, 
    updated_at DATETIME, 
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_product_images_product_id
        FOREIGN KEY (product_id)
        REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);

-- order
CREATE TABLE orders(
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT, 
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(150) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '', 
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT CHECK(total_money >= 0)
);

ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders ADD COLUMN shipping_date DATE; 
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100); 

-- delete 1 order => soft delete 
ALTER TABLE orders ADD COLUMN active TINYINT(1);
-- order status in enum
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled') 
COMMENT 'Status of order';

-- order detail
CREATE TABLE order_details(
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT, 
    FOREIGN KEY (order_id) REFERENCES orders(id),   
    product_id INT, 
    FOREIGN KEY (product_id) REFERENCES products(id),
    price FLOAT CHECK(price >= 0), 
    number_of_products INT CHECK(number_of_products > 0), 
    total_money FLOAT CHECK(total_money >= 0),
    color VARCHAR(20) DEFAULT '' 
);