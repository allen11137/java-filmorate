package ru.yandex.practicum.filmorate.storage.User;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
	void addUser(User user);

	void renewInfoOfUser(User user);

	void deleteToUser(User user);
}
