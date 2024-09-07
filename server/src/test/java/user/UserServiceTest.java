package user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class UserServiceTest {
    private final UserService userService;
    private UserDto user;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("doe@example.com");
        user = userService.createUser(userDto);
    }

    @Test
    void testCreateUser() {
        assertNotNull(user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("doe@example.com", user.getEmail());
    }

    @Test
    void testUpdateUser() {
        UserDto updatedUserDto = new UserDto();
        updatedUserDto.setName("John Updated");
        updatedUserDto.setEmail("updated@example.com");

        UserDto updatedUser = userService.updateUser(user.getId(), updatedUserDto);

        assertEquals("John Updated", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getEmail());
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(user.getId());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUser(user.getId()));
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetUser() {
        UserDto fetchedUser = userService.getUser(user.getId());
        assertNotNull(fetchedUser);
        assertEquals(user.getId(), fetchedUser.getId());
    }
}
