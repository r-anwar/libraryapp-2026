package at.codersbay.libraryapp.api.borrowing;

import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.book.BookRepository;
import at.codersbay.libraryapp.api.user.User;
import at.codersbay.libraryapp.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowingService {

    @Autowired
    private BorrowedRepository borrowedRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Borrowed borrowBook(Long userId, Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        List<Borrowed> activeBorrowings = borrowedRepository.findByBookIdAndReturnDateIsNull(bookId);
        if (activeBorrowings.size() >= book.getAmount()) {
            throw new BookNotAvailableException(
                    "All copies of '" + book.getTitle() + "' (ISBN: " + book.getIsbn() + ") are currently borrowed");
        }

        Borrowed borrowed = new Borrowed();
        borrowed.setUser(user);
        borrowed.setBook(book);
        borrowed.setBorrowedDate(LocalDateTime.now());

        return borrowedRepository.save(borrowed);
    }

    public Borrowed returnBook(Long borrowId) {
        Borrowed borrowed = borrowedRepository.findById(borrowId)
                .orElseThrow(() -> new EntityNotFoundException("Borrowing record with id " + borrowId + " not found"));

        borrowed.setReturnDate(LocalDateTime.now());
        return borrowedRepository.save(borrowed);
    }

    public List<Borrowed> getAllBorrowings() {
        return borrowedRepository.findAll();
    }
}
