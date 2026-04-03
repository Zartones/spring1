package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.model.CartItem;
import online.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toOrderItemResponse(OrderItem orderItem);

    @Mapping(target = "price", source = "book.price")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    OrderItem toOrderItem(CartItem cartItem);
}
