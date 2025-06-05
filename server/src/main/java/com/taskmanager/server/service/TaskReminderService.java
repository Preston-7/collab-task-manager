package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.ReminderNotification;
import com.taskmanager.server.repository.ReminderNotificationRepository;
import com.taskmanager.server.repository.TaskRepository;
import com.taskmanager.server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TaskReminderService {

    private final UserRepository userRepository;
    private final ReminderNotificationRepository reminderRepo;

    @Autowired
    public TaskReminderService(UserRepository userRepository,
                               ReminderNotificationRepository reminderRepo) {
        this.userRepository = userRepository;
        this.reminderRepo = reminderRepo;
    }

    public void checkUpcomingTaskReminders() {
        var now = LocalDateTime.now();
        var upcoming = now.plusMinutes(5);

        userRepository.findAll().forEach(user -> {
            var dueSoon = user.getTasks().stream()
                    .filter(task -> task.getReminderTime() != null)
                    .filter(task -> !task.isCompleted())
                    .filter(task -> task.getReminderTime().isAfter(now) && task.getReminderTime().isBefore(upcoming))
                    .toList();

            for (Task task : dueSoon) {
                ReminderNotification reminder = new ReminderNotification();
                reminder.setUser(user);
                reminder.setMessage("Reminder: " + task.getTitle() + " is due soon.");
                reminder.setTriggeredAt(now);
                reminderRepo.save(reminder);
            }
        });
    }
}

