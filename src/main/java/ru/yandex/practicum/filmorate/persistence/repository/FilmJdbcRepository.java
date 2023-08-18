package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.persistence.model.Film;
import ru.yandex.practicum.filmorate.persistence.model.Genre;
import ru.yandex.practicum.filmorate.persistence.model.Rating;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Repository
@Transactional
public class FilmJdbcRepository {
    private final AtomicInteger id = new AtomicInteger();
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public FilmJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Film> findAll() {
        String sql = """
                SELECT * FROM FILM
                """;
        List<Film> films = jdbcTemplate.query(sql, new DataClassRowMapper<>(Film.class));
        films.forEach(film -> {
            film.setMpa(getMpaByFilmId(film.getId()).orElse(new Rating(1, "G")));
            film.setGenres(getGenresByFilmId(film.getId()));
        });
        return films;
    }

    private Set<Genre> getGenresByFilmId(int id) {
        String sql = """
                SELECT * FROM GENRE g
                LEFT JOIN GENRE_FILM gf ON gf.genre_id = g.id
                WHERE gf.film_id = :id
                """;
        return new HashSet<>(jdbcTemplate.query(sql, Map.of("id", id), new DataClassRowMapper<>(Genre.class)));
    }

    private Optional<Rating> getMpaByFilmId(int id) {
        String sql = """
                SELECT * FROM RATING r
                LEFT JOIN FILM_RATING fr ON fr.rating_id = r.id
                WHERE fr.FILM_ID = :filmId
                """;
        return jdbcTemplate.query(sql, Map.of("filmId", id), new DataClassRowMapper<>(Rating.class)).stream().findFirst();
    }

    public void save(Film film) {
        id.getAndIncrement();
        film.setId(Integer.parseInt(String.valueOf(id)));
        String sqlQuery = """
                INSERT INTO FILM (name, description, releaseDate, duration, rating, id)
                VALUES (:name, :description, :releaseDate, :duration, :rating, :id)
                """;
        jdbcTemplate.update(sqlQuery, Map.of("name", film.getName(), "description", film.getDescription(), "releaseDate", film.getReleaseDate(), "duration", film.getDuration(), "rating", film.getMpa().getId(), "id", film.getId()));
        if (film.getMpa() != null) {
            String sqlQuery2 = "insert into film_rating (film_id, rating_id) values (:film_id, :rating_id)";
            jdbcTemplate.update(sqlQuery2, Map.of("film_id", film.getId(), "rating_id", film.getMpa().getId()));
        }
        if (film.getGenres() != null) {
            film.getGenres().forEach(f -> {
                String sqlQuery2 = "insert into GENRE_FILM (FILM_ID, GENRE_ID) values (:film, :genre)";
                jdbcTemplate.update(sqlQuery2, Map.of("film", film.getId(), "genre", f.getId()));
            });
        }
    }

    public Film update(Film film) {
        String sql = """
                UPDATE FILM
                SET name        = :name,
                    description = :description,
                    releaseDate = :releaseDate,
                    duration    = :duration,
                    rating      = :rating  \s
                WHERE ID = :id
                """;

        jdbcTemplate.update(sql, Map.of("name", film.getName(), "description", film.getDescription(), "releaseDate", film.getReleaseDate(), "duration", film.getDuration(), "rating", film.getMpa().getId(), "id", film.getId()));

        String sqlUpdate = "update film_rating set RATING_ID = :rating_id where FILM_ID = :film_id";
        jdbcTemplate.update(sqlUpdate, Map.of("rating_id", film.getMpa().getId(), "film_id", film.getId()));

        String sqlQueryDel = "delete from GENRE_FILM where FILM_ID = :id";
        jdbcTemplate.update(sqlQueryDel, Map.of("id", film.getId()));
        if (film.getGenres() != null) {
            film.getGenres().stream().distinct().forEach(f -> {
                String sqlQuery2 = "insert into GENRE_FILM (film_id, genre_id) values (:film , :genre)";
                jdbcTemplate.update(sqlQuery2, Map.of("film", film.getId(), "genre", f.getId()));
            });
        }
        return film;
    }

    public Optional<Film> findById(int id) {
        String sql = """
                SELECT * FROM FILM WHERE ID = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", id), new DataClassRowMapper<>(Film.class)).stream().findFirst();
    }

    public void delete(Film film) {
        String sql = """
                DELETE FROM FILM WHERE ID = :id
                """;
        jdbcTemplate.update(sql, Map.of("id", film.getId()));
    }

    public List<Film> findFavoriteFilmsWithLimit(Integer amount) {
        String sql = """
                SELECT *,
                    (SELECT count(l.*) from LIST_LIKES l
                    WHERE l.film_id = id) AS likes
                FROM FILM
                ORDER BY
                likes DESC
                LIMIT :amount
                """;
        List<Film> films = jdbcTemplate.query(sql, Map.of("amount", amount), new DataClassRowMapper<>(Film.class));
        films.forEach(film -> {
            film.setMpa(getMpaByFilmId(film.getId()).orElse(new Rating(1, "G")));
            film.setGenres(getGenresByFilmId(film.getId()));
        });
        return films;
    }

    public void addLikeToFilm(int filmId, int idOfUser) {
        String sql = """
                INSERT INTO LIST_LIKES (person_id, film_id)
                VALUES (:idOfUser, :filmId)
                """;
        jdbcTemplate.update(sql, Map.of("idOfUser", idOfUser, "filmId", filmId));
    }

    public void deleteLike(int filmId, int idOfUser) {
        String sql = """
                DELETE FROM LIST_LIKES
                WHERE person_id = :idOfUser and film_id = :filmId
                """;
        jdbcTemplate.update(sql, Map.of("idOfUser", idOfUser, "filmId", filmId));
    }

    public Rating findMpaById(Integer id) {
        String sql = "SELECT * FROM RATING WHERE ID = :id";
        return jdbcTemplate.query(sql, Map.of("id", id), new DataClassRowMapper<>(Rating.class)).stream().findFirst().orElseThrow();
    }

    public Set<Genre> findAllByIdIsIn(List<Integer> idsGenre) {
        String sql = "SELECT * FROM GENRE WHERE ID IN (:idsGenre)";
        return new HashSet<>(jdbcTemplate.query(sql, Map.of("idsGenre", idsGenre), new DataClassRowMapper<>(Genre.class)));
    }
}
