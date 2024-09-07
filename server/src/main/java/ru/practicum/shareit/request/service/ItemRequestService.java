package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithList;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    Collection<ItemRequestDtoWithList> getUsersItemRequests(Long userId);

    Collection<ItemRequestDto> getAllItemRequests(Long userId);

    ItemRequestDtoWithList getItemRequestByRequestId(Long id);
}
