package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.cart.ShoppingCartDtoRequest;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    ShoppingCartDtoResponse toDto(ShoppingCart shoppingCart);

    ShoppingCart toModel(ShoppingCartDtoRequest requestDto);

    void updateShoppingCart(ShoppingCartDtoRequest dto, @MappingTarget ShoppingCart shoppingCart);
}
