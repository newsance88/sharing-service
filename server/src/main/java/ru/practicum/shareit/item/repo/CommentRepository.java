package ru.practicum.shareit.item.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long itemId);

    List<Comment> findAllByAuthorId(Long userId);
}
