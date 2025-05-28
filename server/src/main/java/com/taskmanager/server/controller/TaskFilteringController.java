package com.taskmanager.server.controller;

import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/register")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // Scheduled notification check every 1 minute
    @Scheduled(fixedRate = 60000)
    public void checkUpcomingTaskReminders() {
        var now = LocalDateTime.now();
        var upcoming = now.plusMinutes(10);

        userRepository.findAll().forEach(user -> {
            var dueSoon = user.getTasks().stream()
                    .filter(task -> task.getReminderTime() != null)
                    .filter(task -> !task.isCompleted())
                    .filter(task -> task.getReminderTime().isAfter(now) && task.getReminderTime().isBefore(upcoming))
                    .collect(Collectors.toList());

            if (!dueSoon.isEmpty()) {
                System.out.println("[Reminder] User: " + user.getUsername() + " has " + dueSoon.size() + " task(s) due soon.");
            }
        });
    }
}
