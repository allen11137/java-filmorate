package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.persistence.model.Person;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Repository
@Transactional
public class UserJdbcRepository {
    public static final String TABLE_NAME = "PERSON";
    public static final String ID = "ID";
    private final AtomicInteger id = new AtomicInteger();
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public UserJdbcRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.dataSource = dataSource;
    }

    public void save(Person person) {
        id.getAndIncrement();
        person.setId(Integer.parseInt(String.valueOf(id)));
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(person);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID);
        simpleJdbcInsert.execute(parameterSource);
    }

    public void update(Person person) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(person);
        String sql = "UPDATE PERSON SET email = :email, login = :login, " +
                "name = :name, birthday = :birthday WHERE ID = :id";
        jdbcTemplate.update(sql, parameterSource);
    }

    public void delete(Person person) {
        String sql = "DELETE FROM PERSON WHERE ID = :id";
        jdbcTemplate.update(sql, Map.of("id", person.getId()));
    }

    public List<Person> findAll() {
        String sql = "SELECT * FROM PERSON";
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(Person.class));
    }

    public Optional<Person> findById(Integer idOfUser) {
        String sql = "SELECT * FROM PERSON WHERE ID = :id";
        return jdbcTemplate.query(sql, Map.of("id", idOfUser), new DataClassRowMapper<>(Person.class))
                .stream()
                .findFirst();
    }

    public void updatePersonFriend(int idOfUser, int idOfFriend) {
        String sql = "INSERT INTO FRIENDSHIP (person_id, friend_id, status_of_friend) " +
                "VALUES (:idOfUser, :idOfFriend, true)";
        jdbcTemplate.update(sql, Map.of("idOfUser", idOfUser, "idOfFriend", idOfFriend));
    }

    public void deleteFriend(int idOfUser, int idOfFriend) {
        String sql = "UPDATE FRIENDSHIP SET status_of_friend = false " +
                "WHERE person_id = :idOfUser and friend_id = :idOfFriend";
        jdbcTemplate.update(sql, Map.of("idOfUser", idOfUser, "idOfFriend", idOfFriend));
    }

    public List<Person> findAllFriends(int idOfUser) {
        String sql = "SELECT * FROM PERSON p LEFT JOIN FRIENDSHIP f ON f.friend_id = p.id " +
                "WHERE f.person_id = :idOfUser and f.status_of_friend = true";
        return jdbcTemplate.query(sql, Map.of("idOfUser", idOfUser), new DataClassRowMapper<>(Person.class));
    }
}
