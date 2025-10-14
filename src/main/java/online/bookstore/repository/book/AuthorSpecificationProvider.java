package online.bookstore.repository.book;

import java.util.Arrays;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    private static final String KEY_AUTHOR = "author";

    @Override
    public String getKey() {
        return KEY_AUTHOR;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get(KEY_AUTHOR)
                .in(Arrays.stream(params).toArray());
    }
}
