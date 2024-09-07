package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserDto userDto) {
        log.info("Creating user: {}", userDto);
        return userClient.createUser(userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable Long id) {
        log.info("Fetching user with id: {}", id);
        return userClient.getUser(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        log.info("Updating user with id: {}, new data: {}", id, userDto);
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        log.info("Deleting user with id: {}", id);
        return userClient.deleteUser(id);
    }
}
