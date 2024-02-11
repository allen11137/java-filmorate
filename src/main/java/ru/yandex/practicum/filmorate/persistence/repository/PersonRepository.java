package ru.yandex.practicum.filmorate.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.filmorate.persistence.model.Person;

import javax.transaction.Transactional;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO FRIENDSHIP (person_id, friend_id, status_of_friend) " +
            "VALUES (:idOfUser, :idOfFriend, true)", nativeQuery = true)
    void updatePersonFriend(@Param("idOfUser") int idOfUser, @Param("idOfFriend") int idOfFriend);

    @Query(value = "SELECT count(*) FROM FRIENDSHIP WHERE person_id = :personId", nativeQuery = true)
    long countFriends(@Param("personId") int personId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE FRIENDSHIP " +
            "SET status_of_friend = false " +
            "WHERE person_id = :idOfUser and friend_id = :idOfFriend", nativeQuery = true)
    void deleteFriend(@Param("idOfUser") int idOfUser, @Param("idOfFriend") int idOfFriend);

    @Query(value = "SELECT * FROM PERSON p " +
            "LEFT JOIN FRIENDSHIP f ON f.friend_id = p.id " +
            "WHERE f.person_id = :idOfUser and f.status_of_friend = true",
            nativeQuery = true)
    List<Person> findAllFriends(@Param("idOfUser") int idOfUser);


    @Query(value = "SELECT * FROM PERSON WHERE id in (SELECT p.id FROM PERSON p " +
            "LEFT JOIN FRIENDSHIP f ON f.person_id = p.id " +
            "WHERE f.person_id = :idOfUser) AND " +
            "id IN (SELECT p1.id FROM PERSON p1 " +
            "LEFT JOIN FRIENDSHIP f ON f.person_id = p1.id " +
            "WHERE f.person_id = :idOfFriend)",
            nativeQuery = true)
    List<Person> findAllMainFriends(int idOfUser, int idOfFriend);
}
