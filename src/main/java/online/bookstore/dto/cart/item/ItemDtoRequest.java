package online.bookstore.dto.cart.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ItemDtoRequest {

    @NotNull
    @Positive
    private Long bookId;

    @Positive
    private int quantity;
}
