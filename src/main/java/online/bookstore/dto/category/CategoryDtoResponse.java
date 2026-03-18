package online.bookstore.dto.category;

import lombok.Data;

@Data
public class CategoryDtoResponse {
    private Long id;
    private String name;
    private String description;
}
