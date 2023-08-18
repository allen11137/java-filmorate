package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
	public Map<Integer, User> amountOfUsers = new HashMap<>();
	public static final AtomicInteger idOfUser = new AtomicInteger();

	static {
		idOfUser.set(1);
	}

	@Override
	public void addUser(User user) {
		int id = idOfUser.getAndIncrement();
		user.setId(id);
		amountOfUsers.put(id, user);
	}

	@Override
	public void renewInfoOfUser(User user) {
		User updatedUser = amountOfUsers.get(user.getId());
		updatedUser.setName(user.getName() != null ? user.getName() : updatedUser.getName());
		updatedUser.setBirthday(user.getBirthday() != null ? user.getBirthday() : updatedUser.getBirthday());
		updatedUser.setLogin(user.getLogin() != null ? user.getLogin() : updatedUser.getLogin());
		updatedUser.setEmail(user.getEmail() != null ? user.getEmail() : updatedUser.getEmail());
	}

	@Override
	public void deleteToUser(User user) {
		amountOfUsers.remove(user.getId());
	}

	public Map<Integer, User> getUsers() {
		return amountOfUsers;
	}
}
