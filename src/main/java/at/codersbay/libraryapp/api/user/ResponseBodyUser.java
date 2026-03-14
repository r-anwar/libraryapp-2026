package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.ResponseBody;

public class ResponseBodyUser extends ResponseBody {

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
