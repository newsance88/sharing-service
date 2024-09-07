package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestBody @Valid BookingDto bookingDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        BookingDto bookingDtoSaved = bookingService.addBooking(bookingDto, userId);
        log.info("creating booking: {}", bookingDtoSaved);
        return bookingDtoSaved;
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable Long bookingId,
                                     @RequestParam Boolean approved,
                                     @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBooking(@PathVariable Long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBooking(bookingId, userId);
    }

    @GetMapping
    public Collection<BookingDto> getBookings(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingsById(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getBookingsByOwner(@RequestParam(required = false, defaultValue = "ALL") BookingState state,
                                                     @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return bookingService.getBookingsByItemOwner(ownerId, state);
    }

}
