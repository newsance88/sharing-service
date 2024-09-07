package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.entity.User;
import ru.practicum.shareit.user.repo.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = UserMapper.toUser(userDto);
        User savedUser = userRepository.save(user);
        log.info("Сохранен user: {}", savedUser);
        return UserMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            if (!user.getEmail().equals(userDto.getEmail()) && userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(userDto.getEmail());
        }


        User updatedUser = userRepository.save(user);
        log.info("User updated: {}", updatedUser);
        return UserMapper.toUserDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
        log.info("User deleted with id: {}", id);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        log.info("User retrieved: {}", user);
        return UserMapper.toUserDto(user);
    }
}
