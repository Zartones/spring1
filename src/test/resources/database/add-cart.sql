INSERT INTO users (id, email, password, first_name, last_name) VALUES (1, 'user@example.com', '1234', 'Bob', 'Laurenc');

INSERT INTO roles (id, name) VALUES (1, 'USER');
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Test Book', 'Test Author', '978-0000000001', 19.99, false);

INSERT INTO shopping_carts (id, user_id) VALUES (1, 1);
