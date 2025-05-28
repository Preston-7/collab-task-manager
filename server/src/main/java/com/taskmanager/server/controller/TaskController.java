package com.taskmanager.server.controller;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.TaskRepository;
import com.taskmanager.server.repository.UserRepository;
import com.taskmanager.server.service.TaskReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.scheduling.annotation.Scheduled;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskReminderService taskReminderService;

    @GetMapping
    public List<Task> getTasksForCurrentUser(Authentication authentication,
                                             @RequestParam(required = false) Boolean completed,
                                             @RequestParam(required = false) String label,
                                             @RequestParam(required = false) String priority,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueBefore,
                                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dueAfter,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) {

        String username = authentication.getName();
        List<Task> tasks = taskRepository.findByUserUsername(username);

        return tasks.stream()
                .filter(task -> completed == null || task.isCompleted() == completed)
                .filter(task -> label == null || label.equalsIgnoreCase(task.getLabel()))
                .filter(task -> priority == null || priority.equalsIgnoreCase(task.getPriority()))
                .filter(task -> dueBefore == null || task.getDueDate() != null && task.getDueDate().isBefore(dueBefore))
                .filter(task -> dueAfter == null || task.getDueDate() != null && task.getDueDate().isAfter(dueAfter))
                .skip((long) page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    @PostMapping
    public Task createTask(@RequestBody Task task, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        task.setUser(user);

        if (task.getSubtasks() != null) {
            task.getSubtasks().forEach(subtask -> subtask.setUser(user));
        }

        return taskRepository.save(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask, Authentication authentication) {
        String username = authentication.getName();
        Optional<Task> existingOpt = taskRepository.findById(id);

        if (existingOpt.isEmpty() || !existingOpt.get().getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Task task = existingOpt.get();
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setCompleted(updatedTask.isCompleted());
        task.setDueDate(updatedTask.getDueDate());
        task.setLabel(updatedTask.getLabel());
        task.setPriority(updatedTask.getPriority());
        task.setReminderTime(updatedTask.getReminderTime());

        if (updatedTask.getSubtasks() != null) {
            updatedTask.getSubtasks().forEach(subtask -> subtask.setUser(task.getUser()));
        }
        task.setSubtasks(updatedTask.getSubtasks());

        return new ResponseEntity<>(taskRepository.save(task), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Optional<Task> taskOpt = taskRepository.findById(id);

        if (taskOpt.isEmpty() || !taskOpt.get().getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        taskRepository.delete(taskOpt.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delegate scheduled task reminder to service
    @Scheduled(fixedRate = 60000)
    public void runReminderCheck() {
        taskReminderService.checkUpcomingTaskReminders();
    }
}
