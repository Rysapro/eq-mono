package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User> {
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.name = ?2 AND u.surname = ?1 AND u.patronymic = ?3")
    Optional<User> findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);

    @Query("SELECT u FROM User u WHERE " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :searchWord, '%')) OR " +
            "LOWER(u.surname) LIKE LOWER(CONCAT('%', :searchWord, '%')) OR " +
            "LOWER(u.patronymic) LIKE LOWER(CONCAT('%', :searchWord, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchWord, '%'))")
    List<User> searchByFields(@Param("searchWord") String searchWord);
}
