package ru.practicum.shareit.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.entity.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i WHERE (LOWER(i.name) LIKE LOWER(CONCAT('%', :text, '%')) OR LOWER(i.description) LIKE LOWER(CONCAT('%', :text, '%'))) AND i.available = true")
    List<Item> searchByText(String text);

    List<Item> findAllByRequest_Id(Long requestId);
}
