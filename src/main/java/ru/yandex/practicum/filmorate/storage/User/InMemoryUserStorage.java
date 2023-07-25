package ru.yandex.practicum.filmorate.storage.User;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {


	public final Set<User> amountOfUsers = new HashSet<>();
	public static final AtomicInteger idOfUser = new AtomicInteger();

	static {
		idOfUser.set(1);
	}

	@Override
	public void addUser(User user) {
		int andIncrement = idOfUser.getAndIncrement();
		user.setId(andIncrement);
		amountOfUsers.add(user);
	}

	@Override
	public void renewInfoOfUser(User user) {
		amountOfUsers.forEach(a -> {
			if (a.getId() == user.getId()) {
				a.setName(user.getName());
				a.setBirthday(user.getBirthday());
				a.setLogin(user.getLogin());
				a.setEmail(user.getEmail());
			}
		});
	}

	@Override
	public void deleteToUser(User user) {
		amountOfUsers.remove(user);
	}

	public List<User> getUsers() {
		return new ArrayList<>(amountOfUsers);
	}
}
