package online.bookstore.mapper;

import online.bookstore.config.MapperConfig;
import online.bookstore.dto.order.item.OrderItemDtoResponse;
import online.bookstore.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDtoResponse toOrderItemResponse(OrderItem orderItem);
}
