package booking;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.ShareItServer;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.entity.BookingState;
import ru.practicum.shareit.booking.entity.Status;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ShareItServer.class})
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
public class BookingServiceTest {

    private final UserController userService;
    private final ItemController itemService;
    private final BookingServiceImpl bookingService;
    private UserDto user;
    private UserDto user1;
    private ItemDto item;
    private BookingDto booking;

    @BeforeEach
    void setUp() {
        UserDto userDto = new UserDto();
        userDto.setName("John Doe");
        userDto.setEmail("doe@example.com");

        user = userService.createUser(userDto);

        UserDto userDto1 = new UserDto();
        userDto1.setName("John Doe1");
        userDto1.setEmail("doe@example1.com");

        user1 = userService.createUser(userDto1);

        ItemDto itemDto = new ItemDto();
        itemDto.setName("Item 1");
        itemDto.setDescription("Description 1");
        itemDto.setAvailable(true);

        item = itemService.createItem(itemDto, user.getId());

        BookingDto bookingDto = new BookingDto();
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
        bookingDto.setItemId(item.getId());

        booking = bookingService.addBooking(bookingDto, user1.getId());
    }

    @Test
    void testCreateBooking() {
        assertNotNull(booking.getId(), "Бронирование должно иметь идентификатор после сохранения");
        assertEquals(Status.WAITING, booking.getStatus(), "Статус нового бронирования должен быть WAITING");
    }

    @Test
    void testApproveBooking() {
        BookingDto approvedBooking = bookingService.approveBooking(booking.getId(), user.getId(), true);
        assertEquals(Status.APPROVED, approvedBooking.getStatus(), "Статус бронирования должен быть APPROVED после подтверждения");
    }

    @Test
    void testRejectBooking() {
        BookingDto rejectedBooking = bookingService.approveBooking(booking.getId(), user.getId(), false);
        assertEquals(Status.REJECTED, rejectedBooking.getStatus(), "Статус бронирования должен быть REJECTED после отклонения");
    }

    @Test
    void testGetBookingById() {
        BookingDto fetchedBooking = bookingService.getBooking(booking.getId(), user1.getId());
        assertNotNull(fetchedBooking, "Бронирование должно быть найдено по ID");
        assertEquals(booking.getId(), fetchedBooking.getId(), "ID полученного бронирования должен совпадать с исходным");
    }

    @Test
    void testGetBookingsByUserAndState() {
        List<BookingDto> bookings = (List<BookingDto>) bookingService.getBookingsById(user1.getId(), BookingState.WAITING);
        assertFalse(bookings.isEmpty(), "Список бронирований не должен быть пустым");
        assertEquals(booking.getId(), bookings.get(0).getId(), "ID бронирования должно совпадать с ожидаемым");
    }

}