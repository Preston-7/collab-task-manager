# Real-Time Collaborative Task Management System

This is a full-stack, real-time task management application built with Spring Boot (Java) on the backend and React (TypeScript) on the frontend. The app allows users to create, update, and track tasks collaboratively in real time. The backend is fully integrated with a PostgreSQL database and is containerized with Docker for portability and deployment.

## Project Overview

This project is designed to demonstrate real-world, production-grade development skills across the stack. It includes database integration, API design, secure user authentication (planned), real-time updates, and frontend integration.

Unlike basic CRUD tutorials, this system is modular, persistent, and built for actual use, not just demonstration.

## Core Features

### Backend (Spring Boot)
- RESTful API with clean separation of concerns
- Task CRUD operations (`/tasks`)
- PostgreSQL integration with JPA/Hibernate
- Tested via Postman and curl
- Dockerized for local development and deployment

### Planned Features
- JWT or OAuth2 authentication
- WebSocket-based real-time task updates
- File upload support for task attachments
- Role-based access control (admin, collaborator, read-only)
- CI/CD pipeline via GitHub Actions and Render deployment

### Frontend (React + TypeScript)
- In progress: React UI built with reusable components and hooks
- Task board, forms, and real-time feedback
- State management using Context API or Redux (TBD)

## Technologies Used

- **Java 21**, **Spring Boot 3**
- **PostgreSQL**, **JPA**, **Hibernate**
- **Docker**, **Docker Compose**
- **React**, **TypeScript**
- **WebSockets** (planned)
- **Render / Railway** for deployment

## Local Development

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Node.js 18+

### Running the Backend
```bash
cd server
./mvnw clean spring-boot:run
