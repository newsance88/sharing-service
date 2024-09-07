package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.entity.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(booking.getItem().getId());
        itemDto.setName(booking.getItem().getName());
        UserDto booker = new UserDto();
        booker.setId(booking.getBooker().getId());
        booker.setName(booking.getBooker().getName());
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(booker)
                .status(booking.getStatus())
                .build();
    }

    public static Booking toBooking(BookingDto bookingDto, Item item, User booker) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }
}
