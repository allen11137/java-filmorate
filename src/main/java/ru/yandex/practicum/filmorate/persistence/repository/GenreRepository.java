package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.persistence.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query(value = "SELECT * FROM GENRE WHERE ID IN :idsGenre", nativeQuery = true)
    Set<Genre> findAllByIdIsIn(@Param("idsGenre") List<Integer> idsGenre);
}
