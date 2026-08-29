# Library Management System — Spring Backend

A production-grade RESTful backend for the Library Management System, built with Spring Boot 4.0.7. This is Phase 2 of the LMS project, replacing the legacy JDBC layer with a full Spring ecosystem stack — featuring JWT authentication, role-based access control, and a clean DTO-driven API.

> **Status:** Feature-complete. JavaFX frontend integration in progress.

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#️-project-structure)
- [Architecture](#️-architecture)
- [Technologies](#️-technologies)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [Security](#-security)
- [Exception Handling](#️-exception-handling)
- [Key Constraints](#-key-constraints)
- [Future Enhancements](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)
- [Support](#-support)

---

## ✨ Features

### Authentication & Authorization

- **JWT Authentication**: Stateless login issuing signed HS256 tokens via `jjwt`
- **Protected Endpoints**: Per-route access enforced via `SecurityFilterChain` and `@PreAuthorize`
- **Password Security**: `BCryptPasswordEncoder` for hashing; no plaintext stored

### Book Management

- **Multi-format Support**: Physical Books, E-Books, and AudioBooks via Single Table Inheritance
- **Full CRUD**: Add, update, delete, and retrieve books
- **Book Search**: Search by title, author, ISBN, or genre
- **Inventory Tracking**: Manage available copies and book status

### Member Management

- **Member Registration**: Register members with Bean Validation on all inputs
- **Profile Management**: Update name, phone, email, address, and status
- **Member Search**: Search by name, ID, or email
- **Status Tracking**: `ACTIVE`, `EXPIRED`, `SUSPENDED`

### Transaction Management

- **Book Borrowing**: Issue books with automatic due date calculation (14 days)
- **Book Returns**: Process returns and calculate late fees
- **Overdue Tracking**: Monitor overdue transactions and outstanding fines
- **Transaction History**: Full borrowing records per member

### Reservation Management

- **Reserve Books**: Queue-based reservation when a book is unavailable
- **Reservation Notifications**: Members notified when their reserved book becomes available
- **Queue Management**: View, peek, and cancel reservations

---

## 🏗️ Project Structure

```
src
├───main
│   ├───java
│   │   └───com
│   │       └───shahaam
│   │           └───lms
│   │               │   LmsApplication.java
│   │               │   
│   │               ├───auth
│   │               │       AuthenticationController.java
│   │               │       AuthenticationRequestDTO.java
│   │               │       AuthenticationResponseDTO.java
│   │               │       AuthenticationService.java
│   │               │       RegisterRequestDTO.java
│   │               │       
│   │               ├───config
│   │               │       ApplicationConfig.java
│   │               │       JwtAuthenticationFilter.java
│   │               │       JwtService.java
│   │               │       SecurityConfig.java
│   │               │       
│   │               ├───controller
│   │               │       BookController.java
│   │               │       LoanController.java
│   │               │       MemberController.java
│   │               │       ReservationController.java
│   │               │       
│   │               ├───demo
│   │               │       DemoController.java
│   │               │       
│   │               ├───dto
│   │               │   ├───book
│   │               │   │       AudioBookRequestDTO.java
│   │               │   │       AudioBookResponseDTO.java
│   │               │   │       BookRequestDTO.java
│   │               │   │       BookResponseDTO.java
│   │               │   │       BookUpdateRequestDTO.java
│   │               │   │       EBookRequestDTO.java
│   │               │   │       EBookResponseDTO.java
│   │               │   │       PhysicalBookRequestDTO.java
│   │               │   │       PhysicalBookResponseDTO.java
│   │               │   │       
│   │               │   ├───loan
│   │               │   │       LoanRequestDTO.java
│   │               │   │       LoanResponseDTO.java
│   │               │   │       
│   │               │   ├───member
│   │               │   │       MemberRequestDTO.java
│   │               │   │       MemberResponseDTO.java
│   │               │   │       MemberUpdateRequestDTO.java
│   │               │   │       
│   │               │   └───reservation
│   │               │           ReservationRequestDTO.java
│   │               │           ReservationResponseDTO.java
│   │               │           
│   │               ├───enums
│   │               │       BookStatus.java
│   │               │       BookType.java
│   │               │       MemberStatus.java
│   │               │       ReservationStatus.java
│   │               │       Roles.java
│   │               │       
│   │               ├───exceptions
│   │               │       BookAlreadyBorrowedException.java
│   │               │       BookNotAvailableException.java
│   │               │       BookNotFoundException.java
│   │               │       DuplicateISBNException.java
│   │               │       DuplicatePupilException.java
│   │               │       ErrorResponse.java
│   │               │       GlobalExceptionHandler.java
│   │               │       InvalidBookTypeException.java
│   │               │       InvalidISBNException.java
│   │               │       InvalidPasswordException.java
│   │               │       MemberLimitExceededException.java
│   │               │       MemberNotFoundException.java
│   │               │       NoActiveBorrowRecordFoundException.java
│   │               │       NoBorrowRecordFoundException.java
│   │               │       ReservationRecordNotFoundException.java
│   │               │       WorkerNotFoundException.java
│   │               │       
│   │               ├───interfaces
│   │               │       Borrowable.java
│   │               │       ReservingService.java
│   │               │       
│   │               ├───models
│   │               │   │   Loan.java
│   │               │   │   
│   │               │   ├───book
│   │               │   │       AbstractBook.java
│   │               │   │       AbstractDigitalBook.java
│   │               │   │       AudioBook.java
│   │               │   │       EBook.java
│   │               │   │       PhysicalBook.java
│   │               │   │       
│   │               │   ├───Pupil
│   │               │   │       AbstractPupil.java
│   │               │   │       Admin.java
│   │               │   │       Member.java
│   │               │   │       
│   │               │   └───reservation
│   │               │           Reservation.java
│   │               │           ReservationId.java
│   │               │           
│   │               ├───repositories
│   │               │       AdminRepository.java
│   │               │       BookRepository.java
│   │               │       LoanRepository.java
│   │               │       MemberRepository.java
│   │               │       ReservationRepository.java
│   │               │       
│   │               ├───services
│   │               │       BookService.java
│   │               │       LoanService.java
│   │               │       MemberService.java
│   │               │       ReservationService.java
│   │               │       
│   │               └───utils
│   │                       PasswordUtils.java
│   │                       ValidationUtils.java
│   │                       
│   └───resources
│       │   application.properties
│       │   
│       ├───static
│       └───templates
└───test
    └───java
        └───com
            └───shahaam
                └───lms
                        LmsApplicationTests.java
```

---

## 🏛️ Architecture

[**Documentation and UMLs**](./docs/architecture.md)
[**Database and ERDs**](./docs/database.md)

### Design Patterns Used

1. **Single Table Inheritance**: `AbstractBook` as the JPA base entity for `PhysicalBook`, `EBook`, and `AudioBook` — mapped to a single table with a `type` discriminator column
2. **DTO Pattern**: Request/response separation using Java records and sealed interfaces with Jackson polymorphic deserialization — entities never exposed directly
3. **Service Layer**: All business logic isolated in `@Service` classes; controllers handle only routing and input
4. **Repository Pattern**: Spring Data JPA repositories with custom JPQL queries where needed
5. **Filter Chain**: `JwtAuthenticationFilter` sits before `UsernamePasswordAuthenticationFilter` to validate tokens on every request
6. **Global Exception Handling**: `@RestControllerAdvice` with `@ExceptionHandler` methods returns consistent JSON error responses

### Entity Hierarchy

```
AbstractBook (JPA Base Entity — Single Table Inheritance)
├── PhysicalBook      (shelf location, copy count)
└── AbstractDigitalBook
    ├── EBook         (download URL, file format)
    └── AudioBook     (narrator, duration)

UserDetails (Spring Security)
├── Member (Entity Class)
└── Admin (Auth)

```

### Security Filter Chain

```
Incoming Request
      │
      ▼
JwtAuthenticationFilter
      │  Extracts & validates Bearer token
      │  Sets SecurityContextHolder
      ▼
SecurityFilterChain
      │  Route-level role checks
      ▼
@PreAuthorize (method-level checks)
      │
      ▼
Controller → Service → Repository
```

---

## 🛠️ Technologies

| Category | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.x |
| Security | Spring Security 7 · jjwt (io.jsonwebtoken) |
| Persistence | Spring Data JPA · Hibernate |
| Database | MySQL (`ddl-auto=update`) |
| Validation | Jakarta Bean Validation (`@Valid`, custom constraints) |
| Build | Maven |
| IDE | VS Code / IntelliJ IDEA |

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- MySQL 8+
- Maven 3.8+

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/Shahaam-Sid/LMS-Java.git
   cd LMS-Java/LMS-Spring-Backend
   ```

2. **Configure the database**

   Create a MySQL database:

   ```sql
   CREATE DATABASE lms_db;
   ```

   Add `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/lms_db
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD

   spring.jpa.hibernate.ddl-auto=update

   app.jwt.secret=YOUR_SECRET_KEY
   ```

3. **Build and run**

   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

### First Run

- On first startup, Hibernate will auto-create all tables via `ddl-auto=update`
- Register your first `ADMIN` account via `POST /api/auth/register` with role `ADMIN`
- Use `POST /api/auth/login` to receive a JWT token
- Pass the token as `Authorization: Bearer <token>` on all subsequent requests

---

## 📡 API Reference

All endpoints are prefixed with `/api/v1`. Protected endpoints require a valid JWT Bearer token.

### Auth

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a new member or admin |
| `POST` | `/api/v1/auth/authenticate` | Authenticate and receive JWT |

### Books

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/books` | Get all books |
| `GET` | `/api/v1/books/{isbn}` | Get book by ISBN |
| `GET` | `/api/v1/books/search?q=` | Search books |
| `PATCH` | `/api/v1/books` | Add a new book |
| `PUT` | `/api/v1/books/{isbn}` | Update book details |
| `DELETE` | `/api/v1/books/{isbn}` | Delete a book |

### Members

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/members` | Get all members |
| `GET` | `/api/v1/members/{id}` | Get member by ID |
| `GET` | `/api/v1/members/active` | Get all active members |
| `GET` | `/api/v1/members/search?q=` | Search members |
| `POST` | `/api/v1/members` | Add a new member profile |
| `DELETE` | `/api/v1/members/{id}` | Delete a member |
| `PATCH` | `/api/v1/members/{id}` | Update a member

### Loans

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/loans` | Get all loans |
| `GET` | `/api/v1/loans/{id}` | Get loan by ID |
| `GET` | `/api/v1/loans/active` | Get all active loans |
| `GET` | `/api/v1/loans/active/overdue` | Get all overdue loans |
| `POST` | `/api/v1/loans` | Create loan transaction |
| `PATCH` | `/api/v1/loans/{id}` | Close lone transaction |

### Reservations

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/reservations{isbn}` | Get all trnasactions by book |
| `GET` | `/api/reservations/{id}` | Get all trnasactions by member |
| `POST` | `/api/reservations` | Reserve a book |

---

## 🔐 Security

### JWT Authentication Flow

```
POST /api/v1/auth/login  { email, password }
          │
          ▼
  UserDetailsService loads user
  BCrypt verifies password
          │
          ▼
  JwtService generates signed token (HS256)
          │
          ▼
  Response: { token: "eyJ..." }
          │
          ▼ (all subsequent requests)
  Authorization: Bearer <token>
          │
          ▼
  JwtAuthenticationFilter validates token
  SecurityContext populated
          │
          ▼
  Request reaches controller
```

---

## ⚠️ Exception Handling

All exceptions are handled globally via `@RestControllerAdvice`

| Exception | HTTP Status | Scenario |
|---|---|---|
| `BookNotFoundException` | `404` | ISBN not found |
| `BookNotAvailableException` | `409` | All copies borrowed |
| `BookAlreadyBorrowedException` | `409` | Member already has this book |
| `MemberNotFoundException` | `404` | Member ID not found |
| `MemberLimitExceededException` | `409` | Member has 3 active borrows |
| `DuplicateISBNException` | `409` | ISBN already exists |
| `CannotDeleteEntityException` | `409` | Active transaction blocks deletion |
| `InvalidPasswordException` | `400` | Password fails requirements |
| `MethodArgumentNotValidException` | `400` | Bean validation failure |
| `AccessDeniedException` | `403` | Insufficient role |
| `AuthenticationException` | `401` | Invalid or expired JWT |

---

## 📊 Key Constraints

- **Borrow Limit**: Members can borrow a maximum of 3 books simultaneously
- **Borrow Duration**: Standard 14-day borrowing period; late fees apply beyond this
- **ISBN Uniqueness**: ISBN must be unique across the entire catalog
- **Reservation Queue**: FIFO — first reserved, first notified when book becomes available
- **Deletion Guard**: Books or members with active transactions cannot be deleted
- **Token Expiry**: JWT tokens expire after 24 hours (configurable via `app.jwt.expiration-ms`)

---

## 🚧 Future Enhancements

- JavaFX desktop frontend (currently in development — see `LMS-JavaFX-Frontend/`)
- Book renewal functionality
- Email notifications for overdue books and reservation readiness
- Pagination and sorting on list endpoints
- Swagger / OpenAPI documentation
- Docker Compose setup for easy deployment

---

## 📄 License

This project is open-source and available for educational purposes.

---

## 👤 Author

**Muhammad Shahaam Siddiqui**

- GitHub: [@Shahaam-Sid](https://github.com/Shahaam-Sid)
- Email: muhammadgc821@gmail.com

---

## 📞 Support

For issues, questions, or suggestions, please open an issue in the GitHub repository.

---

**Last Updated**: June 2026
**Version**: 2.0.0
