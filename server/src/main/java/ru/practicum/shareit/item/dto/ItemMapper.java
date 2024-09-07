package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwner().getId());
        return itemDto;
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);
        return item;
    }

    public static ItemDtoForRequest toItemDtoForRequest(Item item) {
        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest();
        itemDtoForRequest.setId(item.getId());
        itemDtoForRequest.setName(item.getName());
        itemDtoForRequest.setRequestorId(item.getOwner().getId());
        return itemDtoForRequest;
    }

}
