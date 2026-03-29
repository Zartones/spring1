package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.OrderDtoRequest;
import online.bookstore.dto.order.OrderDtoResponse;
import online.bookstore.model.Order;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    OrderDtoResponse toOrderResponse(Order order);

    Order toModel(OrderDtoRequest orderDtoRequest);
}
