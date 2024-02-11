package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.persistence.model.Film;
import ru.yandex.practicum.filmorate.persistence.model.Rating;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ControllerFilmTest {
    private final FilmDbStorage filmDbStorage;
    private Validator validator;
    private Film film;

    @BeforeEach
    void filmValidation() {
        film = new Film("StarWars", "interesting film.",
                LocalDate.of(2005, 06, 01), 126);
        film.setMpa(new Rating(1, "G"));
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    private List<String> getMessageError(Film filmValidation) {
        Set<ConstraintViolation<Film>> breach = validator.validate(filmValidation);
        return breach
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
    }

    @Test
    void addNewFilmTest() {
        filmDbStorage.addToFilm(film);
        Map<Integer, Film> films = filmDbStorage.getListOfFilms();
        assertEquals(1, films.size(), "Неправильный список фильмов");
        assertEquals(film.getName(), films.get(1).getName(), "Неправильное название фильма");
        assertEquals(film.getDescription(), films.get(1).getDescription(), "Неправильное описание фильма");
        assertEquals(film.getReleaseDate(), films.get(1).getReleaseDate(), "Неправильная дата релиза");
        assertEquals(film.getDuration(), films.get(1).getDuration(), "Неправильная длительность");
    }


    @Test
    void createFilmNameTest() {
        film.setName("Name");
        assertEquals("Name", film.getName());
    }

    @Test
    void createFilmWrongDescriptionTest() {
        film.setDescription("Когда засуха, пыльные бури и вымирание растений " +
                "приводят человечество к продовольственному кризису,\n" + "коллектив исследователей и учёных " +
                "отправляется сквозь червоточину (которая предположительно соединяет области" +
                " пространства-времени через большое расстояние) в путешествие,\n" +
                "чтобы превзойти прежние ограничения для космических путешествий человека " +
                "и найти планету с подходящими для человечества условиями.");
        Set<ConstraintViolation<Film>> breaches = validator.validate(film);
        List<String> message = getMessageError(film);
        assertEquals(0, message.size(), "Слишком длинное описание фильма");
    }

    @Test
    void createDurationTest() {
        film.setDuration(100);
        assertEquals(100, film.getDuration());
    }


}
