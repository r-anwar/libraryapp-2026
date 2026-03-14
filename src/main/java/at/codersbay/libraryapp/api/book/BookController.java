package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.ResponseBody;
import at.codersbay.libraryapp.api.user.User;
import at.codersbay.libraryapp.api.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;


    @Autowired
    BorrowedRepository borrowedRepository;

    @PostMapping
    public Book create(Book book) {
        this.bookRepository.save(book);

        return book;
    }

    @GetMapping
    public List<Book> getAll() {
        return this.bookRepository.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        this.bookRepository.deleteById(id);
    }

    @PatchMapping
    public ResponseBody update(Book book) {

        ResponseBody responseBody = new ResponseBody();

        Optional<Book> optionalBook = Optional.empty();

        String id = "";

        if(book.getId() != null) {
            optionalBook = this.bookRepository.findById(book.getId());
        } else if (!StringUtils.isEmpty(book.getIsbn())) {
            optionalBook = this.bookRepository.findByIsbn(book.getIsbn());
        } else {
            responseBody.setMessage("id and isbn was null.");
            return responseBody;
        }

        if(optionalBook.isEmpty()) {
            responseBody.setMessage("could not find book by id or isbn.");
        } else {
            Book oldBook = optionalBook.get();


            oldBook.setTitle(book.getTitle());
            oldBook.setIsbn(book.getIsbn());
            oldBook.setPublishedDate(book.getPublishedDate());

            this.bookRepository.save(oldBook);
            responseBody.setMessage("successfully updated.");
        }

        return responseBody;
    }

    @PostMapping("/borrow")
    public Borrowed borrowBook(@RequestParam("userId") Long userId,
                               @RequestParam("bookId") Long bookId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        Optional<Book> optionalBook = this.bookRepository.findById(bookId);

        if(optionalUser.isEmpty() || optionalBook.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        Book book = optionalBook.get();

        Borrowed borrowed = new Borrowed();
        borrowed.setUser(user);
        user.getBorrowings().add(borrowed);

        borrowed.setBook(book);
        book.getBorrowings().add(borrowed);

        borrowed.setBorrowedDate(LocalDateTime.now());

        this.borrowedRepository.save(borrowed);
        return borrowed;
    }

    @PatchMapping("/borrow")
    public Borrowed returnBook(@RequestParam("id") Long id) {
        Optional<Borrowed>  optionalBorrowed = this.borrowedRepository.findById(id);

        if(optionalBorrowed.isPresent()) {
            Borrowed borrowed = optionalBorrowed.get();
            borrowed.setReturnDate(LocalDateTime.now());

            this.borrowedRepository.save(borrowed);
            return borrowed;
        }

        return null;
    }

    @GetMapping("/borrow")
    public List<Borrowed> getAllBorrowings() {
        return this.borrowedRepository.findAll();
    }
}
