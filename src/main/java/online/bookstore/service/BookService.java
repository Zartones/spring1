package online.bookstore.service;

import java.util.List;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto bookRequestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto);

    void deleteById(Long id);
}

