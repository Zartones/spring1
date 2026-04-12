package online.bookstore.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import online.bookstore.mapper.ShoppingCartMapper;
import online.bookstore.model.Book;
import online.bookstore.model.CartItem;
import online.bookstore.model.ShoppingCart;
import online.bookstore.model.User;
import online.bookstore.repository.CartItemRepository;
import online.bookstore.repository.ShoppingCartRepository;
import online.bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTests {
    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;


    @Test
    @DisplayName("Test save an item method")
    public void saveItem_ValidRequestDto_ReturnsResponseDto() {
        Long userId = 1L;
        ItemDtoRequest request = new ItemDtoRequest();
        request.setBookId(2L);
        request.setQuantity(2);

        ShoppingCart cart = new ShoppingCart();
        cart.setCartItems(new HashSet<>());

        Book book = new Book();
        book.setId(2L);

        ShoppingCartDtoResponse responseDto = new ShoppingCartDtoResponse();

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toDto(cart)).thenReturn(responseDto);

        ShoppingCartDtoResponse result = shoppingCartService.saveItem(userId, request);

        assertThat(result).isEqualTo(responseDto);
        assertThat(cart.getCartItems()).hasSize(1);
        verify(bookRepository).findById(2L);
    }

    @Test
    @DisplayName("Test get shopping cart by user id method")
    public void get_ValidUserId_ReturnsResponseDto() {
        Long userId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart();
        ShoppingCartDtoResponse responseDto = new ShoppingCartDtoResponse();

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(responseDto);

        ShoppingCartDtoResponse result = shoppingCartService.get(userId);

        assertThat(result).isEqualTo(responseDto);
        verify(shoppingCartRepository).findByUserId(userId);
    }

    @Test
    @DisplayName("Test update method")
    public void update_ValidRequest_ReturnsResponseDto() {
        Long userId = 1L;
        Long itemId = 10L;
        int newQuantity = 10;

        ShoppingCart cart = new ShoppingCart();
        cart.setId(100L);

        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(cart);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(itemId, cart.getId())).thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(cart)).thenReturn(new ShoppingCartDtoResponse());

        shoppingCartService.update(userId, itemId, newQuantity);

        assertThat(cartItem.getQuantity()).isEqualTo(newQuantity);
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Test remove method")
    public void removeItem_ValidIds_CallsRepository() {
        Long userId = 1L;
        Long itemId = 10L;

        ShoppingCart cart = new ShoppingCart();
        cart.setId(100L);
        cart.setCartItems(new HashSet<>());

        CartItem cartItem = new CartItem();
        cartItem.setId(itemId);
        cartItem.setShoppingCart(cart);
        cart.getCartItems().add(cartItem);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(itemId, cart.getId())).thenReturn(Optional.of(cartItem));

        shoppingCartService.removeItem(userId, itemId);

        assertThat(cart.getCartItems()).isEmpty();
        verify(shoppingCartRepository).save(cart);
    }

    @Test
    @DisplayName("Test add user creates a new shopping cart")
    public void addUser_ValidUser_SavesCart() {
        User user = new User();

        shoppingCartService.addUser(user);

        verify(shoppingCartRepository, times(1)).save(any(ShoppingCart.class));
    }

}
