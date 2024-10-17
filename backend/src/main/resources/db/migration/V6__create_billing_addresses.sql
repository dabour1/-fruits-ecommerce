 
CREATE TABLE billing_addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_related_information_id BIGINT NOT NULL,
    address_line_one VARCHAR(255) NOT NULL,
    address_line_two VARCHAR(255),
    post_code VARCHAR(50) NOT NULL,
    CONSTRAINT fk_customer_related_information_billing FOREIGN KEY (customer_related_information_id) REFERENCES customer_related_informations (id)
);