package online.bookstore.service;

import java.util.List;
import online.bookstore.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}

