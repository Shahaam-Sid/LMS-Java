# Architecture — Library Management System

## Overview

The LMS is a console-based Java application built around a layered architecture with a strong OOP foundation. The system is intentionally designed without a database or GUI in this phase — all data lives in memory — so the focus remains on clean class design, interface contracts, and separation of concerns.

The application is divided into four layers:

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

**Why `PBKDF2` for password hashing?**
Admin passwords are hashed with a salt using PBKDF2 (via `PasswordUtils`) rather than stored in plaintext. This is industry-standard for credential storage even in small systems, and establishes the right habit before a database is added.

---

## Data Flow — Borrow a Book

```
LibraryMenu.borrowBook()
    │
    ▼
TransactionServices.borrowBook(isbn, memberId)
    │
    ├── MemberServices.getMember(memberId)     → throws MemberNotFoundException
    ├── BookServices.getBook(isbn)             → throws BookNotFoundException
    ├── book.isAvailable()                     → throws BookNotAvailableException
    ├── member.getBorrowedTransactions().size() → throws MemberLimitExceededException
    │
    ├── new Transaction(member, book)
    ├── book.borrow(member)                    → updates availableCopies, status
    └── member.addTransaction(transaction)
```

---

## Diagrams

[**Models & Services**](./diagrams/Library-Management-System.jpg)
[**Enumerations**](./diagrams/Enumerations.jpg)
[**Exceptions & Validations**](./diagrams/Exceptions%20and%20Validations.jpg)

---

## What's Not Here Yet

This document reflects **Phase 2** of the project. The following will be added in later phases:

- **File I/O / Database (Phase 3)**: A persistence layer will sit below Services. Models will remain unchanged — Services will delegate save/load to a Repository layer.
- **Spring Boot / REST API (Phase 4)**: Services are already decoupled from the UI, so exposing them as REST endpoints will not require restructuring the business logic.
- **JavaFX GUI (Phase 5)**: Same reason — `LibraryMenu` is the only UI-aware class, so replacing it with a JavaFX controller is a clean swap.