package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.persistence.model.Person;
import ru.yandex.practicum.filmorate.persistence.repository.PersonRepository;
import ru.yandex.practicum.filmorate.persistence.repository.UserJdbcRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final PersonRepository personRepository;
    private final UserJdbcRepository jdbcRepository;

    @Override
    public Person addUser(Person person) {
        return jdbcRepository.save(person);
    }

    @Override
    public void renewInfoOfUser(Person person) {
        jdbcRepository.update(person);
    }

    @Override
    public void deleteToUser(Person person) {
        jdbcRepository.delete(person);
    }

    @Override
    public Map<Integer, Person> getAll() {
        return jdbcRepository.findAll().stream()
                .collect(Collectors.toMap(Person::getId, Function.identity()));
    }

    @Override
    public Optional<Person> getById(Integer idOfUser) {
        return jdbcRepository.findById(idOfUser);
    }

    @Override
    public void addFriendToPerson(int idOfUser, int idOfFriend) {
        jdbcRepository.updatePersonFriend(idOfUser, idOfFriend);
    }

    @Override
    public void removeFriend(int idOfUser, int idOfFriend) {
        jdbcRepository.deleteFriend(idOfUser, idOfFriend);
    }

    @Override
    public List<Person> getAllFriends(int idOfUser) {
        return jdbcRepository.findAllFriends(idOfUser);
    }

    @Override
    public List<Person> findAllMainFriends(int idOfUser, int idOfFriend) {
        List<Person> friendsForFirstPerson = jdbcRepository.findAllFriends(idOfUser);
        List<Person> friendsForSecondPerson = jdbcRepository.findAllFriends(idOfFriend);
        return friendsForFirstPerson.stream()
                .filter(friendsForSecondPerson::contains)
                .collect(Collectors.toList());
    }
}
