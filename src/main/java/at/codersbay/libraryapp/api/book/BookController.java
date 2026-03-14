package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    BookRepository bookRepository;


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

    @PutMapping
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
}
