package at.codersbay.libraryapp.api.borrowing;

import java.time.LocalDateTime;

public class BorrowedResponseDTO {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookIsbn;
    private Long userId;
    private String userName;
    private LocalDateTime borrowedDate;
    private LocalDateTime returnDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookIsbn() { return bookIsbn; }
    public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public LocalDateTime getBorrowedDate() { return borrowedDate; }
    public void setBorrowedDate(LocalDateTime borrowedDate) { this.borrowedDate = borrowedDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
}
