package at.codersbay.libraryapp.api.dto.request;

import javax.validation.constraints.NotBlank;

public class CreateUserRequestDTO {

    @NotBlank(message = "Username is required")
    private String userName;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Password is required")
    private String password;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
