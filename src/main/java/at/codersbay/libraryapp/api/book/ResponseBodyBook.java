package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.ResponseBody;
import at.codersbay.libraryapp.api.user.User;

public class ResponseBodyBook extends ResponseBody {

    private Book book;

    public ResponseBodyBook() {

    }

    public ResponseBodyBook(Book book, String message) {
        this.book = book;
        this.message = message;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
