package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoResponse;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDtoResponse toDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    ItemDtoResponse toItemDto(CartItem cartItem);
}
