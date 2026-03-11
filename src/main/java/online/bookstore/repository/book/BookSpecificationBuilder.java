package online.bookstore.repository.book;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationBuilder;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters bookSearchParameters) {
        Specification<Book> specification = Specification.where(null);
        if (bookSearchParameters.titles() != null && bookSearchParameters.titles().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(KEY_TITLE)
                    .getSpecification(bookSearchParameters.titles()));
        }
        if (bookSearchParameters.authors() != null && bookSearchParameters.authors().length > 0) {
            specification = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(KEY_AUTHOR)
                    .getSpecification(bookSearchParameters.authors()));
        }
        return specification;
    }
}
