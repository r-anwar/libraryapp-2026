package at.codersbay.libraryapp.api.dto;

import at.codersbay.libraryapp.api.author.Author;
import at.codersbay.libraryapp.api.book.Book;
import at.codersbay.libraryapp.api.borrowing.Borrowed;
import at.codersbay.libraryapp.api.author.AuthorRequestDTO;
import at.codersbay.libraryapp.api.book.CreateBookRequestDTO;
import at.codersbay.libraryapp.api.user.CreateUserRequestDTO;
import at.codersbay.libraryapp.api.author.AuthorResponseDTO;
import at.codersbay.libraryapp.api.book.BookResponseDTO;
import at.codersbay.libraryapp.api.borrowing.BorrowedResponseDTO;
import at.codersbay.libraryapp.api.user.UserResponseDTO;
import at.codersbay.libraryapp.api.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LibraryMapper {

    public BookResponseDTO toBookResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setPublishedDate(book.getPublishedDate());
        dto.setIsbn(book.getIsbn());
        dto.setAmount(book.getAmount());
        if (book.getAuthors() != null) {
            dto.setAuthors(book.getAuthors().stream()
                    .map(this::toAuthorResponseDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    public List<BookResponseDTO> toBookResponseDTOList(List<Book> books) {
        return books.stream().map(this::toBookResponseDTO).collect(Collectors.toList());
    }

    public AuthorResponseDTO toAuthorResponseDTO(Author author) {
        AuthorResponseDTO dto = new AuthorResponseDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        return dto;
    }

    public Author toAuthor(AuthorRequestDTO dto) {
        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        return author;
    }

    public Book toBook(CreateBookRequestDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setIsbn(dto.getIsbn());
        book.setPublishedDate(dto.getPublishedDate());
        book.setAmount(dto.getAmount());
        return book;
    }

    public UserResponseDTO toUserResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        // password is intentionally not mapped
        return dto;
    }

    public List<UserResponseDTO> toUserResponseDTOList(List<User> users) {
        return users.stream().map(this::toUserResponseDTO).collect(Collectors.toList());
    }

    public User toUser(CreateUserRequestDTO dto) {
        User user = new User();
        user.setUserName(dto.getUserName());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPassword(dto.getPassword());
        return user;
    }

    public BorrowedResponseDTO toBorrowedResponseDTO(Borrowed borrowed) {
        BorrowedResponseDTO dto = new BorrowedResponseDTO();
        dto.setId(borrowed.getId());
        dto.setBorrowedDate(borrowed.getBorrowedDate());
        dto.setReturnDate(borrowed.getReturnDate());
        if (borrowed.getBook() != null) {
            dto.setBookId(borrowed.getBook().getId());
            dto.setBookTitle(borrowed.getBook().getTitle());
            dto.setBookIsbn(borrowed.getBook().getIsbn());
        }
        if (borrowed.getUser() != null) {
            dto.setUserId(borrowed.getUser().getId());
            dto.setUserName(borrowed.getUser().getUserName());
        }
        return dto;
    }

    public List<BorrowedResponseDTO> toBorrowedResponseDTOList(List<Borrowed> borrowings) {
        return borrowings.stream().map(this::toBorrowedResponseDTO).collect(Collectors.toList());
    }
}
