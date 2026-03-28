package online.bookstore.service;

import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import online.bookstore.model.User;

public interface ShoppingCartService {

    ShoppingCartDtoResponse saveItem(Long userId, ItemDtoRequest itemDtoRequest);

    ShoppingCartDtoResponse get(Long userId);

    void removeItem(Long userId, Long itemId);

    ShoppingCartDtoResponse update(Long userId, Long itemId, int quantity);

    void addUser(User user);

}
