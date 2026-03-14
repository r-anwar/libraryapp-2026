package at.codersbay.libraryapp.api.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_BOOKS")
public class Book {

    @Id
    @GeneratedValue(generator = "book-sequence-generator")
    @GenericGenerator(
            name = "book-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "book_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    private String title;

    // ISO-8601
    @DateTimeFormat(
            iso = DateTimeFormat.ISO.DATE
    )
    private LocalDate publishedDate;

    @Column(unique = true, nullable = false)
    private String isbn;

    private Integer amount;

    @OneToMany(mappedBy = "book")
    @JsonIgnore
    private Set<Borrowed> borrowings = new HashSet<>();


    public Book() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Set<Borrowed> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<Borrowed> borrowings) {
        this.borrowings = borrowings;
    }
}
