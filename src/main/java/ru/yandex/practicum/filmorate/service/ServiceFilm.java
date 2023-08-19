package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.request.FilmRequest;
import ru.yandex.practicum.filmorate.exception.AlreadyObjectExistsException;
import ru.yandex.practicum.filmorate.exception.LikeExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundFilmException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.persistence.model.Film;
import ru.yandex.practicum.filmorate.persistence.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceFilm {
    public static final LocalDate FIRST_DATE_OF_RELEASE = LocalDate.of(1895, 12, 28);
    public static final int SIZE_OF_DESCRIPTION = 200;

    private final FilmStorage filmStorage;
    private final ServiceUser serviceUser;

    public void addToFilm(Film film) {
        if (!verifyOptionsOfFilmByIdOrName(film.getId(), film.getName())) {
            filmStorage.addToFilm(film);
            log.info("Фильм добавлен: {}", film);
        } else {
            throw new AlreadyObjectExistsException(String.format("Фильм уже %s был добавлен", film.getName()));
        }
    }

    public Film updateFilm(FilmRequest filmRequest) {
        if (verifyOptionsOfFilm(filmRequest.getId())) {
            Film film = filmStorage.updateFilm(filmRequest);
            log.info("База фильмов обновлена: {}", film.getName());
            return film;
        } else {
            throw new NotFoundFilmException(filmRequest.getId());
        }
    }

    public void deleteToFilm(Film film) {
        if (verifyOptionsOfFilm(film.getId())) {
            filmStorage.deleteToFilm(film);
        } else {
            throw new NotFoundFilmException(film.getId());
        }
    }

    public Map<Integer, Film> getListOfFilms() {
        return filmStorage.getListOfFilms();
    }

    public Film getOfIdFilm(int id) {
        if (getListOfFilms().containsKey(id)) {
            return getListOfFilms().get(id);
        } else {
            throw new NotFoundFilmException(id);
        }
    }

    public boolean verifyOptionsOfFilm(Integer id) {
        return getListOfFilms().containsKey(id);
    }

    public boolean verifyOptionsOfFilmByIdOrName(Integer id, String name) {
        Map<Integer, Film> films = getListOfFilms();
        if (films.containsKey(id)) {
            return true;
        }

        return films.values().stream().anyMatch(f -> f.getName().equals(name));
    }

    public List<Film> getListOfLovelyFilms(int amount) {
        List<Film> fs = filmStorage.getListFavoriteFilms(amount);
        log.info("getListOfLovelyFilms {}", fs);
        return fs;
    }

    public Film joinLikeToFilm(int filmId, int idOfUser) {
        serviceUser.getOfUser(idOfUser);
        getOfIdFilm(filmId);
        isExistLike(filmId, idOfUser);
        filmStorage.addLikeToFilm(filmId, idOfUser);
        log.info("joinLikeToFilm {}", getOfIdFilm(filmId));
        return getOfIdFilm(filmId);
    }

    private void isExistLike(int filmId, int idOfUser) {
        boolean exist = filmStorage.isExistLike(filmId, idOfUser);
        if (exist) {
            throw new LikeExistException();
        }
    }

    public Film deleteToLike(int filmId, int idOfUser) {
        serviceUser.getOfUser(idOfUser);
        filmStorage.removeLike(filmId, idOfUser);
        log.info("Отметка мне нравится удалена filmId {}, idOfUser {}", filmId, idOfUser);
        return getOfIdFilm(filmId);
    }

    public Film verifyParametrOfFilm(FilmRequest film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Имя не должно быть пустым");
        } else if (film.getDescription().length() > SIZE_OF_DESCRIPTION) {
            throw new ValidationException("Слишком длинное описание, максимальное количество символов 200");
        } else if (film.getReleaseDate().isBefore(FIRST_DATE_OF_RELEASE)) {
            throw new ValidationException("Неправильная дата релиза фильма.");
        } else if (film.getDuration() < 1) {
            throw new ValidationException("Неправильная длительность фильма");
        }
        return mappingFilmRequestToFilm(film);
    }

    private Film mappingFilmRequestToFilm(FilmRequest filmRequest) {
        Set<Genre> genres = null;
        if (filmRequest.getGenres() != null) {
            genres = filmStorage.getGenres(filmRequest.getGenres()
                    .stream()
                    .map(FilmRequest.Genre::getId)
                    .collect(Collectors.toList()));
        }

        Film film = new Film();
        film.setName(filmRequest.getName());
        film.setDescription(filmRequest.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate());
        film.setDuration(filmRequest.getDuration());
        film.setMpa(filmStorage.getMpas(filmRequest.getMpa().getId()));
        film.setGenres(genres);
        return film;
    }

}
