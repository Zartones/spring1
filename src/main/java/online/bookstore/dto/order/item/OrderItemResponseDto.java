package online.bookstore.dto.order.item;

import lombok.Data;
import online.bookstore.model.Book;

@Data
public class OrderItemResponseDto {
    private Long id;

    private Book book;

    private int quantity;

}
