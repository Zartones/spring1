INSERT INTO categories (id, name, description)
VALUES (1, 'Test Category', 'Test Description');

INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Test Title', 'Test Author', 'Test Isbn', 100);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);