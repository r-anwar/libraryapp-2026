package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.author.Author;
import at.codersbay.libraryapp.api.author.AuthorRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRespository authorRepository;

    public Book createBook(Book book, List<Author> authors) {

        if(!authors.isEmpty()) {
            for (Author author : authors) {
                if (author == null) continue;
                authorRepository.save(author);
                book.getAuthors().add(author);
            }
        }

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.deleteById(id);
    }

    public Book updateBook(Long id, String isbn, String title, java.time.LocalDate publishedDate) {
        Optional<Book> optionalBook = Optional.empty();

        if (id != null) {
            optionalBook = bookRepository.findById(id);
        } else if (isbn != null && !isbn.isEmpty()) {
            optionalBook = bookRepository.findByIsbn(isbn);
        } else {
            throw new IllegalArgumentException("id and isbn were both null");
        }

        Book book = optionalBook.orElseThrow(
                () -> new EntityNotFoundException("Could not find book by id or isbn"));

        if (title != null) book.setTitle(title);
        if (isbn != null) book.setIsbn(isbn);
        if (publishedDate != null) book.setPublishedDate(publishedDate);

        return bookRepository.save(book);
    }
}
