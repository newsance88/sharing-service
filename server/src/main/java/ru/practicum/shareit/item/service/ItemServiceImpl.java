package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.entity.Comment;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repo.CommentRepository;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.request.repo.ItemRequestRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        List<CommentDto> comments = commentRepository.findAllByItemId(id).stream().map(CommentMapper::toCommentDto).toList();
        Booking lastBooking = findLastBookingForItem(id, item.getOwner().getId());
        Booking nextBooking = findNextBookingForItem(id, item.getOwner().getId());
        ItemDto itemDto = ItemMapper.toItemDto(item);
        itemDto.setComments(comments);

        if (nextBooking != null) {
            itemDto.setNextBooking(BookingMapper.toBookingDto(nextBooking));
        }
        if (lastBooking != null) {
            itemDto.setLastBooking(BookingMapper.toBookingDto(lastBooking));
        }

        log.info("Item получен: {}", item);
        return itemDto;
    }

    @Override
    public ItemDto addItem(ItemDto itemDto, Long id) {
        User owner = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = ItemMapper.toItem(itemDto, owner);
        item.setOwner(owner);
        if (itemDto.getRequestId() != null) {
            item.setRequest(itemRequestRepository.findById(itemDto.getRequestId()).orElseThrow());
        }
        Item savedItem = itemRepository.save(item);
        log.info("Item добавлен: {}", item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto, Long userId) {
        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!itemToUpdate.getOwner().getId().equals(userId)) {
            throw new RuntimeException("User not authorized to update this item");
        }
        if (itemDto.getName() != null) {
            itemToUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemToUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemToUpdate.setAvailable(itemDto.getAvailable());
        }

        Item updatedItem = itemRepository.save(itemToUpdate);
        log.info("Item обновлен: {}", updatedItem);
        return ItemMapper.toItemDto(updatedItem);
    }

    @Override
    public List<ItemDto> getUserItems(Long userId) {
        List<Item> items = itemRepository.findAllByOwnerId(userId);
        List<ItemDto> itemDtos = items.stream()
                .map(ItemMapper::toItemDto)
                .toList();
        for (ItemDto itemDto : itemDtos) {
            List<CommentDto> comments = commentRepository.findAllByItemId(itemDto.getId())
                    .stream()
                    .map(CommentMapper::toCommentDto)
                    .collect(Collectors.toList());
            itemDto.setComments(comments);
        }
        log.info("Items for user {}: {}", userId, itemDtos);
        return itemDtos;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.searchByText(text);
        log.info("Items for text '{}': {}", text, items);
        return items.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));
        List<Booking> bookings = bookingRepository.findByBookerIdAndItemAndEndBefore(userId, item, LocalDateTime.now());
        if (bookings.isEmpty()) {
            throw new BookingException("User has no completed bookings for this item");
        }
        Comment comment = CommentMapper.toComment(commentDto, item, user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("Comment added, {}", commentDto);
        return CommentMapper.toCommentDto(comment);
    }

    private Booking findLastBookingForItem(Long itemId, Long ownerId) {
        List<Booking> bookings = bookingRepository.findAllConfirmedBookingsForItem(itemId, ownerId);
        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(b -> b.getEnd().isBefore(now))
                .max(Comparator.comparing(Booking::getEnd))
                .orElse(null);
    }

    private Booking findNextBookingForItem(Long itemId, Long ownerId) {
        List<Booking> bookings = bookingRepository.findAllConfirmedBookingsForItem(itemId, ownerId);
        LocalDateTime now = LocalDateTime.now();

        return bookings.stream()
                .filter(b -> b.getStart().isAfter(now))
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
    }

}
