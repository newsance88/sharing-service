package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingState;

import java.util.Collection;

public interface BookingService {
    BookingDto addBooking(BookingDto bookingDto, Long userId);

    BookingDto approveBooking(Long bookingId, Long userId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    Collection<BookingDto> getBookingsById(Long userId, BookingState bookingState);

    Collection<BookingDto> getBookingsByItemOwner(Long ownerId, BookingState bookingState);
}
