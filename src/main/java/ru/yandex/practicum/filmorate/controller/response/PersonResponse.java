package ru.yandex.practicum.filmorate.controller.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PersonResponse {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
