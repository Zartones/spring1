package online.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.bookstore.dto.cart.ShoppingCartDtoResponse;
import online.bookstore.dto.cart.item.ItemDtoRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTests {
    private static final String BASE_URL = "/cart";

    protected static MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Add item to cart")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addItem_asUser_returnsUpdatedCart() throws Exception {
        ItemDtoRequest request = new ItemDtoRequest();
        request.setBookId(1L);
        request.setQuantity(2);

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        ShoppingCartDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.getCartItems()).hasSize(1);
        assertThat(actual.getCartItems().iterator().next().getBookId()).isEqualTo(1L);
        assertThat(actual.getCartItems().iterator().next().getQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("Add item with invalid request body returns bad request")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void addItem_withInvalidBody_returnsBadRequest() throws Exception {
        ItemDtoRequest request = new ItemDtoRequest();
        request.setBookId(null);
        request.setQuantity(-1);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Get items from cart")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getItems_asUser_returnsShoppingCart() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andReturn();

        ShoppingCartDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual).isNotNull();
        assertThat(actual.getUserId()).isNotNull();
    }

    @Test
    @DisplayName("Attempt to get cart as unauthenticated user")
    public void getItems_asGuest_returnsUnauthorized() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Update item quantity in cart")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItems_asUser_returnsUpdatedCart() throws Exception {
        ItemDtoRequest request = new ItemDtoRequest();
        request.setBookId(1L);
        request.setQuantity(5);

        MvcResult result = mockMvc.perform(put(BASE_URL + "/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        ShoppingCartDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                ShoppingCartDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual.getCartItems()).anyMatch(item -> item.getQuantity() == 5);
    }

    @Test
    @DisplayName("Update item with invalid quantity returns bad request")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItems_withInvalidQuantity_returnsBadRequest() throws Exception {
        ItemDtoRequest request = new ItemDtoRequest();
        request.setBookId(1L);
        request.setQuantity(-1);

        mockMvc.perform(put(BASE_URL + "/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Remove item from cart")
    @WithUserDetails("user@example.com")
    @Sql(scripts = "classpath:database/add-cart-with-items.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-cart.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void removeItems_asUser_returnsNoContent() throws Exception {
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/items/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }


    @Test
    @DisplayName("Attempt to remove item as unauthenticated user")
    public void removeItems_asGuest_returnsUnauthorized() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/items/1"))
                .andExpect(status().isUnauthorized());
    }
}
