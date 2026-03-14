package at.codersbay.libraryapp.api.book;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// CRUD -> Create, Read, Update, Delete

public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {

}
