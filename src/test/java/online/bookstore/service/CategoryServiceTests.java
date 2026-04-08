package online.bookstore.service;

import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import online.bookstore.mapper.CategoryMapper;
import online.bookstore.model.Category;
import online.bookstore.repository.CategoryRepository;
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
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests  {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Test find all method")
    public void findAll_ValidPageable_ReturnPageOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category = new Category();
        CategoryDtoResponse responseDto = new CategoryDtoResponse();
        Page<Category> categoryPage = new PageImpl<>(List.of(category), pageable, 1);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        Page<CategoryDtoResponse> result = categoryService.findAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(responseDto);
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Test get by id method")
    public void getById_ValidId_ReturnResponseDto() {
        Long id = 1L;
        Category category = new Category();
        CategoryDtoResponse responseDto = new CategoryDtoResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(responseDto);

        CategoryDtoResponse result = categoryService.getById(id);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Test save method")
    public void save_ValidRequestDto_ReturnResponseDto() {
        CategoryDtoRequest requestDto = new CategoryDtoRequest();
        Category category = new Category();
        Category savedCategory = new Category();
        CategoryDtoResponse responseDto = new CategoryDtoResponse();

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(responseDto);

        CategoryDtoResponse result = categoryService.save(requestDto);

        assertThat(result).isEqualTo(responseDto);
        verify(categoryRepository).save(category);
    }

    @Test
    @DisplayName("Test update method")
    public void update_ValidId_ReturnResponseDto() {
        Long id = 1L;
        CategoryDtoRequest requestDto = new CategoryDtoRequest();
        Category existingCategory = new Category();
        CategoryDtoResponse responseDto = new CategoryDtoResponse();

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);
        when(categoryMapper.toDto(existingCategory)).thenReturn(responseDto);

        CategoryDtoResponse result = categoryService.update(id, requestDto);

        assertThat(result).isEqualTo(responseDto);

        verify(categoryMapper).updateCategory(requestDto, existingCategory);
    }

    @Test
    @DisplayName("Test delete method")
    public void deleteById_ValidId_CallsRepository() {
        Long id = 1L;

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }
}
