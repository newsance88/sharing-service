package request;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithList;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class RequestServiceTest {
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    private UserDto user;
    private UserDto user1;
    private ItemRequestDto itemRequest;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("doe@example.com");
        user = userService.createUser(userDto);

        UserDto userDto1 = new UserDto();
        userDto1.setName("Jane Doe");
        userDto1.setEmail("jane@example.com");
        user1 = userService.createUser(userDto1);

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("I need a drill");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequest = itemRequestService.createItemRequest(itemRequestDto, user.getId());
    }

    @Test
    void testCreateItemRequest() {
        assertNotNull(itemRequest.getId());
        assertEquals("I need a drill", itemRequest.getDescription());
    }

    @Test
    void testGetUsersItemRequests() {
        Collection<ItemRequestDtoWithList> requests = itemRequestService.getUsersItemRequests(user.getId());
        assertFalse(requests.isEmpty());
        assertEquals(itemRequest.getId(), requests.iterator().next().getId());
    }

    @Test
    void testGetAllItemRequests() {
        Collection<ItemRequestDto> allRequests = itemRequestService.getAllItemRequests(user1.getId());
        assertFalse(allRequests.isEmpty());
    }

    @Test
    void testGetItemRequestByRequestId() {
        ItemRequestDtoWithList fetchedRequest = itemRequestService.getItemRequestByRequestId(itemRequest.getId());
        assertNotNull(fetchedRequest);
        assertEquals(itemRequest.getId(), fetchedRequest.getId());
    }
}
