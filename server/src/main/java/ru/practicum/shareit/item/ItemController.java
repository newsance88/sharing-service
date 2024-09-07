package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.addItem(itemDto, id);
    }

    @GetMapping("/{id}")
    public ItemDto getItem(@PathVariable Long id) {
        return itemService.getItem(id);
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") Long id) {
        return itemService.getUserItems(id);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(
            @PathVariable Long id,
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItem(id, itemDto, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String text) {
        return itemService.searchItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("Adding comment to user {} with id {}", userId, itemId);
        return itemService.addComment(commentDto, userId, itemId);
    }
}
