package online.bookstore.dto.cart.item;

import lombok.Data;
import online.bookstore.model.Book;
import online.bookstore.model.ShoppingCart;

@Data
public class ItemDtoResponse {
    private long id;

    private ShoppingCart shoppingCart;

    private Book book;

    private int quantity;
}
