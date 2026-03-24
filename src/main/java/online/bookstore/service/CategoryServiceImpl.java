package online.bookstore.service;

import lombok.RequiredArgsConstructor;
import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.CategoryMapper;
import online.bookstore.model.Category;
import online.bookstore.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryDtoResponse> findAll(Pageable pageable) {
        return new PageImpl<>(categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList());
    }

    @Override
    public CategoryDtoResponse getById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toDto)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find category with id " + id));
    }

    @Override
    public CategoryDtoResponse save(CategoryDtoRequest categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public CategoryDtoResponse update(Long id, CategoryDtoRequest categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find category with id " + id));
        categoryMapper.updateCategory(categoryDto, category);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
