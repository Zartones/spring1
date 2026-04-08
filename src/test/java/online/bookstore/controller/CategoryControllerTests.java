package online.bookstore.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import online.bookstore.dto.book.BookDto;
import online.bookstore.dto.category.CategoryDtoRequest;
import online.bookstore.dto.category.CategoryDtoResponse;
import online.bookstore.service.BookService;
import online.bookstore.service.CategoryService;
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
public class CategoryControllerTests {

    private static final String BASE_URL = "/categories";

    protected static MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private CategoryService categoryService;

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

    private CategoryDtoRequest sampleRequest() {
        CategoryDtoRequest req = new CategoryDtoRequest();
        req.setName("Test Name");
        req.setDescription("Test Description");
        return req;
    }

    private CategoryDtoResponse sampleResponse(Long id) {
        CategoryDtoResponse res = new CategoryDtoResponse();
        res.setId(id);
        res.setName("Test Name");
        res.setDescription("Test Description");
        return res;
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Add a category")
    public void createCategory_asAdmin_returns201() throws Exception {
        when(categoryService.save(any(CategoryDtoRequest.class))).thenReturn(sampleResponse(1L));

        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest())))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(201);
        assertThat(result.getResponse().getContentAsString()).contains("Test Name");
        verify(categoryService, times(1)).save(any(CategoryDtoRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories")
    public void getAll_asUser_returns200() throws Exception {
        when(categoryService.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(sampleResponse(1L))));

        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Test Name");
        verify(categoryService, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get a category by id")
    public void getCategoryById_existingId_returns200() throws Exception {
        when(categoryService.getById(1L)).thenReturn(sampleResponse(1L));

        MvcResult result = mockMvc.perform(get(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("\"id\":1");
        assertThat(result.getResponse().getContentAsString()).contains("Test Name");
        verify(categoryService, times(1)).getById(1L);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update a category")
    public void updateCategory_asAdmin_returns200() throws Exception {
        CategoryDtoResponse updated = sampleResponse(1L);
        updated.setName("Updated Name");
        when(categoryService.update(eq(1L), any(CategoryDtoRequest.class))).thenReturn(updated);

        CategoryDtoRequest req = sampleRequest();
        req.setName("Updated Name");

        MvcResult result = mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Updated Name");
        verify(categoryService, times(1)).update(eq(1L), any(CategoryDtoRequest.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete a category")
    public void deleteCategory_asAdmin_returns204() throws Exception {
        doNothing().when(categoryService).deleteById(1L);

        MvcResult result = mockMvc.perform(delete(BASE_URL + "/1"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(204);
        assertThat(result.getResponse().getContentAsString()).isEmpty();
        verify(categoryService, times(1)).deleteById(1L);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all books by a category")
    public void getBooksByCategoryId_asUser_returns200() throws Exception {
        BookDto book = new BookDto();
        book.setId(1L);
        book.setTitle("Test Title");
        when(bookService.findByCategory(1L))
                .thenReturn(new PageImpl<>(List.of(book)));

        MvcResult result = mockMvc.perform(get(BASE_URL + "/1/books"))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(200);
        assertThat(result.getResponse().getContentAsString()).contains("Test Title");
        verify(bookService, times(1)).findByCategory(1L);
    }
}
