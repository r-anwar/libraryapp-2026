package at.codersbay.libraryapp.api.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CRUD -> Create, Read, Update, Delete

public interface BookRepository extends JpaRepository<Book, Long> {

    public Optional<Book> findByIsbn(String isbn);

}
