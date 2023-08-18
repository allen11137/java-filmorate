package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.request.FilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundFilmException;
import ru.yandex.practicum.filmorate.persistence.model.Film;
import ru.yandex.practicum.filmorate.persistence.model.Genre;
import ru.yandex.practicum.filmorate.persistence.model.Rating;
import ru.yandex.practicum.filmorate.persistence.repository.FilmJdbcRepository;
import ru.yandex.practicum.filmorate.persistence.repository.FilmRepository;
import ru.yandex.practicum.filmorate.persistence.repository.GenreRepository;
import ru.yandex.practicum.filmorate.persistence.repository.RatingRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final FilmRepository filmRepository;
    private final FilmJdbcRepository jdbcRepository;

    private final RatingRepository ratingRepository;
    private final GenreRepository genreRepository;

    @Override
    public void addToFilm(Film film) {
        jdbcRepository.save(film);
    }

    @Override
    public Film updateFilm(FilmRequest filmRequest) {
        Optional<Film> optionalFilm = jdbcRepository.findById(filmRequest.getId());
        if (optionalFilm.isEmpty()) {
            throw new NotFoundFilmException(filmRequest.getId());
        }
        Set<Genre> genres = null;
        if (filmRequest.getGenres() != null) {
            genres = getGenres(filmRequest.getGenres()
                    .stream()
                    .map(FilmRequest.Genre::getId)
                    .collect(Collectors.toList())
            );
        }

        Film film = optionalFilm.get();
        film.setName(filmRequest.getName() != null ? filmRequest.getName() : film.getName());
        film.setDescription(filmRequest.getDescription() != null ? filmRequest.getDescription() : film.getDescription());
        film.setReleaseDate(filmRequest.getReleaseDate() != null ? filmRequest.getReleaseDate() : film.getReleaseDate());
        film.setDuration(filmRequest.getDuration() != null ? filmRequest.getDuration() : film.getDuration());
        film.setMpa(filmRequest.getMpa() != null ?
                getMpas(filmRequest.getMpa().getId()) : film.getMpa());
        film.setGenres(genres != null ? genres : film.getGenres());

        return jdbcRepository.update(film);
    }

    @Override
    public void deleteToFilm(Film film) {
        jdbcRepository.delete(film);
    }

    @Override
    public Map<Integer, Film> getListOfFilms() {
        List<Film> films = jdbcRepository.findAll();
        log.info("{}", films);
        return films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));
    }

    @Override
    public List<Film> getListFavoriteFilms(int amount) {
        return jdbcRepository.findFavoriteFilmsWithLimit(amount);
    }

    @Override
    public void addLikeToFilm(int filmId, int idOfUser) {
        jdbcRepository.addLikeToFilm(filmId, idOfUser);
    }

    @Override
    public void removeLike(int filmId, int idOfUser) {
        jdbcRepository.deleteLike(filmId, idOfUser);
    }

    @Override
    public Rating getMpas(Integer mpa) {
        return jdbcRepository.findMpaById(mpa);
    }

    @Override
    public Set<Genre> getGenres(List<Integer> idsGenre) {
        return jdbcRepository.findAllByIdIsIn(idsGenre);
    }
}
