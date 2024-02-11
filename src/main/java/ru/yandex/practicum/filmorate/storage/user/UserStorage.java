package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.persistence.model.Person;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage {
    Person addUser(Person person);

    void renewInfoOfUser(Person person);

    void deleteToUser(Person person);

    Map<Integer, Person> getAll();

    Optional<Person> getById(Integer idOfUser);

    void addFriendToPerson(int idOfUser, int idOfFriend);

    void removeFriend(int idOfUser, int idOfFriend);

    List<Person> getAllFriends(int idOfUser);

    List<Person> findAllMainFriends(int idOfUser, int idOfFriend);
}
