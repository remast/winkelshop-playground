INSERT INTO categories (id, name, description) VALUES
('11111111-1111-1111-1111-111111111111', 'Zauberstäbe', 'Zauberstaebe fuer Hexen und Zauberer'),
('11111111-1111-1111-1111-111111111112', 'Bücher', 'Magische Lehr- und Fachbuecher');

INSERT INTO products (id, name, description, price, currency, in_stock, stock, category_id, image_url) VALUES
('22222222-2222-2222-2222-222222222221', 'Stechpalme-Zauberstab', '11 Zoll, Phönixfederkern, passend fuer praezise Zauber', 7.0, 'GALLEON', TRUE, 14, '11111111-1111-1111-1111-111111111111', 'https://cdn.zauberbedarf.test/products/prod_wand_holly_001.jpg'),
('22222222-2222-2222-2222-222222222222', 'Fortgeschrittene Zaubertrankkunst', 'Lehrbuch fuer fortgeschrittene Trankbrauer', 5.0, 'GALLEON', TRUE, 30, '11111111-1111-1111-1111-111111111112', 'https://cdn.zauberbedarf.test/products/prod_book_potions_001.jpg'),
('22222222-2222-2222-2222-222222222223', 'Hogwarts: Eine Geschichte', 'Historische Chronik von Hogwarts', 4.0, 'GALLEON', TRUE, 20, '11111111-1111-1111-1111-111111111112', 'https://cdn.zauberbedarf.test/products/prod_book_hogwarts_001.jpg');

INSERT INTO users (id, name, email, password, role, created_at) VALUES
('33333333-3333-3333-3333-333333333331', 'Harry Potter', 'harry.potter@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('33333333-3333-3333-3333-333333333332', 'Hermione Granger', 'hermione.granger@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('33333333-3333-3333-3333-333333333333', 'Ron Weasley', 'ron.weasley@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('33333333-3333-3333-3333-333333333334', 'Draco Malfoy', 'draco.malfoy@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z'),
('33333333-3333-3333-3333-333333333335', 'Luna Lovegood', 'luna.lovegood@hogwarts.test', 'Expelliarmus123!', 'buyer', TIMESTAMP WITH TIME ZONE '2026-01-01T00:00:00Z');

INSERT INTO orders (id, user_id, status, payment_method, total, currency, shipping_full_name, shipping_street, shipping_city, shipping_postal_code, shipping_country, created_at) VALUES
('44444444-4444-4444-4444-444444444441', '33333333-3333-3333-3333-333333333331', 'placed', 'invoice', 25.0, 'GALLEON', 'Harry Potter', 'Ligusterweg 4', 'Little Whinging', '12345', 'UK', TIMESTAMP WITH TIME ZONE '2026-05-25T10:15:30Z'),
('44444444-4444-4444-4444-444444444442', '33333333-3333-3333-3333-333333333331', 'shipped', 'invoice', 8.0, 'GALLEON', 'Harry Potter', 'Ligusterweg 4', 'Little Whinging', '12345', 'UK', TIMESTAMP WITH TIME ZONE '2026-05-20T08:02:10Z');

INSERT INTO order_items (id, order_id, product_id, name, quantity, unit_price) VALUES
('55555555-5555-5555-5555-555555555551', '44444444-4444-4444-4444-444444444441', '22222222-2222-2222-2222-222222222221', 'Stechpalme-Zauberstab', 3, 7.0),
('55555555-5555-5555-5555-555555555552', '44444444-4444-4444-4444-444444444441', '22222222-2222-2222-2222-222222222223', 'Hogwarts: Eine Geschichte', 1, 4.0),
('55555555-5555-5555-5555-555555555553', '44444444-4444-4444-4444-444444444442', '22222222-2222-2222-2222-222222222223', 'Hogwarts: Eine Geschichte', 2, 4.0);
