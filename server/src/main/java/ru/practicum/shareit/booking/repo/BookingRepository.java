package ru.practicum.shareit.booking.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.entity.Booking;
import ru.practicum.shareit.booking.entity.Status;
import ru.practicum.shareit.item.entity.Item;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker(User booker);

    List<Booking> findByBookerIdAndItemAndEndBefore(Long userId, Item item, LocalDateTime localDateTime);

    List<Booking> findByBookerAndStartBeforeAndEndAfter(User booker, LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerAndEndBefore(User booker, LocalDateTime end);

    List<Booking> findByBookerAndStartAfter(User booker, LocalDateTime start);

    List<Booking> findByBookerAndStatus(User booker, Status status);

    List<Booking> findByItemOwner(User owner);

    List<Booking> findByItemOwnerAndStartBeforeAndEndAfter(User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerAndEndBefore(User owner, LocalDateTime end);

    List<Booking> findByItemOwnerAndStartAfter(User owner, LocalDateTime start);

    List<Booking> findByItemOwnerAndStatus(User owner, Status status);

    @Query("SELECT b FROM Booking b WHERE b.item.id = :itemId AND b.item.owner.id = :ownerId AND b.status = 'CONFIRMED' ORDER BY b.start ASC")
    List<Booking> findAllConfirmedBookingsForItem(@Param("itemId") Long itemId, @Param("ownerId") Long ownerId);

    @Query("SELECT COUNT(b) > 0 FROM Booking b WHERE b.item.id = :itemId AND " +
            "( :start <= b.end AND :end >= b.start )")
    boolean existsByItemIdAndTimeOverlap(@Param("itemId") Long itemId,
                                         @Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

}
