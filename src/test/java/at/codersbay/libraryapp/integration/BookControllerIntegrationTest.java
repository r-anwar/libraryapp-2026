package at.codersbay.libraryapp.integration;

import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.book.BookRepository;
import at.codersbay.libraryapp.api.borrowing.Borrowed;
import at.codersbay.libraryapp.api.borrowing.BorrowedRepository;
import at.codersbay.libraryapp.api.user.User;
import at.codersbay.libraryapp.api.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowedRepository borrowedRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        borrowedRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getBooks_returnsEmptyList_whenNoBooksExist() throws Exception {
        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void getBooks_returnsAllBooks() throws Exception {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("9780132350884");
        book.setAmount(2);
        bookRepository.save(book);

        mockMvc.perform(get("/api/book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Clean Code"))
                .andExpect(jsonPath("$[0].isbn").value("9780132350884"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_validRequest_returns201() throws Exception {
        String json = """
                {
                  "title": "The Pragmatic Programmer",
                  "isbn": "9780135957059",
                  "amount": 3,
                  "authors": [{"firstName": "David", "lastName": "Thomas"}]
                }
                """;

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Pragmatic Programmer"))
                .andExpect(jsonPath("$.isbn").value("9780135957059"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_missingTitle_returns400() throws Exception {
        String json = """
                {
                  "isbn": "9780135957059",
                  "amount": 3
                }
                """;

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_invalidIsbn_returns400() throws Exception {
        String json = """
                {
                  "title": "Test Book",
                  "isbn": "1234567890123",
                  "amount": 1
                }
                """;

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void borrowBook_allCopiesBorrowed_returns409() throws Exception {
        // Create user
        User user = new User();
        user.setUserName("testuser");
        user.setPassword(passwordEncoder.encode("pass"));
        user.setRole("USER");
        userRepository.save(user);

        // Create book with amount=1
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("9780132350884");
        book.setAmount(1);
        bookRepository.save(book);

        // Borrow the only copy
        Borrowed existing = new Borrowed();
        existing.setUser(user);
        existing.setBook(book);
        existing.setBorrowedDate(LocalDateTime.now());
        borrowedRepository.save(existing);

        // Attempt to borrow again — should fail
        mockMvc.perform(post("/api/book/borrow")
                        .param("userId", String.valueOf(user.getId()))
                        .param("bookId", String.valueOf(book.getId()))
                        .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic("testuser", "pass")))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
