package online.bookstore.repository;

import online.bookstore.model.Book;
import online.bookstore.model.Category;
import online.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTests {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find all books by a specific category ID")
    void findAllByCategoriesId_ValidId_ReturnsList() {
        Category category = new Category();
        category.setName("Fantasy");

        categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("The Hobbit");
        book.setAuthor("J.R.R. Tolkien");
        book.setIsbn("123456789");
        book.setPrice(BigDecimal.valueOf(20));
        book.setCategories(Set.of(category));

        bookRepository.save(book);

        List<Book> result = bookRepository.findAllByCategoriesId(category.getId());

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("The Hobbit");
        assertThat(result.get(0).getCategories()).contains(category);
    }
}
