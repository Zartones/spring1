package online.bookstore;

import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import online.bookstore.model.Category;

import java.math.BigDecimal;
import java.util.Set;

public class TestUtil {
    public CreateBookRequestDto sampleRequestDto() {
        CreateBookRequestDto req = new CreateBookRequestDto();
        req.setTitle("Test Title");
        req.setAuthor("Test Author");
        req.setIsbn("Test Isbn");
        req.setPrice(BigDecimal.valueOf(100));
        Category category = new Category();
        category.setId(1L);
        req.setCategories(Set.of(category));
        return req;
    }

    public BookDto sampleResponseDto() {
        BookDto dto = new BookDto();
        dto.setTitle("Test Title");
        dto.setAuthor("Test Author");
        dto.setIsbn("Test Isbn");
        dto.setPrice(BigDecimal.valueOf(100));
        Category category = new Category();
        category.setId(1L);
        dto.setCategoryIds(Set.of(category.getId()));
        return dto;
    }

    public CategoryDtoRequest sampleRequest() {
        CategoryDtoRequest req = new CategoryDtoRequest();
        req.setName("Test Name");
        req.setDescription("Test Description");
        return req;
    }

    public CategoryDtoResponse sampleResponse() {
        CategoryDtoResponse res = new CategoryDtoResponse();
        res.setName("Test Name");
        res.setDescription("Test Description");
        return res;
    }
}
