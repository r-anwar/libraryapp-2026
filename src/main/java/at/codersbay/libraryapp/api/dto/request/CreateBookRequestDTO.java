package at.codersbay.libraryapp.api.dto.request;

import at.codersbay.libraryapp.api.validation.ValidIsbn;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class CreateBookRequestDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate publishedDate;

    @NotBlank(message = "ISBN is required")
    @ValidIsbn
    private String isbn;

    @NotNull(message = "Amount is required")
    @Min(value = 1, message = "Amount must be at least 1")
    private Integer amount;

    private List<AuthorRequestDTO> authors;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public List<AuthorRequestDTO> getAuthors() { return authors; }
    public void setAuthors(List<AuthorRequestDTO> authors) { this.authors = authors; }
}
