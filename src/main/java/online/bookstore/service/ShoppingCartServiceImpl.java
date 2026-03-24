package online.bookstore.service;

import jakarta.transaction.Transactional;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import online.bookstore.exception.EntityNotFoundException;
import online.bookstore.mapper.ShoppingCartMapper;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import online.bookstore.model.User;
import online.bookstore.repository.CartItemRepository;
import online.bookstore.repository.ShoppingCartRepository;
import online.bookstore.repository.book.BookRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;

    private final BookRepository bookRepository;

    @Override
    public ShoppingCartDtoResponse saveItem(Long userId, ItemDtoRequest itemDtoRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        CartItem existingItem = shoppingCart
                .getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(itemDtoRequest.getBookId()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + itemDtoRequest.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setBook(bookRepository.findById(itemDtoRequest.getBookId()).get());
            cartItem.setQuantity(itemDtoRequest.getQuantity());
            cartItem.setShoppingCart(shoppingCart);

            shoppingCart.getCartItems().add(cartItem);
        }

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDtoResponse get(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void removeItem(Long userId, Long itemId) {
        ShoppingCart oldCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, oldCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        ShoppingCart newCart = cartItem.getShoppingCart();
        newCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(newCart);

    }

    @Override
    public ShoppingCartDtoResponse update(Long userId, Long itemId, int quantity) {
        ShoppingCart oldCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found"));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(itemId, oldCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));

        if (Objects.equals(cartItem.getShoppingCart().getUser().getId(), userId)) {
            cartItem.setQuantity(quantity);
        }
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void addUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
