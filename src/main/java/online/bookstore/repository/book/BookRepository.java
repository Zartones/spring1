package online.bookstore.repository.book;

import java.util.List;
import online.bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface BookRepository extends JpaRepository<Book, Long>,
        JpaSpecificationExecutor<Book>,
        PagingAndSortingRepository<Book, Long> {
    List<Book> findAllByCategoriesId(Long categoryId);

}
