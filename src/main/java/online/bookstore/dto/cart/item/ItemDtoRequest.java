package online.bookstore.dto.cart.item;

import lombok.Data;

@Data
public class ItemDtoRequest {

    private Long bookId;

    private int quantity;
}
