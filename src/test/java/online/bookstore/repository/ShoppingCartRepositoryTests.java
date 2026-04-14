package online.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import online.bookstore.model.ShoppingCart;
import online.bookstore.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTests {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Find shopping cart by user id")
    public void findByUserId_ValidId_ReturnOptional() {
        User user = new User();
        user.setFirstName("Bob");
        user.setLastName("Lerc");
        user.setPassword("1234");
        user.setEmail("qwer@gmail.com");

        userRepository.save(user);

        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);

        shoppingCartRepository.save(shoppingCart);

        Optional<ShoppingCart> result = shoppingCartRepository.findByUserId(user.getId());
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getUser().getFirstName()).isEqualTo("Bob");
    }
}
