package online.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.mapper.BookMapper;
import online.bookstore.model.Book;
import online.bookstore.model.Category;
import online.bookstore.repository.book.BookRepository;
import online.bookstore.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class BookServiceTests {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Test save method")
    public void save_ValidCreateBookRequestDto_ReturnBookDto() {
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Test Title");
        request.setPrice(BigDecimal.valueOf(100.00));
        request.setCategories(Set.of(new Category()));

        Book book = new Book();
        book.setTitle(request.getTitle());

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setTitle(book.getTitle());

        when(bookMapper.toModel(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto saveDto = bookService.save(request);

        assertThat(saveDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Test update method")
    public void updateBook_ValidId_ReturnBookDto() {
        Long id = 1L;
        CreateBookRequestDto request = new CreateBookRequestDto();
        request.setTitle("Test Title");
        request.setPrice(BigDecimal.valueOf(100));

        Book oldBook = new Book();
        oldBook.setId(id);
        oldBook.setTitle("Old Title");
        oldBook.setPrice(BigDecimal.valueOf(50));

        BookDto bookDto = new BookDto();
        bookDto.setTitle(request.getTitle());
        bookDto.setPrice(request.getPrice());

        when(bookRepository.findById(id)).thenReturn(Optional.of(oldBook));
        when(bookRepository.save(oldBook)).thenReturn(oldBook);
        when(bookMapper.toDto(oldBook)).thenReturn(bookDto);

        BookDto updateDto = bookService.updateBook(id, request);

        assertThat(updateDto).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Test get by id")
    public void getBookById_ValidId_ReturnBookDto() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("Test Title");
        book.setPrice(BigDecimal.valueOf(50));

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto getDto = bookService.getBookById(id);

        assertThat(getDto).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Test get all books")
    public void findAll_Return_ReturnPageOfBooks() {
        Pageable pageable = PageRequest.of(0, 10);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setPrice(BigDecimal.valueOf(50));

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setPrice(book.getPrice());

        List<Book> books = List.of(book);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(bookDto);
    }

    @Test
    @DisplayName("Test delete method")
    public void deleteById_ValidId() {
        Long id = 1L;

        bookService.deleteById(id);

        verify(bookRepository, times(1)).deleteById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Test search method")
    public void search_ValidParameters_ReturnPageOfDto() {
        BookSearchParameters params = new BookSearchParameters(
                new String[]{"Title"},
                new String[]{"Author"}
        );
        Pageable pageable = PageRequest.of(0, 10);

        Specification<Book> specification = mock(Specification.class);

        Book book = new Book();
        book.setId(1L);

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);

        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookSpecificationBuilder.build(params)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.search(params, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(bookDto);

        verify(bookSpecificationBuilder).build(params);
        verify(bookRepository).findAll(specification, pageable);
    }

    @Test
    @DisplayName("Test find by category method")
    public void findByCategory_ValidId_ReturnPageOfDto() {
        Long categoryId = 1L;

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Title");

        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());

        List<Book> books = List.of(book);
        when(bookRepository.findAllByCategoriesId(categoryId)).thenReturn(books);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        Page<BookDto> result = bookService.findByCategory(categoryId);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(bookDto);

    }


}
