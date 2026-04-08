package online.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.book.BookSearchParameters;
import online.bookstore.dto.book.CreateBookRequestDto;
import online.bookstore.model.Category;
import online.bookstore.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTests {

    private static final String BASE_URL = "/api/books";

    protected static MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private BookService bookService;


    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    private CreateBookRequestDto sampleRequestDto() {
        CreateBookRequestDto req = new CreateBookRequestDto();
        req.setTitle("Test title");
        req.setAuthor("Test Author");
        req.setIsbn("Test Isbn");
        req.setPrice(BigDecimal.valueOf(100));
        Category category = new Category();
        category.setId(1L);
        req.setCategories(Set.of(category));
        return req;
    }

    private BookDto sampleResponseDto(Long id) {
        BookDto dto = new BookDto();
        dto.setId(id);
        dto.setTitle("Test title");
        dto.setAuthor("Test Author");
        dto.setIsbn("Test Isbn");
        dto.setPrice(BigDecimal.valueOf(100));
        return dto;
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books")
    public void getAll_asUser_returns200() throws Exception {
        when(bookService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponseDto(1L))));

        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Test title");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a book by id")
    public void getBookById_existingId_returns200() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleResponseDto(1L));

        MvcResult result = mockMvc.perform(get(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("\"id\":1");
        assertThat(result.getResponse().getContentAsString()).contains("Test title");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add a book")
    public void createBook_asAdmin_returns201() throws Exception {
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(sampleResponseDto(1L));

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDto())))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getContentAsString()).contains("\"id\":1");
        assertThat(result.getResponse().getContentAsString()).contains("Test Isbn");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a book")
    public void updateBook_asAdmin_returns200() throws Exception {
        BookDto updated = sampleResponseDto(1L);
        updated.setTitle("Updated Title");
        when(bookService.updateBook(eq(1L), any(CreateBookRequestDto.class))).thenReturn(updated);

        CreateBookRequestDto req = sampleRequestDto();
        req.setTitle("Updated Title");

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Updated Title");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a book")
    public void deleteBook_asAdmin_returns204() throws Exception {
        doNothing().when(bookService).deleteById(1L);

        MvcResult result = mockMvc.perform(delete(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a book by a search parameter")
    public void search_withParams_returns200() throws Exception {
        when(bookService.search(any(BookSearchParameters.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponseDto(1L))));

        MvcResult result = mockMvc.perform(get(BASE_URL + "/search")
                        .param("title", "Test"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Test title");
    }
}