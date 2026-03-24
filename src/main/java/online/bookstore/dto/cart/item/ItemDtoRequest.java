package online.bookstore.dto.cart.item;

import lombok.Data;
import online.bookstore.model.Book;

@Data
public class ItemDtoRequest {

    private Book book;

    private int quantity;
}
