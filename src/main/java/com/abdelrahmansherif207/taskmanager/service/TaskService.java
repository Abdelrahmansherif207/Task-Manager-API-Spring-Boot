package com.abdelrahmansherif207.taskmanager.service;

import com.abdelrahmansherif207.taskmanager.dto.task.CreateTaskDto;
import com.abdelrahmansherif207.taskmanager.dto.task.TaskResponseDto;
import com.abdelrahmansherif207.taskmanager.dto.task.UpdateTaskDto;
import com.abdelrahmansherif207.taskmanager.entity.Task;
import com.abdelrahmansherif207.taskmanager.entity.User;
import com.abdelrahmansherif207.taskmanager.exception.TaskAccessDeniedException;
import com.abdelrahmansherif207.taskmanager.exception.TaskNotFoundException;
import com.abdelrahmansherif207.taskmanager.repository.TaskRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskResponseDto createTask(@Valid CreateTaskDto dto) {
        User currentUser = getCurrentUser();
        
        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .status(dto.getStatus() != null ? dto.getStatus() : Task.TaskStatus.OPEN)
                .user(currentUser)
                .build();
        
        Task savedTask = taskRepository.save(task);
        return mapToDto(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponseDto> getAllUserTasks() {
        User currentUser = getCurrentUser();
        return taskRepository.findByUserOrderByIdDesc(currentUser)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = getTaskForCurrentUser(taskId);
        return mapToDto(task);
    }

    public TaskResponseDto updateTask(Long taskId, @Valid UpdateTaskDto dto) {
        Task task = getTaskForCurrentUser(taskId);
        
        if (dto.getTitle() != null && !dto.getTitle().isEmpty()) {
            task.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            task.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            task.setStatus(dto.getStatus());
        }
        
        Task updatedTask = taskRepository.save(task);
        return mapToDto(updatedTask);
    }

    public void deleteTask(Long taskId) {
        Task task = getTaskForCurrentUser(taskId);
        taskRepository.delete(task);
    }

    private Task getTaskForCurrentUser(Long taskId) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new TaskAccessDeniedException("Access Denied!");
        }
        
        return task;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private TaskResponseDto mapToDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .build();
    }
}
