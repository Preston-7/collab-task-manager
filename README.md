# Collaborative Task Manager API
[![Java](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)  
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-brightgreen)](https://spring.io/projects/spring-boot)  
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)](https://www.postgresql.org/)  
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()  
[![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)  

A **scalable, real-time collaborative task management system** designed for individuals and teams. Built with a **Spring Boot backend** and a planned **React frontend**, this system supports seamless task organization, reminders, and future team collaboration.

---

## Features

### Core Backend Features
- **User Authentication**
  - Secure registration and login endpoints
  - Password hashing with bcrypt
  - Role-based access control (default: `USER`)

- **Task Management**
  - Full CRUD operations for tasks
  - Task fields include: title, description, due date, priority, label, and completion status
  - Nested **subtasks** supported with parent-child relationships
  - File attachments: upload and download capabilities

- **Reminders**
  - Configure `reminderTime` for tasks
  - Background service scans for upcoming reminders every minute
  - Automatic **email notifications** for pending tasks
  - Sent reminders logged in the `ReminderNotification` table

- **Filtering & Pagination**
  - Filter tasks by completion status, priority, label, or due date range
  - Paginate large result sets for performance optimization

---

## Planned Features

| Feature                            | Status         | Notes                                   |
|------------------------------------|----------------|-----------------------------------------|
| User Registration/Login            | Complete       | BasicAuth implemented                   |
| Task CRUD                          | Complete       | Supports nested subtasks                |
| Reminder Emails                    | Complete       | Automated background service enabled    |
| React Frontend (TypeScript)        | In Progress    | Login, dashboard, task UI               |
| JWT Authentication                 | Planned        | Replace BasicAuth with token-based flow |
| Docker + Cloud Deployment          | Planned        | Deployable via Docker to AWS/Fly.io     |
| Team Collaboration Features        | Planned        | Assign tasks to users, add comments     |
| Real-Time Updates (WebSockets)     | Planned        | Push notifications for task changes     |
| Analytics Dashboard                | Planned        | Productivity insights & visualizations  |
| Mobile-Friendly Frontend           | Planned        | Responsive UI for mobile devices        |

---

## Project Structure

```bash
collab-task-manager/
├── server/              # Spring Boot backend
│   ├── src/main/java/   # Java source code
│   ├── src/main/resources/
├── client/              # React frontend (planned)
├── README.md            # Project documentation
```

---

## API Usage Examples

### Register a User
```bash
POST /register
Content-Type: application/json

{
  "username": "testuser",
  "password": "password",
  "email": "testuser@example.com"
}
```

### Login and Fetch Tasks
```bash
curl -u testuser:password http://localhost:8080/tasks
```

### Create a Task
```bash
POST /tasks
Authorization: Basic Auth

{
  "title": "New Task",
  "description": "Finish API integration",
  "dueDate": "2025-07-10T23:59:00",
  "reminderTime": "2025-07-10T12:00:00"
}
```

### Upload an Attachment
```bash
POST /tasks/{id}/attachment
Content-Type: multipart/form-data
Authorization: Basic Auth
```

---

## Running Locally

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/your-username/collab-task-manager.git
   cd collab-task-manager/server
   ```

2. **Configure the Database**  
   Update `src/main/resources/application.properties` with your PostgreSQL credentials.

3. **Start the Backend**  
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Test API Endpoints**  
   Use Postman, curl, or your browser to interact with the API.

---

## Frontend Preview (Coming Soon)

| Login Page              | Task Dashboard            |
|-------------------------|----------------------------|
| *(screenshot placeholder)* | *(screenshot placeholder)* |

---

## Docker Deployment (Planned)

1. **Build Docker image**:  
   ```bash
   docker build -t collab-task-manager-backend ./server
   ```
2. **Run container**:  
   ```bash
   docker run -p 8080:8080 collab-task-manager-backend
   ```

---

## Contributions

This project was developed by:  

**Preston Beachum**  
Software Engineering Student @ UNCW  
- [LinkedIn]([https://linkedin.com/in/your-link](https://www.linkedin.com/in/preston-beachum-a3b7b12ba/))  
- [GitHub]([https://github.com/your-username](https://github.com/prestonbeachum/prestonbeachum))  
- Email: prestonbeachum@gmail.com  
