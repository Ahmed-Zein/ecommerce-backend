DO $$
DECLARE
    userId1 INT := 1;
    userId2 INT := 2;
    product1_id INT;
    product2_id INT;
    product3_id INT;
    product4_id INT;
    product5_id INT;
    address1_id INT;
    address2_id INT;
    order1_id INT;
    order2_id INT;
    order3_id INT;
    order4_id INT;
    order5_id INT;
BEGIN
    -- Clear existing data
    DELETE FROM web_order_quantities;
    DELETE FROM web_order;
    DELETE FROM inventory;
    DELETE FROM product;
    DELETE FROM address;

    -- Insert new data
    INSERT INTO product (name, short_description, long_description, price) VALUES
    ('Product #1', 'Product one short description.', 'This is a very long description of product #1.', 5.50),
    ('Product #2', 'Product two short description.', 'This is a very long description of product #2.', 10.56),
    ('Product #3', 'Product three short description.', 'This is a very long description of product #3.', 2.74),
    ('Product #4', 'Product four short description.', 'This is a very long description of product #4.', 15.69),
    ('Product #5', 'Product five short description.', 'This is a very long description of product #5.', 42.59);

    -- Get product IDs
    SELECT id INTO product1_id FROM product WHERE name = 'Product #1';
    SELECT id INTO product2_id FROM product WHERE name = 'Product #2';
    SELECT id INTO product3_id FROM product WHERE name = 'Product #3';
    SELECT id INTO product4_id FROM product WHERE name = 'Product #4';
    SELECT id INTO product5_id FROM product WHERE name = 'Product #5';

    -- Insert into inventory
    INSERT INTO inventory (product_id, quantity) VALUES
    (product1_id, 5),
    (product2_id, 8),
    (product3_id, 12),
    (product4_id, 73),
    (product5_id, 2);

    -- Insert into address
    INSERT INTO address (address_line_1, city, country, user_id) VALUES
    ('123 Tester Hill', 'Testerton', 'England', userId1),
    ('312 Spring Boot', 'Hibernate', 'England', userId2);

    -- Get address IDs
    SELECT id INTO address1_id FROM address WHERE user_id = userId1 ORDER BY id DESC LIMIT 1;
    SELECT id INTO address2_id FROM address WHERE user_id = userId2 ORDER BY id DESC LIMIT 1;

    -- Insert into web_order
    INSERT INTO web_order (address_id, user_id) VALUES
    (address1_id, userId1),
    (address1_id, userId1),
    (address1_id, userId1),
    (address2_id, userId2),
    (address2_id, userId2);

    -- Get order IDs
    SELECT id INTO order1_id FROM web_order WHERE address_id = address1_id AND user_id = userId1 ORDER BY id DESC LIMIT 1;
    SELECT id INTO order2_id FROM web_order WHERE address_id = address1_id AND user_id = userId1 ORDER BY id DESC OFFSET 1 LIMIT 1;
    SELECT id INTO order3_id FROM web_order WHERE address_id = address1_id AND user_id = userId1 ORDER BY id DESC OFFSET 2 LIMIT 1;
    SELECT id INTO order4_id FROM web_order WHERE address_id = address2_id AND user_id = userId2 ORDER BY id DESC LIMIT 1;
    SELECT id INTO order5_id FROM web_order WHERE address_id = address2_id AND user_id = userId2 ORDER BY id DESC OFFSET 1 LIMIT 1;

    -- Insert into web_order_quantities
    INSERT INTO web_order_quantities (order_id, product_id, quantity) VALUES
    (order1_id, product1_id, 5),
    (order1_id, product2_id, 5),
    (order2_id, product3_id, 5),
    (order2_id, product2_id, 5),
    (order2_id, product5_id, 5),
    (order3_id, product3_id, 5),
    (order4_id, product4_id, 5),
    (order4_id, product2_id, 5),
    (order5_id, product3_id, 5),
    (order5_id, product1_id, 5);
END $$;