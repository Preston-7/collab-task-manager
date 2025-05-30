package com.taskmanager.server.controller;

import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
public class TaskFilteringController {

    private final UserRepository userRepository;

    @Autowired
    public TaskFilteringController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Check reminders every 1 minute
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
