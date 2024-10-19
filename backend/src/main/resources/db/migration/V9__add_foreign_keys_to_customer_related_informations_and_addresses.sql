 
-- Add foreign key constraints to customer_related_informations
ALTER TABLE customer_related_informations
ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id),
ADD CONSTRAINT fk_shipping_address FOREIGN KEY (shipping_address_id) REFERENCES shipping_addresses (id),
ADD CONSTRAINT fk_billing_address FOREIGN KEY (billing_address_id) REFERENCES billing_addresses (id);

-- Add foreign key constraints to billing_addresses
ALTER TABLE billing_addresses
ADD CONSTRAINT fk_customer_related_information_billing FOREIGN KEY (customer_related_information_id) REFERENCES customer_related_informations (id);

-- Add foreign key constraints to shipping_addresses
ALTER TABLE shipping_addresses
ADD CONSTRAINT fk_customer_related_information_shipping FOREIGN KEY (customer_related_information_id) REFERENCES customer_related_informations (id);

-- Add foreign key constraints to credit_card_infos
ALTER TABLE credit_card_infos
ADD CONSTRAINT fk_customer_related_information_credit_card FOREIGN KEY (customer_related_information_id) REFERENCES customer_related_informations (id);
