package com.taskmanager.server.controller;

import com.taskmanager.server.model.ReminderNotification;
import com.taskmanager.server.model.User;
import com.taskmanager.server.repository.ReminderNotificationRepository;
import com.taskmanager.server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private ReminderNotificationRepository reminderRepo;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<ReminderNotification> getReminders(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return reminderRepo.findByUser(user);
    }
}

