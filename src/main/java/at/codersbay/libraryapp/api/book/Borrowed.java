package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.user.User;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="TB_BORROWS")
public class Borrowed {

    @Id
    @GeneratedValue(generator = "borrow-sequence-generator")
    @GenericGenerator(
            name = "borrow-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "borrow_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    private Book book;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )
    private LocalDateTime borrowedDate;

    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE_TIME
    )
    private LocalDateTime returnDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }
}
