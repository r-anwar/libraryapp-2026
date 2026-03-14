package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.book.Borrowed;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TB_USERS")
public class User {

    @Id
    @GeneratedValue(generator = "user-sequence-generator")
    @GenericGenerator(
            name = "user-sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "user_sequence"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    private Long id;

    @Column(unique = true, nullable = false)
    private String userName;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Borrowed> borrowings = new HashSet<>();

    public User() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Borrowed> getBorrowings() {
        return borrowings;
    }

    public void setBorrowings(Set<Borrowed> borrowings) {
        this.borrowings = borrowings;
    }
}
