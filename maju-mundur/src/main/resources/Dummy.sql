
INSERT INTO users (id, firstname, lastname, email, password, verified, created_date, last_modified_date, verification_code) VALUES
                                                                                                                                ('user-1', 'John', 'Doe', 'john.doe@example.com', 'hashedpassword1', true, NOW(), NOW(), NULL),
                                                                                                                                ('user-2', 'Jane', 'Smith', 'jane.smith@example.com', 'hashedpassword2', true, NOW(), NOW(), NULL);

-- Insert Customers
INSERT INTO customers (id, user_id, full_name, birth_date, phone_number, email, points) VALUES
    ('customer-1', 'user-1', 'John Doe', '1990-05-10', '081234567890', 'john.doe@example.com', 100);

-- Insert Merchants
INSERT INTO merchants (id, user_id, full_name, phone_number, email, created_at) VALUES
    ('merchant-1', 'user-2', 'Jane Smith', '081298765432', 'jane.smith@example.com', NOW());

-- Insert Products
INSERT INTO products (id, product_code, name, brand, size, price, stock, description, created_at, last_update, merchant_id) VALUES
                                                                                                                                ('product-1', 'PROD-001', 'Nike Air Max', 'Nike', 'M', 1500000, 10, 'High-quality running shoes.', NOW(), NOW(), 'merchant-1'),
                                                                                                                                ('product-2', 'PROD-002', 'Adidas Ultraboost', 'Adidas', 'L', 1800000, 5, 'Comfortable and stylish sneakers.', NOW(), NOW(), 'merchant-1');

-- Assign Roles to Users
INSERT INTO users_roles (users_id, roles_id) VALUES
                                               ('user-1', 'cb724c34-882b-4845-93b1-e6e220be8400'), -- John Doe is a CUSTOMER
                                               ('user-2', '4a7a7eea-bf17-4f3c-ae89-00b51139b12b'); -- Jane Smith is a MERCHANT
