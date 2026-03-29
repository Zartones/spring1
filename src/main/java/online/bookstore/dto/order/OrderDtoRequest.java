package online.bookstore.dto.order;

import lombok.Data;
import online.bookstore.model.Status;

@Data
public class OrderDtoRequest {

    private String shippingAddress;

    private Status status;
}
