package online.bookstore.service;

import online.bookstore.dto.order.OrderRequestDto;
import online.bookstore.dto.order.OrderResponseDto;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderResponseDto saveOrder(Long userId, OrderRequestDto orderDtoRequest);

    Page<OrderResponseDto> getOrders(Pageable pageable, Long userId);

    OrderResponseDto update(Long userId, Long orderId, Status status);

    Page<OrderItemResponseDto> getItems(Pageable pageable, Long userId, Long orderId);

    OrderItemResponseDto getItem(Long userId, Long orderId, Long itemId);
}
