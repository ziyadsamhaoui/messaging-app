<div align="center">

# 💬 messaging-app

**A full-stack real-time messaging application built with Java, Spring Boot, React, and TypeScript**

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![React](https://img.shields.io/badge/React-19-61DAFB?style=flat-square&logo=react&logoColor=black)](https://react.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.x-3178C6?style=flat-square&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white)](https://www.docker.com/)

A scalable messaging platform featuring secure authentication, real-time communication, and a modern React frontend powered by a Spring Boot backend.

</div>

---

## ✨ Features

- 💬 **Real-time messaging** — Instant message delivery with live updates
- 🔐 **Authentication** — Secure user registration and login
- 👤 **User management** — Create accounts and manage user sessions
- 🟢 **Presence status** — Online/offline user indicators
- ⌨️ **Typing indicators** — See when another user is typing
- 📨 **Conversation management** — Private chats between users
- ⚡ **REST API** — Spring Boot backend exposing messaging services
- 🐳 **Docker support** — One-command deployment with Docker Compose
- 📱 **Responsive interface** — Optimized for desktop and mobile devices

---

## 🗂️ Project Structure

```text
messaging-app/
├── frontend/                  # React + TypeScript frontend
│   ├── src/
│   ├── public/
│   └── package.json
│
├── src/                       # Spring Boot backend
│   └── main/
│       ├── java/
│       └── resources/
│
├── .mvn/                      # Maven wrapper
├── compose.yaml               # Docker Compose configuration
└── pom.xml                    # Maven configuration
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot |
| Frontend | React + TypeScript |
| Build Tool | Maven |
| Containerization | Docker |

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Node.js 18+
- Docker & Docker Compose *(optional but recommended)*

### Clone the repository

```bash
git clone https://github.com/your-username/messaging-app.git
cd messaging-app
```

### Run with Docker

```bash
docker compose up --build
```

### Or run manually

Backend:

```bash
./mvnw spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

---

## 🤝 Contributing

Contributions are welcome!

1. Fork the repository
2. Create a feature branch

```bash
git checkout -b feature/my-feature
```

3. Commit your changes

```bash
git commit -m "feat: add new feature"
```

4. Push your branch

```bash
git push origin feature/my-feature
```

5. Open a Pull Request

---
