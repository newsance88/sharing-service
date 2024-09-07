package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    @NotBlank
    private String name;
    @Email(message = "Email should be valid")
    @NotBlank
    private String email;
}
