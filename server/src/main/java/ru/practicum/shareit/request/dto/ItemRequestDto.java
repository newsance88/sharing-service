package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@NoArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
