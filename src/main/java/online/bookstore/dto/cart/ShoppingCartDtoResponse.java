package online.bookstore.dto.cart;

import java.util.Set;
import lombok.Data;
import online.bookstore.model.CartItem;
import online.bookstore.model.User;

@Data
public class ShoppingCartDtoResponse {
    private Long id;
    private User user;
    private Set<CartItem> cartItems;
}
