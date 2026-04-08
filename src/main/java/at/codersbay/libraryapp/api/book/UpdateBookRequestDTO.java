package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.validation.ValidIsbn;
import at.codersbay.libraryapp.api.validation.ValidUpdateRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@ValidUpdateRequest
public class UpdateBookRequestDTO {

    private Long id;

    @ValidIsbn
    private String isbn;

    private String title;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate publishedDate;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }
}
