package online.bookstore.service;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderDtoRequest;
import online.bookstore.dto.order.OrderDtoResponse;
import online.bookstore.dto.order.item.OrderItemDtoResponse;
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
    public OrderDtoResponse saveOrder(Long userId, OrderDtoRequest orderDtoRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for id: " + userId));
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderMapper.toModel(orderDtoRequest).getShippingAddress());
        order.setStatus(Status.PENDING);

        Set<CartItem> cartItems = shoppingCartRepository.findByUserId(userId).get().getCartItems();
        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem c : cartItems) {
            OrderItem o = new OrderItem();
            o.setBook(c.getBook());
            o.setQuantity(c.getQuantity());
            o.setOrder(order);
            o.setPrice(c.getBook().getPrice());
            orderItems.add(o);

            BigDecimal sum = o.getPrice().multiply(new BigDecimal(o.getQuantity()));
            total = total.add(sum);
        }
        order.setTotal(total);
        order.setOrderItems(orderItems);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderDtoResponse> getOrders(Pageable pageable, Long userId) {
        return new PageImpl<>(orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toOrderResponse)
                .toList());
    }

    @Override
    public OrderDtoResponse update(Long userId, Long orderId, Status status) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found for id: " + orderId));
        order.setStatus(status);
        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    @Override
    public Page<OrderItemDtoResponse> getItems(Pageable pageable, Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found for id: " + orderId));

        return new PageImpl<>(orderItemRepository.findAllByOrderId(order.getId(), pageable).stream()
                .map(orderItemMapper::toOrderItemResponse)
                .toList());
    }

    @Override
    public OrderItemDtoResponse getItem(Long userId, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found for id: " + orderId));
        OrderItem orderItem = orderItemRepository.findByIdAndOrderId(itemId, order.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found for id: " + itemId));
        return orderItemMapper.toOrderItemResponse(orderItem);
    }
}
