package com.taskmanager.server.service;

import com.taskmanager.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskReminderService {

    private final UserRepository userRepository;

    @Autowired
    public TaskReminderService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkUpcomingTaskReminders() {
        var now = LocalDateTime.now();
        var upcoming = now.plusMinutes(10);

        userRepository.findAll().forEach(user -> {
            var dueSoon = user.getTasks().stream()
                    .filter(task -> task.getReminderTime() != null)
                    .filter(task -> !task.isCompleted())
                    .filter(task -> task.getReminderTime().isAfter(now) && task.getReminderTime().isBefore(upcoming))
                    .toList();

            if (!dueSoon.isEmpty()) {
                System.out.println("[Reminder] User: " + user.getUsername() + " has " + dueSoon.size() + " task(s) due soon.");
            }
        });
    }
}
