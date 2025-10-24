package com.abdelrahmansherif207.taskmanager.repository;

import com.abdelrahmansherif207.taskmanager.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String username);
}
