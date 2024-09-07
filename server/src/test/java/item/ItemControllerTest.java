package item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {ItemController.class})
public class ItemControllerTest {
    private final ObjectMapper mapper;
    private final MockMvc mvc;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        itemDto.setName("ItemName");
        itemDto.setDescription("ItemDescription");
        itemDto.setAvailable(true);
    }

    @Test
    void createItem() throws Exception {
        when(itemService.addItem(any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void getItem() throws Exception {
        when(itemService.getItem(anyLong()))
                .thenReturn(itemDto);

        mvc.perform(get("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void getUserItems() throws Exception {
        when(itemService.getUserItems(anyLong()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(anyLong(), any(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    void searchItems() throws Exception {
        when(itemService.searchItems(any()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get("/items/search")
                        .param("text", "ItemName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class));
    }

    @Test
    void addComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");

        when(itemService.addComment(any(), anyLong(), anyLong()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class));
    }
}
