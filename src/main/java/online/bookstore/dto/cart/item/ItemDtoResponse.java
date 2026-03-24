package online.bookstore.dto.cart.item;

import lombok.Data;

@Data
public class ItemDtoResponse {
    private Long id;

    private Long bookId;

    private String bookTitle;

    private int quantity;
}
