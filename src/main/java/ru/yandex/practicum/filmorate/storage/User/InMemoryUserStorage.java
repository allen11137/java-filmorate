package ru.yandex.practicum.filmorate.storage.User;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {


	public final Map<Integer, User> amountOfUsers = new HashMap<>();
	public static final AtomicInteger idOfUser = new AtomicInteger();

	static {
		idOfUser.set(1);
	}

	@Override
	public void addUser(User user) {
		int andIncrement = idOfUser.getAndIncrement();
		user.setId(andIncrement);
		amountOfUsers.put(andIncrement, user);
	}

	@Override
	public void renewInfoOfUser(User user) {
		if (amountOfUsers.containsKey(user.getId())) {
			user.setName(user.getName());
			user.setBirthday(user.getBirthday());
			user.setLogin(user.getLogin());
			user.setEmail(user.getEmail());
		}
	}

	@Override
	public void deleteToUser(User user) {
		amountOfUsers.remove(user);
	}

	public List<User> getUsers() {
		return new ArrayList<>(amountOfUsers.values());
	}
}
