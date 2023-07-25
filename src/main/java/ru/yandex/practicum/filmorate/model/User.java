package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class User {

	private int id;

	@Email
	private String email;
	@NotBlank
	private String login;
	private String name;
	private LocalDate birthday;

	@JsonIgnore
	private Set<Long> amountIdOfFriend = new HashSet<>();

	public User(String email, String login, String name, LocalDate birthday) {
		this.email = email;
		this.login = login;
		this.name = name;
		this.birthday = birthday;
	}
}

