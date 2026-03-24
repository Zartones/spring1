package online.bookstore.service;

import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;

public interface ShoppingCartService {

    ShoppingCartDtoResponse saveItem(Long userId, ItemDtoRequest itemDtoRequest);

    ShoppingCartDtoResponse get(Long userId);

    void removeItem(Long itemId);

    ShoppingCartDtoResponse update(Long userId, Long itemId, int quantity);

}
