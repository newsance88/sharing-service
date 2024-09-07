package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.entity.Status;
import ru.practicum.shareit.booking.repo.BookingRepository;
import ru.practicum.shareit.exceptions.BookingException;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.item.repo.ItemRepository;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repo.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

    @Override
    public BookingDto addBooking(BookingDto bookingDto, Long userId) {
        log.info("Бронирование создается id:{}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new RuntimeException("Предмет не найден"));
        bookingDto.setStatus(Status.WAITING);
        checkBooking(bookingDto, user, item);
        checkForBookingOverlap(bookingDto, item.getId());
        Booking booking = bookingRepository.save(BookingMapper.toBooking(bookingDto, item, user));
        BookingDto savedBookingDto = BookingMapper.toBookingDto(booking);
        return savedBookingDto;
    }

    @Override
    public BookingDto approveBooking(Long bookingId, Long userId, Boolean approved) {
        log.info("Бронирование апрув id:{}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId)) {
            throw new BookingException("id mismatch");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new BookingException("Booking already approv  ed");
        }
        booking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        log.info("Получение бронирования  id:{}", bookingId);
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (!Objects.equals(booking.getItem().getOwner().getId(), userId) && !Objects.equals(booking.getBooker().getId(), userId)) {
            throw new BookingException("Mismatch with owner or booker id");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public Collection<BookingDto> getBookingsById(Long userId, BookingState bookingState) {
        log.info("Бронирование получение по айди id:{}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case CURRENT:
                bookings = bookingRepository.findByBookerAndStartBeforeAndEndAfter(user, now, now);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerAndEndBefore(user, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerAndStartAfter(user, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerAndStatus(user, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerAndStatus(user, Status.REJECTED);
                break;
            default:
                bookings = bookingRepository.findByBooker(user);
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted((b1, b2) -> b2.getStart().compareTo(b1.getStart())) // Сортировка от новых к старым
                .toList();
    }

    @Override
    public Collection<BookingDto> getBookingsByItemOwner(Long ownerId, BookingState bookingState) {
        log.info("Бронирование получение по айди овнера id:{}", ownerId);
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new RuntimeException("User not found"));

        List<Booking> bookings;
        LocalDateTime now = LocalDateTime.now();

        switch (bookingState) {
            case CURRENT:
                bookings = bookingRepository.findByItemOwnerAndStartBeforeAndEndAfter(owner, now, now);
                break;
            case PAST:
                bookings = bookingRepository.findByItemOwnerAndEndBefore(owner, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemOwnerAndStartAfter(owner, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemOwnerAndStatus(owner, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemOwnerAndStatus(owner, Status.REJECTED);
                break;
            default:
                bookings = bookingRepository.findByItemOwner(owner);
        }

        return bookings.stream()
                .map(BookingMapper::toBookingDto)
                .sorted((b1, b2) -> b2.getStart().compareTo(b1.getStart())) // Сортировка от новых к старым
                .toList();
    }


    private void checkBooking(BookingDto bookingDto, User user, Item item) {
        if (item.getOwner().getId().equals(user.getId())) {
            throw new BookingException("Нельзя бронировать собственный предмет");
        }

        if (!item.getAvailable()) {
            throw new BookingException("Предмет недоступен для бронирования");
        }

        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new BookingException("Дата начала и окончания бронирования не может быть пустой");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new BookingException("Дата начала бронирования не может быть в прошлом");
        }

        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new BookingException("Дата окончания бронирования не может быть раньше даты начала");
        }

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new BookingException("Дата начала и окончания бронирования не может совпадать");
        }
    }

    private void checkForBookingOverlap(BookingDto bookingDto, Long itemId) {
        boolean overlapExists = bookingRepository.existsByItemIdAndTimeOverlap(
                itemId,
                bookingDto.getStart(),
                bookingDto.getEnd()
        );

        if (overlapExists) {
            throw new BookingException("На этот период уже существует бронирование");
        }
    }

}
