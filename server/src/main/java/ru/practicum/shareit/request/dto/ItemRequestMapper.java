package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.user.entity.User;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestor(itemRequest.getRequestor());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public static ItemRequestDtoWithList toItemRequestDtoWithList(ItemRequest itemRequest, List<ItemDtoForRequest> items) {
        ItemRequestDtoWithList itemRequestDtoWithList = new ItemRequestDtoWithList();
        itemRequestDtoWithList.setId(itemRequest.getId());
        itemRequestDtoWithList.setDescription(itemRequest.getDescription());
        itemRequestDtoWithList.setCreated(itemRequest.getCreated());
        itemRequestDtoWithList.setItems(items);
        return itemRequestDtoWithList;
    }

    public static ItemRequest toItemRequestFromDtoWithList(ItemRequestDtoWithList itemRequestDtoWithList, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDtoWithList.getId());
        itemRequest.setDescription(itemRequestDtoWithList.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(itemRequestDtoWithList.getCreated());
        return itemRequest;
    }
}
