package item;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class ItemServiceTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private UserDto user;
    private ItemDto item;

    @BeforeEach
    void setUp() {

        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("doe@example.com");
        user = userService.createUser(userDto);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        itemDto.setAvailable(true);
        item = itemService.addItem(itemDto, user.getId());
    }

    @Test
    void testAddItem() {
        assertNotNull(item.getId());
        assertEquals("Item 1", item.getName());
    }

    @Test
    void testGetItem() {
        ItemDto fetchedItem = itemService.getItem(item.getId());
        assertNotNull(fetchedItem);
        assertEquals(item.getId(), fetchedItem.getId());
    }

    @Test
    void testUpdateItem() {
        ItemDto updateDto = new ItemDto();
        updateDto.setName("Updated Item");
        updateDto.setDescription("Updated Description");
        ItemDto updatedItem = itemService.updateItem(item.getId(), updateDto, user.getId());

        assertEquals("Updated Item", updatedItem.getName());
        assertEquals("Updated Description", updatedItem.getDescription());
    }

    @Test
    void testGetUserItems() {
        List<ItemDto> userItems = itemService.getUserItems(user.getId());
        assertFalse(userItems.isEmpty());
        assertEquals(item.getId(), userItems.get(0).getId());
    }

    @Test
    void testSearchItems() {
        List<ItemDto> foundItems = itemService.searchItems("Item 1");
        assertFalse(foundItems.isEmpty());
        assertEquals(item.getId(), foundItems.get(0).getId());
    }
}
