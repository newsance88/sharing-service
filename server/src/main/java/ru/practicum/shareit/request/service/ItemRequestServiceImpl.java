package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithList;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.entity.ItemRequest;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequestRepository.save(itemRequest);
        log.info("Created ItemRequest: {}", itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDtoWithList> getUsersItemRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ItemRequest> itemRequests = itemRequestRepository.findByRequestorId(userId);

        List<ItemRequestDtoWithList> itemRequestDtos = itemRequests.stream()
                .map(itemRequest -> {
                    List<ItemDtoForRequest> items = getItemsForRequest(itemRequest.getId());
                    return ItemRequestMapper.toItemRequestDtoWithList(itemRequest, items);
                })
                .toList();

        log.info("Getting requests for User: {}", userId);

        return itemRequestDtos;
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNot(userId);

        List<ItemRequestDto> itemRequestDtos = itemRequests.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
        log.info("Getting all requests");

        return itemRequestDtos;
    }

    @Override
    public ItemRequestDtoWithList getItemRequestByRequestId(Long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id).orElseThrow(() -> new RuntimeException("Request not found"));
        List<ItemDtoForRequest> items = getItemsForRequest(id);
        ItemRequestDtoWithList request = ItemRequestMapper.toItemRequestDtoWithList(itemRequest, items);
        log.info("Getting request for ItemRequest: {}", request);
        return request;
    }

    public List<ItemDtoForRequest> getItemsForRequest(Long requestId) {
        itemRequestRepository.findById(requestId).orElseThrow(() -> new RuntimeException("Request not found"));
        List<Item> list = itemRepository.findAllByRequest_Id(requestId);
        List<ItemDtoForRequest> items = itemRepository.findAllByRequest_Id(requestId).stream()
                .map(ItemMapper::toItemDtoForRequest)
                .toList();
        log.info("Fetched items: {}", items);
        return items;
    }


}
