package online.bookstore.dto.cart.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDtoRequest {

    @NotNull
    private Long bookId;

    @NotNull
    @Positive
    private int quantity;
}
