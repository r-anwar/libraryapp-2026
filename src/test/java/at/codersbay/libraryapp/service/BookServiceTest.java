package at.codersbay.libraryapp.service;

import at.codersbay.libraryapp.api.author.Author;
import at.codersbay.libraryapp.api.author.AuthorRespository;
import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.book.BookRepository;
import at.codersbay.libraryapp.api.book.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRespository authorRepository;

    @InjectMocks
    private BookService bookService;

    // -----------------------------------------------------------------------
    // createBook – Happy Path
    // -----------------------------------------------------------------------

    @Test
    void createBook_validInputWithAuthor_returnsCreatedBook() {
        Book book = new Book();
        book.setTitle("Clean Code");
        book.setIsbn("9780132350884");
        book.setAmount(3);

        Author author = new Author();
        author.setFirstName("Robert");
        author.setLastName("Martin");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(book, List.of(author));

        assertThat(result.getTitle()).isEqualTo("Clean Code");
        verify(authorRepository, times(1)).save(author);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_withMultipleAuthors_savesAllAuthors() {
        Book book = new Book();
        book.setTitle("The Pragmatic Programmer");
        book.setIsbn("9780135957059");
        book.setAmount(2);

        Author author1 = new Author();
        author1.setFirstName("David");
        author1.setLastName("Thomas");

        Author author2 = new Author();
        author2.setFirstName("Andrew");
        author2.setLastName("Hunt");

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        bookService.createBook(book, List.of(author1, author2));

        verify(authorRepository, times(2)).save(any(Author.class));
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void createBook_emptyAuthorList_savesBookWithoutAuthors() {
        Book book = new Book();
        book.setTitle("No Author Book");
        book.setIsbn("9780132350884");
        book.setAmount(1);

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.createBook(book, List.of());

        assertThat(result.getTitle()).isEqualTo("No Author Book");
        verify(authorRepository, never()).save(any());
        verify(bookRepository, times(1)).save(book);
    }

    // -----------------------------------------------------------------------
    // deleteBook
    // -----------------------------------------------------------------------

    @Test
    void deleteBook_existingId_deletesSuccessfully() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_nonExistingId_throwsEntityNotFoundException() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> bookService.deleteBook(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // -----------------------------------------------------------------------
    // updateBook
    // -----------------------------------------------------------------------

    @Test
    void updateBook_existingId_updatesAndReturnsBook() {
        Book existing = new Book();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setIsbn("9780132350884");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book result = bookService.updateBook(1L, null, "New Title", LocalDate.of(2024, 1, 1));

        assertThat(result.getTitle()).isEqualTo("New Title");
        verify(bookRepository).save(existing);
    }

    @Test
    void updateBook_nonExistingId_throwsEntityNotFoundException() {
        when(bookRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(42L, null, "Title", null))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateBook_noIdAndNoIsbn_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> bookService.updateBook(null, null, "Title", null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
