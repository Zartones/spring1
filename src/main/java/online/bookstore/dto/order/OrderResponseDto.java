package online.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import online.bookstore.dto.order.item.OrderItemResponseDto;
import online.bookstore.model.Status;

@Data
public class OrderResponseDto {
    private Long id;

    private Long userId;

    private Status status;

    private BigDecimal total;

    private LocalDateTime orderDate;

    private Set<OrderItemResponseDto> orderItems;
}
