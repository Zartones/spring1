package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.order.OrderDtoRequest;
import online.bookstore.dto.order.OrderDtoResponse;
import online.bookstore.dto.order.item.OrderItemDtoResponse;
import online.bookstore.model.User;
import online.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management", description = "Endpoints for orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add an Order")
    public OrderDtoResponse addOrder(@AuthenticationPrincipal User user,
                                     @Valid @RequestBody OrderDtoRequest orderDtoRequest) {
        return orderService.saveOrder(user.getId(), orderDtoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get user's history of orders")
    public Page<OrderDtoResponse> getAllOrders(@AuthenticationPrincipal User user,
                                               Pageable pageable) {
        return orderService.getOrders(pageable, user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order's quantity")
    public OrderDtoResponse updateOrder(@AuthenticationPrincipal User user,
                                        @PathVariable Long id,
                                        @Valid @RequestBody OrderDtoRequest orderDtoRequest) {
        return orderService.update(user.getId(), id, orderDtoRequest.getStatus());
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items from order")
    public Page<OrderItemDtoResponse> getAllItems(@AuthenticationPrincipal User user,
                                                  @PathVariable Long orderId, Pageable pageable) {
        return orderService.getItems(pageable, user.getId(), orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("{orderId}/items/{itemId}")
    @Operation(summary = "Get item from order")
    public OrderItemDtoResponse getItem(@AuthenticationPrincipal User user,
                                        @PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.getItem(user.getId(), orderId, itemId);
    }
}
