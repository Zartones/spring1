package online.bookstore.dto.cart;

import java.util.Set;
import lombok.Data;
import online.bookstore.dto.cart.item.ItemDtoResponse;

@Data
public class ShoppingCartDtoResponse {
    private Long id;
    private Long userId;
    private Set<ItemDtoResponse> cartItems;
}
