package online.bookstore.service;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import online.bookstore.mapper.ShoppingCartMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import online.bookstore.repository.CartItemRepository;
import online.bookstore.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartDtoResponse saveItem(Long userId, ItemDtoRequest itemDtoRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));
        CartItem existingItem = shoppingCart
                .getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(itemDtoRequest.getBook().getId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + itemDtoRequest.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBook(itemDtoRequest.getBook());
            cartItem.setQuantity(itemDtoRequest.getQuantity());
            cartItem.setShoppingCart(shoppingCart);

            shoppingCart.getCartItems().add(cartItem);
        }

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDtoResponse get(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Shopping cart not found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void removeItem(Long itemId) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);

    }

    @Override
    public ShoppingCartDtoResponse update(Long userId, Long itemId, int quantity) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (Objects.equals(cartItem.getShoppingCart().getUser().getId(), userId)) {
            cartItem.setQuantity(quantity);
        }
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }
}
