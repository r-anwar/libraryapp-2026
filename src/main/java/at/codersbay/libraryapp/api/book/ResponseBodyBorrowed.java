package at.codersbay.libraryapp.api.book;

import at.codersbay.libraryapp.api.ResponseBody;

public class ResponseBodyBorrowed extends ResponseBody {

    private Borrowed borrowed;

    public ResponseBodyBorrowed() {

    }

    public ResponseBodyBorrowed(Borrowed borrowed, String message) {
        this.borrowed = borrowed;
        this.message = message;
    }

    public Borrowed getBorrowed() {
        return borrowed;
    }

    public void setBorrowed(Borrowed borrowed) {
        this.borrowed = borrowed;
    }
}
