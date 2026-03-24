package online.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import online.bookstore.model.User;
import online.bookstore.service.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for shopping carts")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add item to cart")
    @PostMapping
    public ShoppingCartDtoResponse addItem(@AuthenticationPrincipal User user,
                                           @Valid @RequestBody ItemDtoRequest itemDtoRequest) {
        return shoppingCartService.saveItem(user.getId(), itemDtoRequest);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get items from cart")
    @GetMapping
    public ShoppingCartDtoResponse getItems(@AuthenticationPrincipal User user) {
        return shoppingCartService.get(user.getId());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Change quantity")
    @PutMapping("items/{cartItemId}")
    public ShoppingCartDtoResponse updateItems(@AuthenticationPrincipal User user,
                                               @PathVariable Long cartItemId,
                                               @Valid @RequestBody ItemDtoRequest request) {
        return shoppingCartService.update(user.getId(), cartItemId, request.getQuantity());
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove item")
    @DeleteMapping("items/{cartItemId}")
    public void removeItems(@AuthenticationPrincipal User user, @PathVariable Long cartItemId) {
        shoppingCartService.removeItem(user.getId(), cartItemId);
    }
}
