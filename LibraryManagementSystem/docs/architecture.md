# Architecture — Library Management System

## Overview

The LMS is a console-based Java application built around a layered architecture with a strong OOP foundation. The system has progressed from in-memory storage (Phase 2) to a MySQL-backed persistence layer (Phase 3), with all data now read from and written to a relational database via direct JDBC.

The application is divided into five layers:

```
┌─────────────────────────────┐
│         UI Layer            │  LibraryMenu.java — console I/O only
├─────────────────────────────┤
│      Service Layer          │  BookServices, MemberServices,
│                             │  TransactionServices, WorkerServices
├─────────────────────────────┤
│       Model Layer           │  AbstractBook, AbstractPupil,
│                             │  Member, Admin, Transaction, ...
├─────────────────────────────┤
│    Utility / Support        │  Enums, Exceptions,
│                             │  ValidationUtils, PasswordUtils
├─────────────────────────────┤
│     Persistence Layer       │  MySQL 8 via JDBC (Connector/J 8.3.0)
│                             │  SQL embedded directly in Service classes
└─────────────────────────────┘
```

---

## Package Structure and Responsibilities

| Package | Responsibility |
|---|---|
| `com.library.ui` | Console menus and user interaction only — no business logic |
| `com.library.services` | All business logic — validation, rules enforcement, data operations |
| `com.library.models` | Domain objects — plain data + behaviour tightly tied to the entity |
| `com.library.interfaces` | Contracts that define what an object *can do*, independent of what it *is* |
| `com.library.enums` | Fixed value sets for status and type fields |
| `com.library.exceptions` | Custom exceptions for domain-specific error scenarios |

---

## Inheritance Hierarchy

### Members (Users)

```
AbstractPupil  (abstract)
├── Member     — library member with borrowing privileges
└── Admin      — library staff with management privileges
```

`AbstractPupil` holds shared fields (name, address, email, phone, age) and enforces `isAdmin()` as an abstract method, which each subclass answers differently. Both implement `Searchable` so they can be found through the unified search system.

### Books

```
AbstractBook  (abstract, implements Borrowable, Reservable)
├── PhysicalBook           — has shelfLocation and totalCopies
└── AbstractDigitalBook    (abstract)
    ├── EBook              — has downloadURL and format
    └── AudioBook          — has narrator
```

The two-level book hierarchy exists because digital books share a `downloadURL` and `format` that physical books don't have, but EBook and AudioBook still differ enough to warrant separate classes. `AbstractBook` carries the fields common to all books (ISBN, title, author, genre, publishedYear, status, type).

---

## Interface Design

Four interfaces define behavioural contracts in this system:

**`Borrowable`** — implemented by `AbstractBook`
Defines the full borrow/return contract: `borrow()`, `returnItem()`, `isAvailable()`, `getAvailable()`, `calculateDueDate()`. Placed on the book rather than the member because availability is a property of the book, not the borrower.

**`Reservable`** — implemented by `AbstractBook`
Handles reservation queue operations: `reserve()`, `cancelReservation()`, `getQueuePosition()`, `getQueue()`. Separated from `Borrowable` because not all borrowable items may support reservations in future phases.

**`Searchable`** — implemented by `AbstractBook`, `Member`, `Admin`
Single method: `matchesQuery(query)`. Placing this on every searchable entity means `BookServices`, `MemberServices`, and `WorkerServices` all use the same search pattern — loop, call `matchesQuery`, collect results.

**`Pupil`** — implemented by `Member` and `Admin`
Defines `isAdmin()`. Acts as a common type for any user of the system regardless of role.

---

## Key Design Decisions

**Why abstract classes instead of just interfaces for books and members?**
`AbstractBook` and `AbstractPupil` hold real shared state (fields) and shared behaviour that every subclass inherits unchanged. Interfaces can't hold instance fields, so abstract classes were the right tool here. The interfaces (`Borrowable`, `Searchable`, etc.) then layer *additional contracts* on top.

**Why is business logic in Services and not in Models?**
Models know about themselves — a `Member` knows its own borrowed transactions. But rules like "a member cannot borrow more than 3 books" or "a book with no available copies cannot be issued" involve coordinating multiple objects. That coordination belongs in `TransactionServices`, not in `Member` or `AbstractBook`.

**Why custom exceptions instead of generic ones?**
`throw new Exception("Book not found")` gives the caller no structured information. `throw new BookNotFoundException(isbn)` lets the UI layer catch specific scenarios and show the right message, without relying on parsing strings. Each custom exception maps to exactly one user-facing error scenario.

**Why direct JDBC instead of a Repository pattern or ORM?**
At this phase the goal is understanding how Java talks to a database at the lowest level — what a `Connection` is, how `PreparedStatement` works, how a `ResultSet` maps to an object. An ORM like Hibernate would hide all of that. A Repository pattern would be the right next step, but adds a layer of abstraction before the basics are solid. Direct JDBC in Services is the deliberate choice here, with a planned refactor noted in the future enhancements section.


Admin passwords are hashed with a salt using PBKDF2 (via `PasswordUtils`) rather than stored in plaintext. This is industry-standard for credential storage even in small systems, and establishes the right habit before a database is added.

---

## Data Flow — Borrow a Book

```
LibraryMenu.borrowBook()
    │
    ▼
TransactionServices.borrowBook(isbn, memberId)
    │
    ├── MemberServices.getMember(memberId)      → SELECT from members → throws MemberNotFoundException
    ├── BookServices.getBook(isbn)              → SELECT from books   → throws BookNotFoundException
    ├── book.isAvailable()                      → throws BookNotAvailableException
    ├── member.getBorrowedTransactions().size() → throws MemberLimitExceededException
    │
    ├── new Transaction(member, book)
    ├── INSERT transaction into transactions table
    ├── UPDATE books SET available_copies, status
    └── UPDATE members borrowed list (or rely on transactions join)
```

---

## Persistence Layer — MySQL via Direct JDBC

### Technology

- **Database**: MySQL 8
- **Driver**: MySQL Connector/J `8.3.0` (`com.mysql.cj.jdbc.Driver`)
- **Approach**: Direct JDBC — SQL statements are written and executed inside Service classes, with no ORM or Repository abstraction layer

### How it works

Each Service class is responsible for its own database operations. When a service method needs to read or write data, it opens a connection, prepares a statement, executes it, maps the `ResultSet` back to model objects, and closes the connection.

A typical pattern inside a service method:

```java
// Inside BookServices.java
public AbstractBook getBook(String isbn) throws BookNotFoundException {
    String sql = "SELECT * FROM books WHERE isbn = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, isbn);
        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) throw new BookNotFoundException(isbn);
        return mapResultSetToBook(rs);   // builds the correct subclass

    } catch (SQLException e) {
        throw new RuntimeException("Database error: " + e.getMessage());
    }
}
```

### Connection management

Database connections are managed through a central `DatabaseConnection` utility class (in `com.library.models` or a dedicated `db` package) that holds the connection URL, credentials, and hands out connections on demand. `PreparedStatement` is used everywhere — never string-concatenated SQL — to prevent SQL injection.

### How Services map SQL rows to Models

Since the database stores a `book_type` column (`PHYSICAL`, `EBOOK`, `AUDIOBOOK`), each service has a private `mapResultSetToBook()` helper that reads the type and instantiates the correct subclass (`PhysicalBook`, `EBook`, or `AudioBook`). The model classes themselves are unchanged — they remain plain Java objects with no JDBC code inside them.

### Trade-off of this approach

Embedding SQL directly in Services is straightforward and keeps the codebase flat. The trade-off is that Services now have two responsibilities: business logic *and* data access. This is acceptable for the current phase. In a future phase, extracting SQL into dedicated Repository classes (one per entity) would cleanly separate these concerns and make the Services easier to unit test.

### Database schema

See [`docs/database.md`](./database.md) for full table definitions, relationships, and setup instructions.
See [`sql/schema.sql`](../sql/schema.sql) for the complete SQL to recreate the database from scratch.

---

## Diagrams

| Diagram | File |
|---|---|
| Library Management System | [diagrams/Library-Management-System.jpg](diagrams/Library-Management-System.jpg) |
| Enumerations | [diagrams/Enumerations.jpg](diagrams/Enumerations.jpg) |
| Exceptions & Utilities | [diagrams/exceptions-and-validations.jpg](diagrams/Exceptions%20and%20Validations.jpg) |
| Entity Relationship Diagram | [diagrams/erd.png](diagrams/erd.png) |

---

## What's Not Here Yet

This document reflects **Phase 3** of the project. The following will be added in later phases:


- **Spring Boot / REST API (Phase 4)**: Services are already decoupled from the UI, so exposing them as REST endpoints will not require restructuring the business logic.
- **JavaFX GUI (Phase 5)**: `LibraryMenu` is the only UI-aware class, so replacing it with a JavaFX controller is a clean swap.