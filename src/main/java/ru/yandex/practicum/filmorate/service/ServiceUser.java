package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class ServiceUser {
	public final Set<User> amountOfUsers = new HashSet<>();
	public final AtomicInteger idOfUser = new AtomicInteger();

	public User verifyOptionsOfUser(User user) throws ValidationException {
		if (user.getLogin().isBlank()) {
			throw new ValidationException("Неправильное имя User");
		} else if (user.getEmail().isBlank() || !EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new ValidationException("Неправильный адрес электронной почты");
		} else if (user.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Неправильная дата рождения");
		} else {
			return user;
		}
	}

	public void renewInfoOfUser(User user) {
		amountOfUsers.forEach(a -> {
			if (a.getId() == user.getId()) {
				a.setName(user.getName());
				a.setBirthday(user.getBirthday());
				a.setLogin(user.getLogin());
				a.setEmail(user.getEmail());
			}
			log.info("Информация о user обновлена: {}", user);
		});
	}

	public void addUser(User user) {
		idOfUser.getAndIncrement();
		user.setId(idOfUser.get());
		log.info("User добавлен: {}", user);
		if (user.getName() == null || user.getName().isBlank()) {
			user.setName(user.getLogin());
		}
		amountOfUsers.add(user);
	}

	public boolean checkAddOfUsers(User user) {
		return amountOfUsers.stream().anyMatch(a -> a.getId() == user.getId());
	}

}
