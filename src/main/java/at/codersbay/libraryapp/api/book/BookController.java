package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.author.Author;
import at.codersbay.libraryapp.api.borrowing.Borrowed;
import at.codersbay.libraryapp.api.borrowing.BorrowingService;
import at.codersbay.libraryapp.api.dto.LibraryMapper;
import at.codersbay.libraryapp.api.borrowing.BorrowedResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
@Tag(name = "Book", description = "Book management endpoints")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BorrowingService borrowingService;

    @Autowired
    private LibraryMapper mapper;

    @Operation(summary = "Create a new book", description = "Creates a new book with optional authors. ISBN must be a valid ISBN-13.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error (missing fields or invalid ISBN)"),
            @ApiResponse(responseCode = "409", description = "Book with this ISBN already exists")
    })
    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid CreateBookRequestDTO dto) {
        Book book = mapper.toBook(dto);
        List<Author> authors = null;
        if (dto.getAuthors() != null) {
            authors = dto.getAuthors().stream()
                    .map(mapper::toAuthor)
                    .collect(Collectors.toList());
        }
        Book saved = bookService.createBook(book, authors);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toBookResponseDTO(saved));
    }

    @Operation(summary = "Get all books", description = "Returns a list of all books in the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAll() {
        return ResponseEntity.ok(mapper.toBookResponseDTOList(bookService.getAllBooks()));
    }

    @Operation(summary = "Delete a book by ID", description = "Permanently removes a book from the library.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a book", description = "Updates title, ISBN or publishedDate of an existing book. Lookup by id or isbn.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Neither id nor isbn provided, or invalid ISBN"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @PatchMapping
    public ResponseEntity<BookResponseDTO> update(@RequestBody @Valid UpdateBookRequestDTO dto) {
        Book updated = bookService.updateBook(dto.getId(), dto.getIsbn(), dto.getTitle(), dto.getPublishedDate());
        return ResponseEntity.ok(mapper.toBookResponseDTO(updated));
    }

    @Operation(summary = "Borrow a book", description = "Creates a borrowing record for a user and a book. Fails if all copies are already borrowed.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "404", description = "User or book not found"),
            @ApiResponse(responseCode = "409", description = "All copies currently borrowed")
    })
    @PostMapping("/borrow")
    public ResponseEntity<BorrowedResponseDTO> borrowBook(@RequestParam("userId") Long userId,
                                                           @RequestParam("bookId") Long bookId) {
        Borrowed borrowed = borrowingService.borrowBook(userId, bookId);
        return ResponseEntity.ok(mapper.toBorrowedResponseDTO(borrowed));
    }

    @Operation(summary = "Return a borrowed book", description = "Sets the return date on an existing borrowing record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book returned successfully"),
            @ApiResponse(responseCode = "404", description = "Borrowing record not found")
    })
    @PatchMapping("/borrow")
    public ResponseEntity<BorrowedResponseDTO> returnBook(@RequestParam("id") Long id) {
        Borrowed borrowed = borrowingService.returnBook(id);
        return ResponseEntity.ok(mapper.toBorrowedResponseDTO(borrowed));
    }

    @Operation(summary = "Get all borrowing records", description = "Returns all borrowing records including returned books.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping("/borrow")
    public ResponseEntity<List<BorrowedResponseDTO>> getAllBorrowings() {
        return ResponseEntity.ok(mapper.toBorrowedResponseDTOList(borrowingService.getAllBorrowings()));
    }
}
