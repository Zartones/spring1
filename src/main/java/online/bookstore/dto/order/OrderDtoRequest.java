package online.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderDtoRequest {

    @NotBlank
    private String shippingAddress;

}
