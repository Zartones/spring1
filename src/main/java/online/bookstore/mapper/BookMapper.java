package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.BookDto;
import online.bookstore.dto.CreateBookRequestDto;
import online.bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBook(CreateBookRequestDto dto, @MappingTarget Book book);

}
