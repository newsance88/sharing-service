package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public ItemDto getItem(Long id);

    public ItemDto addItem(ItemDto item, Long id);

    public ItemDto updateItem(Long id, ItemDto itemDto, Long userId);

    List<ItemDto> getUserItems(Long userId);

    List<ItemDto> searchItems(String text);

    CommentDto addComment(CommentDto commentDto, Long userId, Long itemId);

}
