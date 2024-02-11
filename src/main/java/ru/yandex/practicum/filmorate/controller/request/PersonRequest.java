package ru.yandex.practicum.filmorate.controller.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PersonRequest {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
