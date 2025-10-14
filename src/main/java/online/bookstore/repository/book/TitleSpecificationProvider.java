package online.bookstore.repository.book;

import java.util.Arrays;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String KEY_TITLE = "title";

    @Override
    public String getKey() {
        return KEY_TITLE;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get(KEY_TITLE)
                .in(Arrays.stream(params).toArray());
    }
}
