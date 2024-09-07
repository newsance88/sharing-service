package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId, @RequestBody @Valid ItemDto itemDto) {
        log.info("Creating item for user {} with data {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        log.info("Fetching item with id {} for user {}", id, userId);
        return itemClient.getItem(userId, id);
    }

    @GetMapping
    public ResponseEntity<Object> getUserItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Fetching all items for user {}", userId);
        return itemClient.getUserItems(userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id,
            @RequestBody ItemDto itemDto) {
        log.info("Updating item with id {} for user {}, new data: {}", id, userId, itemDto);
        return itemClient.updateItem(userId, id, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @RequestParam String text) {
        log.info("Searching items with text: {}", text);
        return itemClient.searchItems(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long itemId,
            @RequestBody @Valid CommentDto commentDto) {
        log.info("Adding comment for item {} by user {}", itemId, userId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
