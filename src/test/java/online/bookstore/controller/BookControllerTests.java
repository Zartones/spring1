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
import online.bookstore.dto.book.CreateBookRequestDto;
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
public class BookControllerTests {

    private static final String BASE_URL = "/api/books";

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
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAll_asUser_returnsAllBooks() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL))
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

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a book by id")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getBookById_existingId_returnBookWithId1() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/1"))
                .andReturn();

        BookDto expected = TestUtil.sampleResponseDto();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("\"id\":1");
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add a book")
    @Sql(scripts = "classpath:database/add-categories.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createBook_asAdmin_returnsCreatedBook() throws Exception {
        CreateBookRequestDto req = TestUtil.sampleRequestDto();

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn();

        BookDto expected = TestUtil.sampleResponseDto();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Attempt to add a book with invalid data")
    public void createBook_invalidData_returnsBadRequest() throws Exception {
        CreateBookRequestDto req = TestUtil.sampleRequestDto();
        req.setTitle("");

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a book")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateBook_asAdmin_returnsUpdatedBook() throws Exception {
        CreateBookRequestDto req = TestUtil.sampleRequestDto();
        req.setTitle("Updated Title");

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn();

        BookDto expected = TestUtil.sampleResponseDto();
        expected.setTitle("Updated Title");

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                BookDto.class
        );

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .withComparatorForType(BigDecimal::compareTo, BigDecimal.class)
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a book that does not exist")
    public void updateBook_nonExistingId_returnsNotFound() throws Exception {
        CreateBookRequestDto req = TestUtil.sampleRequestDto();
        long nonExistingId = 999L;

        mockMvc.perform(put(BASE_URL + "/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a book")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteBook_asAdmin_returnsEmptyTrue() throws Exception {
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a book by a search parameter")
    @Sql(scripts = "classpath:database/add-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/clear-books.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void search_withParams_returnsSearchedBook() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL + "/search")
                        .param("title", "Test"))
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
