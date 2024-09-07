package item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ContextConfiguration;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
@ContextConfiguration(classes = ShareItServer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemJsonTest {

    private final JacksonTester<ItemDto> json;

    @Test
    void testSerialize() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Item Name", "Item Description", true, null, null, null, 2L, 3L);

        var result = json.write(itemDto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Item Name");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Item Description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.ownerId").isEqualTo(3);
    }

    @Test
    void testDeserialize() throws Exception {
        String content = "{ \"id\": 1, \"name\": \"Item Name\", \"description\": \"Item Description\", \"available\": true, \"requestId\": 2, \"ownerId\": 3 }";

        ItemDto itemDto = json.parseObject(content);

        assertThat(itemDto.getId()).isEqualTo(1);
        assertThat(itemDto.getName()).isEqualTo("Item Name");
        assertThat(itemDto.getDescription()).isEqualTo("Item Description");
        assertThat(itemDto.getAvailable()).isEqualTo(true);
        assertThat(itemDto.getRequestId()).isEqualTo(2);
        assertThat(itemDto.getOwnerId()).isEqualTo(3);
    }
}
