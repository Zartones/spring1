package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.model.CartItem;
import online.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemResponseDto toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "order", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);
}
