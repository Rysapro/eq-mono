package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, BaseRepository<User> {
    Optional<User> findByNameAndSurnameAndPatronymic(String name, String surname, String patronymic);
    
    @Query("SELECT u FROM User u WHERE u.name = ?2 AND u.surname = ?1 AND u.patronymic = ?3")
    Optional<User> findBySurnameAndNameAndPatronymic(String surname, String name, String patronymic);
}
