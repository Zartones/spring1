package online.bookstore.service;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.BookSearchParameters;
import online.bookstore.dto.CreateBookRequestDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookRequestDto) {
        Book book = bookMapper.toModel(bookRequestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return new PageImpl<>(bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList());
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
        bookMapper.updateBook(bookRequestDto,book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParameters bookSearchParameters, Pageable pageable) {
        Specification<Book> specification = bookSpecificationBuilder.build(bookSearchParameters);
        return new PageImpl<>(bookRepository.findAll(specification, pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList());
    }

}
