package online.bookstore.dto.cart;

import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.Data;
import online.bookstore.model.CartItem;
import online.bookstore.model.User;

@Data
public class ShoppingCartDtoRequest {
    @NotBlank
    private User user;
    private Set<CartItem> cartItems;
}
