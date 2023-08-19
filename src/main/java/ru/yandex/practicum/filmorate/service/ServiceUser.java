package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.request.PersonRequest;
import ru.yandex.practicum.filmorate.controller.response.PersonResponse;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.FriendExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.persistence.model.Person;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUser {
    private final UserStorage userStorage;

    public PersonResponse addUser(PersonRequest personRequest) {
        if (!isContainsUser(personRequest)) {
            Person person = userStorage.addUser(verifyOptionsOfUser(personRequest));
            log.info("User добавлен: {}", person);
            return getPersonResponse(person);
        } else {
            throw new AlreadyObjectExistsException(String.format("Пользователь %s уже был добавлен", personRequest.getName()));
        }
    }

    private PersonResponse getPersonResponse(Person person) {
        return PersonResponse.builder()
                .id(person.getId())
                .login(person.getLogin())
                .email(person.getEmail())
                .name(person.getName())
                .birthday(person.getBirthday())
                .build();
    }

    public PersonResponse renewInfoOfUser(PersonRequest personRequest) {
        Person person = verifyOptionsOfUser(personRequest);
        if (isContainsUser(personRequest)) {
            userStorage.renewInfoOfUser(person);
            log.info("Данные о пользователе обновлены: {}", personRequest);
            return getPersonResponse(person);
        } else {
            throw new NotFoundUserException(personRequest.getId());
        }
    }

    public List<Person> getUsers() {
        return new ArrayList<>(userStorage.getAll().values());
    }

    public Person getOfUser(Integer idOfUser) {
        Optional<Person> person = userStorage.getById(idOfUser);
        if (person.isPresent()) {
            log.info("Пользователь idOfUser {}", idOfUser);
            return person.get();
        } else {
            throw new NotFoundUserException(idOfUser);
        }
    }

    public Person userFriends(int idOfUser, int idOfFriend) {
        Person person = getOfUser(idOfUser);
        getOfUser(idOfFriend);

        List<Person> friends = userStorage.getAllFriends(idOfUser);
        if (friends.stream().anyMatch(f -> f.getId() == idOfFriend)) {
            throw new FriendExistException();
        }

        if (idOfUser != idOfFriend) {
            userStorage.addFriendToPerson(idOfUser, idOfFriend);
            log.info("Друг добавлен {}", idOfFriend);
            return person;
        } else {
            throw new ValidationException("Id пользователя не может совпадать с id друга");
        }
    }

    public Person deleteFromFriends(int idOfUser, int idOfFriend) {
        userStorage.removeFriend(idOfUser, idOfFriend);
        log.info("Пользователь удален из друзей {}", idOfFriend);
        return getOfUser(idOfFriend);
    }

    public List<Person> amountOfFriends(int idOfUser) {
        List<Person> friends = userStorage.getAllFriends(idOfUser);
        log.info("Список друзей пользователя {}", idOfUser);
        return friends;
    }

    public List<Person> mainFriends(int idOfUser, int idOfFriend) {
        List<Person> listOfFriend = userStorage.findAllMainFriends(idOfUser, idOfFriend);
        log.info("Список общих друзей с пользователем {}", idOfUser);
        return listOfFriend;
    }

    public Person verifyOptionsOfUser(PersonRequest personRequest) throws ValidationException {
        if (personRequest.getLogin() == null || personRequest.getLogin().isBlank()) {
            throw new ValidationException("Неправильное имя User");
        } else if (personRequest.getEmail() == null || personRequest.getEmail().isBlank() || !EmailValidator.getInstance().isValid(personRequest.getEmail())) {
            throw new ValidationException("Неправильный адрес электронной почты");
        } else if (personRequest.getBirthday() == null || personRequest.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неправильная дата рождения");
        } else if (personRequest.getName() == null || personRequest.getName().isBlank()) {
            personRequest.setName(personRequest.getLogin());
        }
        Person person = new Person();
        person.setId(personRequest.getId());
        person.setName(personRequest.getName());
        person.setEmail(personRequest.getEmail());
        person.setBirthday(personRequest.getBirthday());
        person.setLogin(personRequest.getLogin());
        return person;
    }

    public boolean isContainsUser(PersonRequest personRequest) {
        return userStorage.getAll().containsKey(personRequest.getId());
    }
}

