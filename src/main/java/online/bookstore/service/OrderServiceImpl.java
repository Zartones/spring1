package online.bookstore.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderRequestDto;
import online.bookstore.dto.order.OrderResponseDto;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.OrderItemMapper;
import online.bookstore.mapper.OrderMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.Order;
import online.bookstore.model.OrderItem;
import online.bookstore.model.Status;
import online.bookstore.model.User;
import online.bookstore.repository.OrderItemRepository;
import online.bookstore.repository.OrderRepository;
import online.bookstore.repository.ShoppingCartRepository;
import online.bookstore.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    private final OrderItemMapper orderItemMapper;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto saveOrder(Long userId, OrderRequestDto orderDtoRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userId));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderMapper.toModel(orderDtoRequest).getShippingAddress());
        order.setStatus(Status.PENDING);

        Set<CartItem> cartItems = shoppingCartRepository.findByUserId(userId).get().getCartItems();

        Set<OrderItem> orderItems = cartItems.stream().map(orderItemMapper::toOrderItem)
                .peek(i -> i.setOrder(order))
                .collect(Collectors.toSet());

        BigDecimal total = orderItems.stream()
                .map(i -> i.getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotal(total);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderResponseDto> getOrders(Pageable pageable, Long userId) {
        return new PageImpl<>(orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toOrderResponse)
                .toList());
    }

    @Override
    public OrderResponseDto update(Long userId, Long orderId, Status status) {
        Order order = findOrderByUserAndId(orderId, userId);
        order.setStatus(status);
        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderItemResponseDto> getItems(Pageable pageable, Long userId, Long orderId) {
        Order order = findOrderByUserAndId(orderId, userId);

        return new PageImpl<>(orderItemRepository.findAllByOrderId(order.getId(), pageable).stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList());
    }

    @Override
    public OrderItemResponseDto getItem(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found for id: " + itemId));
        return orderItemMapper.toOrderItemResponse(orderItem);
    }

    private Order findOrderByUserAndId(Long orderId, Long userId) {
        return orderRepository.findByIdAndUserId(orderId, userId).orElseThrow(() ->
                new EntityNotFoundException("Order not found for id: " + orderId));
    }
}
