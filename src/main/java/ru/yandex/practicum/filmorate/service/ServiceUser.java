package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.persistence.model.Person;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUser {
	private final UserStorage userStorage;

	public void addUser(Person person) {
		if (!isContainsUser(person)) {
			userStorage.addUser(verifyOptionsOfUser(person));
			log.info("User добавлен: {}", person);
		} else {
			throw new AlreadyObjectExistsException(String.format("Пользователь %s уже был добавлен", person.getName()));
		}
	}

	public void renewInfoOfUser(Person person) {
		verifyOptionsOfUser(person);
		if (isContainsUser(person)) {
			userStorage.renewInfoOfUser(person);
			log.info("Данные о пользователе обновлены: {}", person);
		} else {
			throw new NotFoundUserException(person.getId());
		}
	}

	public void deleteToUser(Person person) {
		if (isContainsUser(person)) {
			userStorage.deleteToUser(person);
		} else {
			throw new NotFoundUserException(person.getId());
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
		if ((idOfUser != idOfFriend)) {
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

	public Person verifyOptionsOfUser(Person person) throws ValidationException {
		if (person.getLogin() == null || person.getLogin().isBlank()) {
			throw new ValidationException("Неправильное имя User");
		} else if (person.getEmail().isBlank() || !EmailValidator.getInstance().isValid(person.getEmail())) {
			throw new ValidationException("Неправильный адрес электронной почты");
		} else if (person.getBirthday() == null || person.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Неправильная дата рождения");
		} else {
			if (person.getName() == null || person.getName().isBlank()) {
				person.setName(person.getLogin());
			}
			return person;
		}
	}

	public boolean isContainsUser(Person person) {
		return userStorage.getAll().containsKey(person.getId());
	}
}

