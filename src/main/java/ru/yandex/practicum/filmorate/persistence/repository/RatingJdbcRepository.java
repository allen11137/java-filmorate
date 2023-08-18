package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.persistence.model.Rating;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Repository
@Transactional
public class RatingJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RatingJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Rating> findAll() {
        String sql = """
                SELECT * FROM RATING
                """;
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(Rating.class));
    }

    public Optional<Rating> findById(Integer id) {
        String sql = """
                SELECT * FROM RATING WHERE ID = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", id), new DataClassRowMapper<>(Rating.class))
                .stream()
                .findFirst();
    }
}
