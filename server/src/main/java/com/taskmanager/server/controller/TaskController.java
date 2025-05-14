package com.taskmanager.server.controller;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.TaskRepository;
import com.taskmanager.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Task> getAllTasks(@AuthenticationPrincipal UserDetails userDetails) {
        return taskRepository.findByUserUsername(userDetails.getUsername());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        task.setUser(user.get());
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task taskDetails, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isEmpty() || !optionalTask.get().getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.notFound().build();
        }
        Task task = optionalTask.get();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setCompleted(taskDetails.isCompleted());
        task.setDueDate(taskDetails.getDueDate());
        Task updatedTask = taskRepository.save(task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isEmpty() || !task.get().getUser().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.notFound().build();
        }
        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
