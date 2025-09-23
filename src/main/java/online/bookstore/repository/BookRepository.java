package online.bookstore.repository;

import java.util.List;
import online.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
