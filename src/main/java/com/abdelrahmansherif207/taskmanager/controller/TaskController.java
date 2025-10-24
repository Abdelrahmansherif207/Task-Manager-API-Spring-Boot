package com.abdelrahmansherif207.taskmanager.controller;

import com.abdelrahmansherif207.taskmanager.dto.task.CreateTaskDto;
import com.abdelrahmansherif207.taskmanager.dto.task.TaskResponseDto;
import com.abdelrahmansherif207.taskmanager.dto.task.UpdateTaskDto;
import com.abdelrahmansherif207.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class TaskController {
    private final TaskService taskService;

    @PostMapping({"","/"})
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody CreateTaskDto dto) {
        TaskResponseDto createdTask = taskService.createTask(dto);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping({"","/"})
    public ResponseEntity<List<TaskResponseDto>> getAllUserTasks() {
        List<TaskResponseDto> tasks = taskService.getAllUserTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        TaskResponseDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskDto dto
    ) {
        TaskResponseDto updatedTask = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
