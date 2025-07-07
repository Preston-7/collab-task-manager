package com.taskmanager.server.controller;

import com.taskmanager.server.service.TaskReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class ReminderDebugController {

    private final TaskReminderService taskReminderService;

    @Autowired
    public ReminderDebugController(TaskReminderService taskReminderService) {
        this.taskReminderService = taskReminderService;
    }

    @PostMapping("/run-reminder")
    public void runReminderManually() {
        taskReminderService.checkUpcomingTaskReminders();
    }
}
