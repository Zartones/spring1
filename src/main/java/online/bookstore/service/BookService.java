package online.bookstore.service;

import online.bookstore.dto.BookDto;
import online.bookstore.dto.BookSearchParameters;
import online.bookstore.dto.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable);
}

