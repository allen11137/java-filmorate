package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import lombok.Data;
import jakarta.validation.constraints.Email;

import java.time.LocalDate;


@Data
public class User {

	@Min(1)
	private int id;

	@Email
	private String email;
	private String login;
	private String name;
	private LocalDate birthday;

	public User(String email, String login, String name, LocalDate birthday) {
		this.email = email;
		this.login = login;
		this.name = name;
		this.birthday = birthday;
	}
}
