package online.bookstore.mapper;

import java.util.stream.Collectors;
import online.bookstore.config.MapperConfig;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookDtoWithoutCategoryIds;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.model.Book;
import online.bookstore.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBook(CreateBookRequestDto dto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoryIds(book.getCategories()
                    .stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }
    }

}
