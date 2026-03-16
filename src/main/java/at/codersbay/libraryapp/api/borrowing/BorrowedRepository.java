package at.codersbay.libraryapp.api.borrowing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// CRUD -> Create, Read, Update, Delete

public interface BorrowedRepository extends JpaRepository<Borrowed, Long> {

    public List<Borrowed> findByBookIdAndReturnDateIsNull(Long bookId);

}
