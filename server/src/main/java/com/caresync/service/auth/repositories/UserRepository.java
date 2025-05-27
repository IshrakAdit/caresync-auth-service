package com.caresync.service.auth.repositories;

import com.caresync.service.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}