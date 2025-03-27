package com.example.electronic_queue_monolit.repository;

import com.example.electronic_queue_monolit.domain.model.User;
import com.example.electronic_queue_monolit.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, BaseRepository<User> {
    Optional<User> findByUsername(String username);
}
