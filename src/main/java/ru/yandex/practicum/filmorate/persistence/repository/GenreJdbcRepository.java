package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.persistence.model.Genre;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Repository
@Transactional
public class GenreJdbcRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public GenreJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public List<Genre> findAll() {
        String sql = """
                SELECT * FROM GENRE
                """;
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(Genre.class));
    }

    public Optional<Genre> findById(Integer id) {
        String sql = """
                SELECT * FROM GENRE WHERE ID = :id
                """;
        return jdbcTemplate.query(sql, Map.of("id", id), new DataClassRowMapper<>(Genre.class))
                .stream()
                .findFirst();
    }
}
