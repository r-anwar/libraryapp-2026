package at.codersbay.libraryapp.api;

public class ResponseBody {

    public String message;

    public ResponseBody() {}

    public ResponseBody(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
