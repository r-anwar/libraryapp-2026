package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserRepository userRepository;


    @PostMapping
    public ResponseEntity<ResponseBodyUser> create(User user) {

        ResponseBodyUser responseBodyUser = new ResponseBodyUser();

        if(StringUtils.isEmpty(user.getUserName())) {
            responseBodyUser.setMessage("Username is required.");
            return new ResponseEntity<>(responseBodyUser, HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = this.userRepository.findByUserName(user.getUserName());

        if(optionalUser.isPresent()) {
            responseBodyUser.setMessage("Username must be unique.");
            return new ResponseEntity<>(responseBodyUser, HttpStatus.BAD_REQUEST);
        }

        this.userRepository.save(user);


        responseBodyUser.setUser(user);
        responseBodyUser.setMessage("User created.");

        return new ResponseEntity<>(responseBodyUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(this.userRepository.findAll(), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseBody> delete(@RequestParam("id") Long id) {
        try {
            this.userRepository.deleteById(id);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Throwable t) {
            System.out.println(t);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity<ResponseBodyUser> update(User user) {

        ResponseBodyUser responseBodyUser = new ResponseBodyUser();

        Optional<User> optionalUser = Optional.empty();

        if(user.getId() != null) {
            optionalUser = this.userRepository.findById(user.getId());
        } else if (!StringUtils.isEmpty(user.getUserName())) {
            optionalUser = this.userRepository.findByUserName(user.getUserName());
        } else {
            responseBodyUser.setMessage("id and user name was null.");
            return new ResponseEntity<>(responseBodyUser, HttpStatus.BAD_REQUEST);
        }

        if(optionalUser.isEmpty()) {
            responseBodyUser.setMessage("could not find user by id or user name.");
            return new ResponseEntity<>(responseBodyUser, HttpStatus.NOT_FOUND);
        } else {
            User oldUser = optionalUser.get();

            oldUser.setFirstName(user.getFirstName());
            oldUser.setLastName(user.getLastName());

            this.userRepository.save(oldUser);
            responseBodyUser.setMessage("successfully updated.");
        }

        return new ResponseEntity<>(responseBodyUser, HttpStatus.OK);
    }
}
