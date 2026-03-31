package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.OrderRequestDto;
import online.bookstore.dto.order.OrderResponseDto;
import online.bookstore.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderResponseDto toOrderResponse(Order order);

    Order toModel(OrderRequestDto orderDtoRequest);
}
