package user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserJsonTest {
    private final JacksonTester<UserDto> json;

    @Test
    void testSerialize() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John Doe");
        userDto.setEmail("john.doe@mail.com");

        var result = json.write(userDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("John Doe");
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo("john.doe@mail.com");
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{ \"id\": 1, \"name\": \"John Doe\", \"email\": \"john.doe@mail.com\" }";

        UserDto userDto = json.parseObject(content);

        assertThat(userDto.getId()).isEqualTo(1);
        assertThat(userDto.getName()).isEqualTo("John Doe");
        assertThat(userDto.getEmail()).isEqualTo("john.doe@mail.com");
    }

}
