package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.exception.NotFoundUserException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.User.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.User.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceUser implements UserStorage {

	private final InMemoryUserStorage userStorage;


	@Override
	public void addUser(User user) {
		if (!checkAddOfUsers(user)) {
			userStorage.addUser(user);
			log.info("User добавлен: {}", user);
		} else {
			throw new AlreadyObjectExistsException(String.format("Пользователь уже был добавлен", user.getName()));
		}
	}

	@Override
	public void renewInfoOfUser(User user) {
		if (checkAddOfUsers(user)) {
			userStorage.renewInfoOfUser(user);
			log.info("Данные о пользователе обновлены: {}", user);
		} else {
			throw new NotFoundUserException(user.getId());
		}
	}

	@Override
	public void deleteToUser(User user) {
		if (checkAddOfUsers(user)) {
			userStorage.deleteToUser(user);
		} else {
			throw new NotFoundUserException(user.getId());
		}
	}

	public List<User> getListOfUser() {
		return userStorage.getUsers();
	}

	public User getOfUser(long idOfUser) {
		log.info("Пользователь idOfUser {}", idOfUser);
		return userStorage.getUsers().stream()
				.filter(u -> u.getId() == idOfUser).findFirst()
				.orElseThrow(() -> new NotFoundUserException(idOfUser));
	}

	public User userFriends(long idOfUser, long idOfFriend) {
		User user = getOfUser(idOfUser);
		User user1 = getOfUser(idOfFriend);
		user.getAmountIdOfFriend().add(idOfFriend);
		user1.getAmountIdOfFriend().add(idOfUser);
		log.info("Друг добавлен {}", idOfFriend);
		return user;
	}

	public User deleteFromFriends(long idOfUser, long idOfFriend) {
		getOfUser(idOfUser).getAmountIdOfFriend().remove(idOfFriend);
		getOfUser(idOfFriend).getAmountIdOfFriend().remove(idOfUser);
		log.info("Пользователь удален из друзей {}", idOfFriend);
		return getOfUser(idOfFriend);
	}

	public List<User> amountOfFriends(long idOfUser) {
		List<User> listOfFriend = new ArrayList<>();
		getOfUser(idOfUser).getAmountIdOfFriend().forEach(f -> listOfFriend.add(getOfUser(f)));
		log.info("Список друзей пользователя {}", idOfUser);
		return listOfFriend;
	}

	public List<User> mainFriends(long idOfUser, long idOfFriend) {
		List<User> listOfFriend = new ArrayList<>();
		getOfUser(idOfUser).getAmountIdOfFriend().stream()
				.flatMap(g -> getOfUser(idOfFriend)
						.getAmountIdOfFriend().stream()
						.filter(a -> Objects.equals(g, a)))
				.forEach(y -> listOfFriend.add(getOfUser(y)));
		log.info("Список общих друзей с пользователем {}", idOfUser);
		return listOfFriend;
	}

	public User verifyOptionsOfUser(User user) throws ValidationException {
		if (user.getLogin() == null || user.getLogin().isBlank()) {
			throw new ValidationException("Неправильное имя User");
		} else if (user.getEmail().isBlank() || !EmailValidator.getInstance().isValid(user.getEmail())) {
			throw new ValidationException("Неправильный адрес электронной почты");
		} else if (user.getBirthday().isAfter(LocalDate.now())) {
			throw new ValidationException("Неправильная дата рождения");
		} else {
			if (user.getName() == null || user.getName().isBlank()) {
				user.setName(user.getLogin());
			}
			return user;
		}
	}


	public boolean checkAddOfUsers(User user) {
		return userStorage.amountOfUsers.stream().anyMatch(a -> a.getId() == user.getId());
	}

}

