package online.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.bookstore.util.TestUtil;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTests {

    private static final String BASE_URL = "/categories";

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
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add a category")
    @Sql(scripts = "classpath:database/clear-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createCategory_asAdmin_returnsCreatedCategory() throws Exception {
        CategoryDtoRequest categoryDtoRequest = TestUtil.sampleRequest();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDtoRequest)))
                .andReturn();

        CategoryDtoResponse expected = TestUtil.sampleResponse();

        CategoryDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories")
    @Sql(scripts = "classpath:database/add-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_asUser_returnsAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andReturn();

        CategoryDtoResponse expected = TestUtil.sampleResponse();

        String json = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        JsonNode firstBook = root.get("content").get(0);

        CategoryDtoResponse actual = objectMapper.treeToValue(firstBook, CategoryDtoResponse.class);

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a category by id")
    @Sql(scripts = "classpath:database/add-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCategoryById_existingId_returnsCategoryWithId1() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/1"))
                .andReturn();

        CategoryDtoResponse expected = TestUtil.sampleResponse();

        CategoryDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("\"id\":1");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Attempt to get a category by non existing id")
    public void getCategoryById_nonExistingId_returnsNotFound() throws Exception {
        long nonExistingId = 999L;

        mockMvc.perform(get(BASE_URL + "/" + nonExistingId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a category")
    @Sql(scripts = "classpath:database/add-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateCategory_asAdmin_returnsUpdatedCategory() throws Exception {
        CategoryDtoRequest req = TestUtil.sampleRequest();
        req.setName("Updated Name");

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn();

        CategoryDtoResponse expected = TestUtil.sampleResponse();
        expected.setName("Updated Name");

        CategoryDtoResponse actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDtoResponse.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a category")
    @Sql(scripts = "classpath:database/add-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-categories.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteCategory_asAdmin_returnsEmptyTrue() throws Exception {
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Attempt to delete a category as user")
    public void deleteCategory_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books by a category")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBooksByCategoryId_asUser_returnsAllBooksWithCategoryId1() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/1/books"))
                .andReturn();

        BookDto expected = TestUtil.sampleResponseDto();

        String json = result.getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        JsonNode firstBook = root.get("content").get(0);

        BookDto actual = objectMapper.treeToValue(firstBook, BookDto.class);

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expected);
    }
}
