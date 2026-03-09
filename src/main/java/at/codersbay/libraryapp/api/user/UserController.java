package at.codersbay.libraryapp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
