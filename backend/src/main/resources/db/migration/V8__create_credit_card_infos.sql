CREATE TABLE credit_card_infos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_related_information_id BIGINT NOT NULL,
    card_number VARCHAR(255) NOT NULL,
    expiration_date VARCHAR(255) NOT NULL,
    cvv VARCHAR(10) NOT NULL,
    CONSTRAINT fk_customer_related_information FOREIGN KEY (customer_related_information_id) REFERENCES customer_related_informations (id)
);
