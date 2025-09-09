package online.bookstore.repository;

import online.bookstore.model.Book;
import java.util.List;
public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
