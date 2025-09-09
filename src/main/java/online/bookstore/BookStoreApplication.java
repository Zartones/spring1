package online.bookstore;

import online.bookstore.model.Book;
import online.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.math.BigDecimal;

@SpringBootApplication
public class BookStoreApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("Title");
            book.setAuthor("Author");
            book.setPrice(BigDecimal.valueOf(100));
            book.setDescription("Description");
            book.setCoverImage("Image");
            book.setIsbn("Isbn");

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
