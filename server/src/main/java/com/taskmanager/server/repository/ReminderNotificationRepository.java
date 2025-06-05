package com.taskmanager.server.repository;

import com.taskmanager.server.model.ReminderNotification;
import com.taskmanager.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReminderNotificationRepository extends JpaRepository<ReminderNotification, Long> {
    List<ReminderNotification> findByUser(User user);
}
