package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.persistence.model.Person;
import ru.yandex.practicum.filmorate.service.ServiceUser;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class ControllerUser {
    private final ServiceUser serviceUser;

    @GetMapping
    public List<Person> getListOfUser() {
        log.info("Пользователи: {}", serviceUser.getUsers().size());
        return serviceUser.getUsers();
    }

    @PostMapping
    ResponseEntity<Person> makeUser(@RequestBody Person person) {
        serviceUser.addUser(person);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PutMapping
    ResponseEntity<Person> userUpdate(@RequestBody Person person) throws NotFoundException, ValidationException {
        serviceUser.renewInfoOfUser(person);
        return ResponseEntity.status(HttpStatus.OK).body(person);
    }

    @PutMapping("/{id}/friends/{friendId}")
    ResponseEntity<Person> joinToFriend(@PathVariable int id, @PathVariable int friendId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceUser.userFriends(id, friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    ResponseEntity<Person> removeFromFriends(@PathVariable int id, @PathVariable int friendId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceUser.deleteFromFriends(id, friendId));
    }

    @GetMapping("/{id}")
    ResponseEntity<Person> getUser(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceUser.getOfUser(id));
    }

    @GetMapping("/{id}/friends")
    ResponseEntity<List<Person>> getListFriends(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceUser.amountOfFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    ResponseEntity<List<Person>> getListFriends(@PathVariable int id, @PathVariable int otherId) {
        return ResponseEntity.status(HttpStatus.OK).body(serviceUser.mainFriends(id, otherId));
    }
}
