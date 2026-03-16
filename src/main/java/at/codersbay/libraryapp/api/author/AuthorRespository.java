package at.codersbay.libraryapp.api.author;

import at.codersbay.libraryapp.api.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AuthorRespository extends JpaRepository<Author, Long> {

}
