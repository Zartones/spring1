package online.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDtoRequest {
    @NotBlank
    private String name;

    private String description;
}
