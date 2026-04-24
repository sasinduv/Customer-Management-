CREATE TABLE IF NOT EXISTS country (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS city (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    country_id BIGINT,
    FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    date_of_birth DATE,
    nic VARCHAR(20) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS customer_mobile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT,
    mobile_number VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE IF NOT EXISTS customer_address (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    customer_id BIGINT,
    address_line_1 VARCHAR(255),
    address_line_2 VARCHAR(255),
    city_id BIGINT,
    country_id BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (city_id) REFERENCES city(id),
    FOREIGN KEY (country_id) REFERENCES country(id)
);

CREATE TABLE IF NOT EXISTS customer_family_member (
    customer_id BIGINT,
    family_member_customer_id BIGINT,
    PRIMARY KEY (customer_id, family_member_customer_id),
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (family_member_customer_id) REFERENCES customer(id)
);