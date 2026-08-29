# 📚 Library Management System (LMS-Java)

A full-stack Library Management System built progressively across three sub-projects — from raw Java to a Spring Boot backend with a JavaFX desktop frontend. Each iteration represents a distinct phase of development, documented independently in its own subfolder.

---

## 🗂️ Repository Structure

```
LMS-Java/
├── LibraryManagementSystem/ # Phase 1 — Raw Java + JDBC - Legacy
├── lms/ # Phase 2 — Spring Boot
└── lms-frontend/ # Phase 3 — React Frontend
```

---

## 📦 Sub-Projects

### 1. LMS Legacy

> **Stack:** Java · JDBC · MySQL · Maven

The original implementation of the Library Management System — built without any frameworks or frontend. Focuses on core OOP design, custom data structures, SQL queries via JDBC, and console-based interaction.

**Highlights:**

- Pure Java with no Spring or ORM dependencies
- Direct JDBC integration with MySQL
- PBKDF2 password hashing
- Reservation queue using SQL triggers and stored procedures
- Single Table Inheritance for book types (`PhysicalBook`, `EBook`, `AudioBook`)

📁 See [`LMS-Legacy/`](./LibraryManagementSystem/) for setup and usage docs.

---

### 2. LMS Spring Backend

> **Stack:** Java · Spring Boot · Spring MVC · Spring Security · Spring Data JPA · MySQL · Maven

A full RESTful backend built with Spring Boot. Replaces the legacy layer with a production-grade architecture including JWT-based authentication, role-based access control, and a clean DTO-driven API.

**Highlights:**

- JPA entity modeling with `@EmbeddedId`, Single Table Inheritance
- RESTful API with proper HTTP semantics
- Global exception handling via `@RestControllerAdvice`
- DTO design using Java records and sealed interfaces with Jackson polymorphic deserialization
- Spring Security with `UserDetailsService`, `BCryptPasswordEncoder`, and `JwtAuthenticationFilter`
- Role-Based Access Control (RBAC) using `SecurityFilterChain` and `@PreAuthorize`
- Bean Validation with `@Valid` and custom constraint annotations
- `ddl-auto=validate` with schema-first design in MySQL Workbench

📁 See [`LMS-Spring-Backend/`](./lms/) for API docs, setup, and configuration details.

---

### 3. LMS React Frontend

> **Stack:** React · JavaScript · JWT  
> ⚠️ **Status: Under Development**

A web client that connects to the Spring Boot backend via HTTP. Handles authentication through JWT Bearer tokens and renders role-specific views for users and administrators.

**Highlights:**

- Built with React for a modern single-page application experience
- Communicates with the REST backend using `fetch`/`axios`
- JWT token stored and attached to all authenticated requests
- Role-aware UI — different views for `ADMIN` and `MEMBER` roles
- Login screen and session management in progress

📁 See [`lms-frontend/`](./lms-frontend/) for build instructions and current progress.

---

## 🔐 Authentication Flow

```
[React Login Page]
│
│ POST /api/auth/login {username, password}
▼
[Spring Boot Backend]
│
│ Validates credentials via UserDetailsService + BCrypt
│ Issues signed JWT (HS256)
▼
[React Client receives JWT]
│
│ Authorization: Bearer <token> (on every subsequent request)
▼
[JwtAuthenticationFilter] → [SecurityFilterChain] → [Protected Endpoints]

```

---

## 🛠️ Tech Stack Overview

| Layer | LMS Legacy | LMS Spring Backend | LMS React Frontend |
|---|---|---|---|
| Language | Java | Java | JavaScript |
| Framework | None | Spring Boot | React |
| Persistence | JDBC | Spring Data JPA | — (via HTTP) |
| Security | PBKDF2 (manual) | Spring Security + JWT | Bearer token |
| Database | MySQL | MySQL | — |
| Build Tool | Maven | Maven | npm/vite |
| Frontend | Console | — | Web SPA |

---

## 📋 Prerequisites

- Java 17+
- MySQL 8+
- Maven 3.8+
- Node.js 18+ (for the frontend module)

---

## 🚀 Getting Started

Each sub-project has its own setup guide. Navigate to the relevant subfolder and follow the `README.md` inside:

- [`LMS-Legacy/`](./LibraryManagementSystem/) — Console app setup
- [`LMS-Spring-Backend/`](./lms/) — Backend server setup
- [`lms-frontend/`](./lms-frontend/) — React client setup

---

## 👨‍💻 Author

**Shahaam-Sid**  
Computer Science Student | Java Enthusiast  
[GitHub](https://github.com/Shahaam-Sid) · [LinkedIn](https://www.linkedin.com/in/shahaam-sid/)

---

> _This project is built for learning and portfolio purposes, progressively applying enterprise Java patterns from scratch._
