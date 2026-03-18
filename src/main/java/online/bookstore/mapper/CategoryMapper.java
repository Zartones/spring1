package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import online.bookstore.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDtoResponse toDto(Category category);

    Category toEntity(CategoryDtoRequest categoryDto);

    void updateCategory(CategoryDtoRequest dto, @MappingTarget Category category);
}
