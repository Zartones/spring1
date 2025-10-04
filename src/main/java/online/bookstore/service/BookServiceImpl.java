package online.bookstore.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.CreateBookRequestDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto bookRequestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find book with id " + id));
        book.setTitle(bookRequestDto.getTitle());
        book.setDescription(bookRequestDto.getDescription());
        book.setAuthor(bookRequestDto.getAuthor());
        book.setPrice(bookRequestDto.getPrice());
        book.setIsbn(bookRequestDto.getIsbn());
        book.setCoverImage(bookRequestDto.getCoverImage());
        return bookMapper.toDto(bookRepository.save(book));

    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
