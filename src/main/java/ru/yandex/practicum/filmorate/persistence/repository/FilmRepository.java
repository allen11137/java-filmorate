package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.persistence.model.Film;

import javax.transaction.Transactional;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    @Query(value = """
            SELECT *, 
                (SELECT count(l.*) from LIST_LIKES l 
                WHERE l.film_id = id) AS likes 
            FROM FILM 
            ORDER BY 
            likes DESC 
            LIMIT :amount
            """,
            nativeQuery = true)
    List<Film> findFavoriteFilmsWithLimit(@Param("amount") int amount);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO LIST_LIKES (person_id, film_id)
            VALUES (:idOfUser, :filmId)
            """, nativeQuery = true)
    void addLikeToFilm(@Param("filmId") int filmId, @Param("idOfUser") int idOfUser);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM LIST_LIKES
            WHERE person_id = :idOfUser and film_id = :filmId
            """, nativeQuery = true)
    void deleteLike(int filmId, int idOfUser);
}
