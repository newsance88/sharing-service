package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithList;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestDto itemRequestDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public Collection<ItemRequestDtoWithList> getRequestsFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getUsersItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllItemRequests(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithList getItemRequest(@PathVariable Long requestId) {
        return itemRequestService.getItemRequestByRequestId(requestId);
    }
}
