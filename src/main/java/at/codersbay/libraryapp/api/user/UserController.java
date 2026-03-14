package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.ResponseBody;
import org.apache.commons.lang3.StringUtils;
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

    @PutMapping
    public ResponseBody update(User user) {

        ResponseBody responseBody = new ResponseBody();

        Optional<User> optionalUser = Optional.empty();

        if(user.getId() != null) {
            optionalUser = this.userRepository.findById(user.getId());
        } else if (!StringUtils.isEmpty(user.getUserName())) {
            optionalUser = this.userRepository.findByUserName(user.getUserName());
        } else {
            responseBody.setMessage("id and user name was null.");
            return responseBody;
        }

        if(optionalUser.isEmpty()) {
            responseBody.setMessage("could not find user by id or user name.");
        } else {
            User oldUser = optionalUser.get();

            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());

            this.userRepository.save(oldUser);
            responseBody.setMessage("successfully updated.");
        }

        return responseBody;
    }
}
