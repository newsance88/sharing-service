package ru.practicum.shareit.request.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.practicum.shareit.user.entity.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@Entity
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;
    @Column(name = "created")
    private LocalDateTime created;
}
