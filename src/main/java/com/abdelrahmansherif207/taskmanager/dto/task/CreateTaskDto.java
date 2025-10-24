package com.abdelrahmansherif207.taskmanager.dto.task;

import com.abdelrahmansherif207.taskmanager.entity.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskDto {
    private String title;
    private String description;
    private Task.TaskStatus status;
}