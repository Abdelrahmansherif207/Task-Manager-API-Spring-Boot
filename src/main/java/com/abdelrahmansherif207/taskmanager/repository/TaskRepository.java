package com.abdelrahmansherif207.taskmanager.repository;

import com.abdelrahmansherif207.taskmanager.entity.Task;
import com.abdelrahmansherif207.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserOrderByIdDesc(User user);
    Optional<Task> findByIdAndUser(Long id, User user);
}