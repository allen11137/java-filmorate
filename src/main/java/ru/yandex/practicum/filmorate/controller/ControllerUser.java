package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ServiceUser;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ControllerUser {
	private final ServiceUser serviceUser;

	@GetMapping
	public List<User> getListOfUser() {
		log.info("Список пользователей: {}", serviceUser.amountOfUsers.size());
		return new ArrayList<>(serviceUser.amountOfUsers);
	}

	@PostMapping
	ResponseEntity<User> makeUser(@Valid @RequestBody User user) throws AlreadyObjectExistsException, ValidationException {
		if (!serviceUser.amountOfUsers.contains(user)) {
			log.info("Пользователь добавлен: {}", user);
			serviceUser.addUser(serviceUser.verifyOptionsOfUser(user));
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			throw new AlreadyObjectExistsException("Пользователь уже был добавлен" + user.getEmail());
		}
	}

	@PutMapping
	ResponseEntity<User> userUpdate(@Valid @RequestBody User user) throws NotFoundException, ValidationException {
		if (serviceUser.checkAddOfUsers(user)) {
			log.info("Информация обновлена: {}", user);
			serviceUser.renewInfoOfUser(serviceUser.verifyOptionsOfUser(user));
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			throw new NotFoundException("Пользователь не найден с id: " + user.getId());
		}
	}
}
