package online.bookstore.service;

import online.bookstore.dto.order.OrderDtoRequest;
import online.bookstore.dto.order.OrderDtoResponse;
import online.bookstore.dto.order.item.OrderItemDtoResponse;
import online.bookstore.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDtoResponse saveOrder(Long userId, OrderDtoRequest orderDtoRequest);

    Page<OrderDtoResponse> getOrders(Pageable pageable, Long userId);

    OrderDtoResponse update(Long userId, Long orderId, Status status);

    Page<OrderItemDtoResponse> getItems(Pageable pageable, Long userId, Long orderId);

    OrderItemDtoResponse getItem(Long userId, Long orderId, Long itemId);
}
