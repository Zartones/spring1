package online.bookstore.service;

import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDtoResponse> findAll(Pageable pageable);

    CategoryDtoResponse getById(Long id);

    CategoryDtoResponse save(CategoryDtoRequest categoryDto);

    CategoryDtoResponse update(Long id, CategoryDtoRequest categoryDto);

    void deleteById(Long id);
}
