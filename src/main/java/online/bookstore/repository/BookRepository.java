package online.bookstore.repository;

import java.util.List;
import java.util.Optional;
import online.bookstore.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> getBookBId(Long id);
}
