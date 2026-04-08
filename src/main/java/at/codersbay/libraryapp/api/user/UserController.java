package at.codersbay.libraryapp.api.user;

import at.codersbay.libraryapp.api.dto.LibraryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "User management endpoints")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LibraryMapper mapper;

    @Operation(summary = "Register a new user", description = "Creates a new user account. Username must be unique. Password will be stored hashed.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error (missing username or password)"),
            @ApiResponse(responseCode = "409", description = "Username already taken")
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@RequestBody @Valid CreateUserRequestDTO dto) {
        User user = mapper.toUser(dto);
        User saved = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUserResponseDTO(saved));
    }

    @Operation(summary = "Get all users", description = "Returns a list of all registered users. Passwords are never included.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List returned successfully")
    })
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        return ResponseEntity.ok(mapper.toUserResponseDTOList(userService.getAllUsers()));
    }

    @Operation(summary = "Delete a user by ID", description = "Permanently removes a user from the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a user", description = "Updates firstName and lastName of an existing user. Lookup by id or userName.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Neither id nor userName provided"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestBody UpdateUserRequestDTO dto) {
        User updated = userService.updateUser(dto.getId(), dto.getUserName(), dto.getFirstName(), dto.getLastName());
        return ResponseEntity.ok(mapper.toUserResponseDTO(updated));
    }
}
