package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.ServiceUser;


import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ControllerUser {
	private final ServiceUser serviceUser;


	@GetMapping
	public List<User> getListOfUser() {
		log.info("Число пользователей: {}", serviceUser.getListOfUser().size());
		return serviceUser.getListOfUser();
	}

	@PostMapping
	ResponseEntity<User> makeUser(@Valid @RequestBody User user) {
		serviceUser.addUser(serviceUser.verifyOptionsOfUser(user));
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@PutMapping
	ResponseEntity<User> userUpdate(@Valid @RequestBody User user) {
		serviceUser.renewInfoOfUser(user);
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	@PutMapping("/{id}/friends/{friendId}")
	ResponseEntity<User> joinToFriend(@PathVariable long id, @PathVariable long friendId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceUser.userFriends(id, friendId));
	}

	@DeleteMapping("/{id}/friends/{friendId}")
	ResponseEntity<User> removeFromFriends(@PathVariable long id, @PathVariable long friendId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceUser.deleteFromFriends(id, friendId));
	}

	@GetMapping("/{idOfUser}")
	ResponseEntity<User> getUser(@PathVariable long idOfUser) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceUser.getOfUser(idOfUser));
	}

	@GetMapping("/{id}/friends")
	ResponseEntity<List<User>> getListFriends(@PathVariable long id) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceUser.amountOfFriends(id));
	}

	@GetMapping("/{id}/friends/common/{otherId}")
	ResponseEntity<List<User>> getListFriends(@PathVariable long id, @PathVariable long otherId) {
		return ResponseEntity.status(HttpStatus.OK).body(serviceUser.mainFriends(id, otherId));
	}
}
