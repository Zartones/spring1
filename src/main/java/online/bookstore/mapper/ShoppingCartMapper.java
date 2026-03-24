package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDtoResponse toDto(ShoppingCart shoppingCart);

}
