package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.ResponseBody;
import at.codersbay.libraryapp.api.user.User;
import at.codersbay.libraryapp.api.user.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseBodyBook> create(Book book) {
        this.bookRepository.save(book);

        return new ResponseEntity<ResponseBodyBook>(new ResponseBodyBook(book, "Successfully Created."),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAll() {

        return new ResponseEntity<>(this.bookRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseBody> delete(@RequestParam("id") Long id) {

        try {
            this.bookRepository.deleteById(id);
            return new ResponseEntity<>(new ResponseBody("Book successfully deleted."), HttpStatus.OK);
        } catch (Throwable t) {
            System.out.println(t);
        }

        return new ResponseEntity<>(new ResponseBody("Could not delete book."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping
    public ResponseEntity<ResponseBodyBook> update(Book book) {

        ResponseBodyBook responseBody = new ResponseBodyBook();

        Optional<Book> optionalBook = Optional.empty();

        String id = "";

        if (book.getId() != null) {
            optionalBook = this.bookRepository.findById(book.getId());
        } else if (!StringUtils.isEmpty(book.getIsbn())) {
            optionalBook = this.bookRepository.findByIsbn(book.getIsbn());
        } else {
            responseBody.setMessage("id and isbn was null.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        if (optionalBook.isEmpty()) {
            responseBody.setMessage("could not find book by id or isbn.");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        } else {
            Book oldBook = optionalBook.get();


            oldBook.setTitle(book.getTitle());
            oldBook.setIsbn(book.getIsbn());
            oldBook.setPublishedDate(book.getPublishedDate());

            this.bookRepository.save(oldBook);
            responseBody.setMessage("successfully updated.");
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PostMapping("/borrow")
    public ResponseEntity<ResponseBodyBorrowed> borrowBook(@RequestParam("userId") Long userId,
                                                           @RequestParam("bookId") Long bookId) {

        ResponseBodyBorrowed responseBody = new ResponseBodyBorrowed();

        if (userId == null || bookId == null) {
            responseBody.setMessage("UserId and BookId are required.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findById(userId);

        Optional<Book> optionalBook = this.bookRepository.findById(bookId);

        if (optionalUser.isEmpty() || optionalBook.isEmpty()) {
            responseBody.setMessage("Could not found user or book.");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
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
        responseBody.setMessage("successfully borrowed.");
        responseBody.setBorrowed(borrowed);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @PatchMapping("/borrow")
    public ResponseEntity<ResponseBodyBorrowed> returnBook(@RequestParam("id") Long id) {

        ResponseBodyBorrowed responseBody = new ResponseBodyBorrowed();

        if (id == null) {
            responseBody.setMessage("id is required.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }

        Optional<Borrowed> optionalBorrowed = this.borrowedRepository.findById(id);

        if (optionalBorrowed.isEmpty()) {
            responseBody.setMessage("Could not found borrowed.");
            return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
        }
        Borrowed borrowed = optionalBorrowed.get();
        borrowed.setReturnDate(LocalDateTime.now());

        try {
            this.borrowedRepository.save(borrowed);

            responseBody.setMessage("successfully returned.");
            responseBody.setBorrowed(borrowed);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        } catch(Throwable throwable) {
            System.out.println(throwable);
        }
        responseBody.setMessage("could not save borrowed return information.");
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/borrow")
    public ResponseEntity<List<Borrowed>> getAllBorrowings() {
        return new ResponseEntity<>(this.borrowedRepository.findAll(), HttpStatus.OK);
    }
}
