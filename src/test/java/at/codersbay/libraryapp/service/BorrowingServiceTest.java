package at.codersbay.libraryapp.service;

import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.book.BookRepository;
import at.codersbay.libraryapp.api.borrowing.BookNotAvailableException;
import at.codersbay.libraryapp.api.borrowing.Borrowed;
import at.codersbay.libraryapp.api.borrowing.BorrowedRepository;
import at.codersbay.libraryapp.api.borrowing.BorrowingService;
import at.codersbay.libraryapp.api.user.User;
import at.codersbay.libraryapp.api.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowingServiceTest {

    @Mock
    private BorrowedRepository borrowedRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BorrowingService borrowingService;

    // -----------------------------------------------------------------------
    // borrowBook
    // -----------------------------------------------------------------------

    @Test
    void borrowBook_validUserAndBook_returnsBorrowedRecord() {
        User user = new User();
        user.setId(1L);
        user.setUserName("max");

        Book book = new Book();
        book.setId(10L);
        book.setTitle("Clean Code");
        book.setAmount(2);

        Borrowed saved = new Borrowed();
        saved.setId(100L);
        saved.setUser(user);
        saved.setBook(book);
        saved.setBorrowedDate(LocalDateTime.now());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(borrowedRepository.findByBookIdAndReturnDateIsNull(10L)).thenReturn(List.of());
        when(borrowedRepository.save(any(Borrowed.class))).thenReturn(saved);

        Borrowed result = borrowingService.borrowBook(1L, 10L);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getUser().getUserName()).isEqualTo("max");
        verify(borrowedRepository, times(1)).save(any(Borrowed.class));
    }

    @Test
    void borrowBook_userNotFound_throwsEntityNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.borrowBook(99L, 10L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void borrowBook_bookNotFound_throwsEntityNotFoundException() {
        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.borrowBook(1L, 99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void borrowBook_allCopiesBorrowed_throwsBookNotAvailableException() {
        User user = new User();
        user.setId(1L);

        Book book = new Book();
        book.setId(10L);
        book.setTitle("The Pragmatic Programmer");
        book.setAmount(1);

        Borrowed existing = new Borrowed();
        existing.setBook(book);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(borrowedRepository.findByBookIdAndReturnDateIsNull(10L)).thenReturn(List.of(existing));

        assertThatThrownBy(() -> borrowingService.borrowBook(1L, 10L))
                .isInstanceOf(BookNotAvailableException.class)
                .hasMessageContaining("The Pragmatic Programmer");
    }

    // -----------------------------------------------------------------------
    // returnBook
    // -----------------------------------------------------------------------

    @Test
    void returnBook_existingBorrowing_setsReturnDate() {
        Borrowed borrowed = new Borrowed();
        borrowed.setId(1L);
        borrowed.setBorrowedDate(LocalDateTime.now().minusDays(3));

        when(borrowedRepository.findById(1L)).thenReturn(Optional.of(borrowed));
        when(borrowedRepository.save(any(Borrowed.class))).thenAnswer(inv -> inv.getArgument(0));

        Borrowed result = borrowingService.returnBook(1L);

        assertThat(result.getReturnDate()).isNotNull();
        verify(borrowedRepository).save(borrowed);
    }

    @Test
    void returnBook_nonExistingId_throwsEntityNotFoundException() {
        when(borrowedRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowingService.returnBook(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }
}
