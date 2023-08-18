package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.persistence.model.Person;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ControllerUserTest {
    protected Person person;
    private final UserDbStorage userStorage;

    @BeforeEach
    public void personValidation() {
        person = new Person("lilia@yandex.ru", "lilia", "Liliya", LocalDate.of(2003, 9, 11));
        userStorage.addUser(person);
    }

    @Test
    void validatePerson() {
        Map<Integer, Person> personList = userStorage.getAll();

        assertEquals(1, personList.size(), "Неправильный список user");
        assertEquals(person.getName(), personList.get(person.getId()).getName(), "Неправильное имя");
        assertEquals(person.getEmail(), personList.get(person.getId()).getEmail(), "Неправильный адрес электронной почты");
        assertEquals(person.getBirthday(), personList.get(person.getId()).getBirthday(), "Неправильая дата рождения");
        assertEquals(person.getLogin(), personList.get(person.getId()).getLogin(), "Логин не совпадают");
    }

    @Test
    void testUpdate() {
        person.setLogin("Alina");
        userStorage.renewInfoOfUser(person);

        Optional<Person> person = userStorage.getById(this.person.getId());
        assertEquals("Alina", person.get().getLogin());
    }

    @Test
    void newUserWrongLoginTest() {
        person.setBirthday(LocalDate.now().plusYears(3));

        userStorage.renewInfoOfUser(person);
        Optional<Person> person1 = userStorage.getById(person.getId());
        assertEquals(LocalDate.now().plusYears(3), person1.get().getBirthday());
    }

    @Test
    void newUserWrongEmailTest() {
        person.setEmail("alisaalisa.yandex.ru");

        userStorage.renewInfoOfUser(person);
        Optional<Person> person1 = userStorage.getById(person.getId());
        assertEquals("alisaalisa.yandex.ru", person1.get().getEmail());
    }

    @AfterEach
    void finish() {
        userStorage.deleteToUser(person);
    }
}
