package ru.practicum.shareit.request.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.entity.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorId(Long userId);

    List<ItemRequest> findAllByRequestorIdNot(Long userId);
}


