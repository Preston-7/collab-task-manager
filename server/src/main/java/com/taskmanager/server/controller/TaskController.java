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
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Task> createTask(@RequestBody Task task, Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();

        task.setUser(user);

        if (task.getSubtasks() != null) {
            task.getSubtasks().forEach(subtask -> subtask.setUser(user));
        }

        Task savedTask = taskRepository.save(task);
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/attachment")
    public ResponseEntity<String> uploadAttachment(@PathVariable Long id,
                                                   @RequestParam("file") MultipartFile file,
                                                   Authentication authentication) {
        String username = authentication.getName();
        Optional<Task> taskOpt = taskRepository.findById(id);

        if (taskOpt.isEmpty() || !taskOpt.get().getUser().getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            String fileInfo = String.format("Received file '%s' (%d bytes)", file.getOriginalFilename(), file.getSize());
            return ResponseEntity.ok(fileInfo);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to process file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    @Scheduled(fixedRate = 60000)
    public void runReminderCheck() {
        taskReminderService.checkUpcomingTaskReminders();
    }
}
