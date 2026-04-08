package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.author.AuthorResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class BookResponseDTO {

    private Long id;
    private String title;
    private LocalDate publishedDate;
    private String isbn;
    private int amount;
    private List<AuthorResponseDTO> authors;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getPublishedDate() { return publishedDate; }
    public void setPublishedDate(LocalDate publishedDate) { this.publishedDate = publishedDate; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public List<AuthorResponseDTO> getAuthors() { return authors; }
    public void setAuthors(List<AuthorResponseDTO> authors) { this.authors = authors; }
}
