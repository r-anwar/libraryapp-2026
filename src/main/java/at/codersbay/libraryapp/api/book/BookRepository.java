package at.codersbay.libraryapp.api.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

// CRUD -> Create, Read, Update, Delete

public interface BookRepository extends JpaRepository<Book, Long> {

    public Optional<Book> findByIsbn(String isbn);

    //Set<Book> findByAuthorId(Long authorId);

}
