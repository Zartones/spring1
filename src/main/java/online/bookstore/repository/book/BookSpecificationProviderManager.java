package online.bookstore.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.model.Book;
import online.bookstore.repository.SpecificationProvider;
import online.bookstore.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProvider;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProvider.stream()
                .filter(b -> b.getKey().equals(key))
                .findFirst().orElseThrow(() ->
                        new RuntimeException("Can`t find correct specification provider for key: "
                                + key));
    }
}
