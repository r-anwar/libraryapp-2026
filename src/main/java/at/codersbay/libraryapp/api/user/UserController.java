package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @PostMapping
    public User create(User user) {
        this.userRepository.save(user);

        return user;
    }

    @GetMapping
    public List<User> getAll() {
        return this.userRepository.findAll();
    }

    @DeleteMapping
    public void delete(@RequestParam("id") Long id) {
        this.userRepository.deleteById(id);
    }

    // id = -9999

    @PutMapping
    public ResponseBody update(User user) {

        ResponseBody responseBody = new ResponseBody();

        Optional<User> optionalUser = Optional.empty();

        if(user.getId() != null) {
            optionalUser = this.userRepository.findById(user.getId());
        } else if (user.getUserName() != null) {
            optionalUser = this.userRepository.findByUserName(user.getUserName());
        } else {
            responseBody.setMessage("id and user name was null.");
        }

        if(optionalUser.isEmpty()) {
            responseBody.setMessage("could not find user by id or user name.");
        }

        if(optionalUser.isPresent()) {
            User oldUser = optionalUser.get();

            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());

            this.userRepository.save(oldUser);
            responseBody.setMessage("successfully updated.");
        }

        return responseBody;
    }
}
