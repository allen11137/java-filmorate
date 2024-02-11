package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.persistence.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query(value = "SELECT * FROM RATING WHERE ID = :id", nativeQuery = true)
    Rating findMpaById(@Param("id") Integer id);
}
