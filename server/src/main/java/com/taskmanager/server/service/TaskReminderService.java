package com.taskmanager.server.service;

import com.taskmanager.server.model.Task;
import com.taskmanager.server.model.ReminderNotification;
import com.taskmanager.server.repository.ReminderNotificationRepository;
import com.taskmanager.server.repository.TaskRepository;
import com.taskmanager.server.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Service
public class TaskReminderService {

    private final UserRepository userRepository;
    private final ReminderNotificationRepository reminderRepo;
    private final TaskRepository taskRepo;
    private final JavaMailSender mailSender;

    @Autowired
    public TaskReminderService(UserRepository userRepository,
                               ReminderNotificationRepository reminderRepo,
                               TaskRepository taskRepo,
                               JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.reminderRepo = reminderRepo;
        this.taskRepo = taskRepo;
        this.mailSender = mailSender;
    }

    @Scheduled(fixedRate = 30000)
    public void checkUpcomingTaskReminders() {
        System.out.println("TaskReminderService running at " + LocalDateTime.now());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime upcoming = now.plusMinutes(3);

        userRepository.findAll().forEach(user -> {
            System.out.println("👤 Checking user: " + user.getUsername());
            if (user.getTasks() == null || user.getTasks().isEmpty()) {
                System.out.println("No tasks found for user: " + user.getUsername());
                return;
            }

            user.getTasks().forEach(task -> {
                System.out.println("🔍 Task: " + task.getTitle() +
                        " | reminderTime: " + task.getReminderTime() +
                        " | completed: " + task.isCompleted() +
                        " | reminderSent: " + task.isReminderSent());

                if (task.getReminderTime() == null) {
                    System.out.println("⏭ Skipping: No reminderTime set.");
                    return;
                }
                if (task.isCompleted()) {
                    System.out.println("Skipping: Task is already completed.");
                    return;
                }
                if (task.isReminderSent()) {
                    System.out.println("Skipping: Reminder already sent.");
                    return;
                }
                if (task.getReminderTime().isBefore(now)) {
                    System.out.println("Skipping: reminderTime is in the past.");
                    return;
                }
                if (task.getReminderTime().isAfter(upcoming)) {
                    System.out.println("Skipping: reminderTime is beyond upcoming window.");
                    return;
                }

                // Send reminder
                ReminderNotification reminder = new ReminderNotification();
                reminder.setUser(user);
                reminder.setMessage("Reminder: " + task.getTitle() + " is due soon.");
                reminder.setTriggeredAt(LocalDateTime.now());
                reminderRepo.save(reminder);

                if (user.getEmail() != null && !user.getEmail().isBlank()) {
                    sendReminderEmail(user.getEmail(), task.getTitle());
                }

                task.setReminderSent(true);
                taskRepo.save(task);
                System.out.println("Reminder triggered for task: " + task.getTitle());
            });
        });
    }

    private void sendReminderEmail(String to, String taskTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject("Task Reminder");
            message.setText("Reminder: Your task \"" + taskTitle + "\" is due soon.");
            mailSender.send(message);
            System.out.println("Reminder email sent to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send reminder email to " + to + ": " + e.getMessage());
        }
    }
}
