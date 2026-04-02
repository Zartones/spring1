package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.OrderRequestDto;
import online.bookstore.dto.order.OrderResponseDto;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.model.Order;
import online.bookstore.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toOrderResponse(Order order);

    Order toModel(OrderRequestDto orderDtoRequest);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toItemDto(OrderItem orderItem);
}
