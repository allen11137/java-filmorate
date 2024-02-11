package ru.yandex.practicum.filmorate.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "PERSON")
@NoArgsConstructor
public class Person {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ID")
    private int id;
    @Column(name = "EMAIL")
    private String email;
    @NotBlank
    @Column(name = "LOGIN")
    private String login;
    @Column(name = "NAME")
    private String name;
    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    public Person(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}

