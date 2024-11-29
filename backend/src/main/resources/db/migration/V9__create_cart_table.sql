CREATE TABLE carts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    shipping_cost DOUBLE NOT NULL,
    discount DOUBLE NOT NULL DEFAULT 0.0,
    CONSTRAINT fk_user_cart FOREIGN KEY (user_id) REFERENCES users(id)
);
 