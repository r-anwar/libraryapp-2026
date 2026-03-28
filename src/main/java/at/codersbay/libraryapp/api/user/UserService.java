package at.codersbay.libraryapp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        if (user.getUserName() == null || user.getUserName().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }
        if (userRepository.findByUserName(user.getUserName()).isPresent()) {
            throw new org.springframework.dao.DataIntegrityViolationException(
                    "Username '" + user.getUserName() + "' is already taken");
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, String userName, String firstName, String lastName) {
        Optional<User> optionalUser = Optional.empty();

        if (id != null) {
            optionalUser = userRepository.findById(id);
        } else if (userName != null && !userName.isBlank()) {
            optionalUser = userRepository.findByUserName(userName);
        } else {
            throw new IllegalArgumentException("id and userName were both null");
        }

        User user = optionalUser.orElseThrow(
                () -> new EntityNotFoundException("Could not find user by id or userName"));

        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);

        return userRepository.save(user);
    }
}
