package com.taskmanager.server.controller;

import com.taskmanager.server.service.TaskReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/debug")
public class DebugController {

    @Autowired
    private TaskReminderService taskReminderService;

    @GetMapping("/reminders")
    public void manuallyTriggerReminders() {
        taskReminderService.checkUpcomingTaskReminders();
    }
}
