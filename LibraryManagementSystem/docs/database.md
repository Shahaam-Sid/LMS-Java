# Database — Library Management System

## Overview

The LMS uses **MySQL 8** as its persistence layer, accessed via direct JDBC (Connector/J `8.3.0`). The database is named `lms_db` and contains six tables covering books, members, workers, transactions, reservations, and password credentials.

See [`sql/schema.sql`](../sql/schema.sql) for the complete SQL to recreate the database from scratch.

---

## Setup

### Prerequisites

- MySQL 8.0+
- MySQL Connector/J `8.3.0` on the classpath (declared in `pom.xml`)

### Creating the Database

```bash
mysql -u root -p < sql/schema.sql
```

Or manually in MySQL Workbench / shell:

```sql
CREATE DATABASE IF NOT EXISTS lms_db;
USE lms_db;
-- then run the table definitions from schema.sql
```

### Connection

Database connection details are managed through `DBConnection.java` (`com.library.db`). Update the credentials there before running the application:

```java
private static final String URL  = "jdbc:mysql://127.0.0.1:3306/lms_db";
private static final String USER = "root";
private static final String PASS = "your_password";
```

---

## Tables

### `books`

Stores all book records across all types. Book-type-specific fields are nullable — only the columns relevant to the book's `book_type` are populated.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `isbn` | `VARCHAR(13)` | NO | Primary key |
| `title` | `VARCHAR(50)` | NO | Book title |
| `author` | `VARCHAR(50)` | NO | Author name |
| `genre` | `VARCHAR(20)` | NO | Genre |
| `published_year` | `SMALLINT` | NO | Publication year |
| `book_status` | `ENUM` | NO | `AVAILABLE`, `BORROWED`, `RESERVED`, `LOST`, `UNDER_MAINTENANCE` |
| `book_type` | `ENUM` | NO | `PHYSICAL`, `EBOOK`, `AUDIOBOOK` |
| `shelf_location` | `VARCHAR(25)` | YES | Physical books only |
| `total_copies` | `INT` | YES | Physical books only |
| `available_copies` | `INT` | YES | Physical books only |
| `download_url` | `VARCHAR(2000)` | YES | EBook / AudioBook only |
| `file_format` | `VARCHAR(30)` | YES | EBook / AudioBook only |
| `file_size_mb` | `DOUBLE(8,2)` | YES | EBook / AudioBook only |
| `narrator` | `VARCHAR(100)` | YES | AudioBook only |

**Indexes:** `FULLTEXT` index on `title`, `author`, `isbn`, `genre`, `shelf_location`, `narrator`, `file_format` — used by the search feature.

**Java mapping:** `BookServices.mapResultSetToBook()` reads `book_type` and instantiates the correct subclass (`PhysicalBook`, `EBook`, or `AudioBook`).

---

### `members`

Stores registered library members.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `member_id` | `CHAR(9)` | NO | Primary key |
| `member_name` | `VARCHAR(35)` | NO | Full name |
| `phone` | `VARCHAR(11)` | NO | Contact number |
| `email` | `VARCHAR(254)` | NO | Email address |
| `address` | `VARCHAR(55)` | NO | Home address |
| `age` | `INT` | NO | Age (must be > 1910 — constraint enforces valid birth year) |
| `member_status` | `ENUM` | NO | `ACTIVE`, `SUSPENDED`, `EXPIRED` |

**Indexes:** `FULLTEXT` index on `member_id`, `member_name`, `email`, `phone`.

**Constraint:** `check_age_members` — `age > 1910` (rejects obviously invalid ages).

---

### `workers`

Stores library staff (admins). Mirrors the `members` structure but without a status field — workers are always active or removed entirely.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `worker_id` | `CHAR(9)` | NO | Primary key |
| `worker_name` | `VARCHAR(35)` | NO | Full name |
| `phone` | `CHAR(11)` | YES | Contact number |
| `email` | `VARCHAR(254)` | NO | Email address |
| `address` | `VARCHAR(55)` | NO | Home address |
| `age` | `INT` | NO | Age (must be > 1910) |

**Indexes:** `FULLTEXT` index on `worker_id`, `worker_name`, `email`, `phone`.

**Constraint:** `check_age_workers` — `age > 1910`.

---

### `salt_n_hash`

Stores hashed passwords for workers. Intentionally separated from `workers` so credential data is isolated and never accidentally included in general worker queries.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `worker_id` | `CHAR(9)` | NO | Primary key, FK → `workers.worker_id` |
| `password_salt` | `CHAR(32)` | NO | Random salt generated at registration |
| `password_hash` | `CHAR(64)` | NO | PBKDF2 hash of `salt + password` |

**Foreign key:** `fk_worker_id_snh` → `workers(worker_id)` — `ON DELETE CASCADE`. Deleting a worker automatically removes their credentials.

**Java:** `PasswordUtils.java` handles salt generation, hashing, and verification. Plaintext passwords are never stored or logged.

---

### `transactions`

Records every borrow event. A row is created when a book is borrowed and updated (with `return_date` and `fine_amount`) when it is returned.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `transaction_id` | `CHAR(36)` | NO | Primary key (UUID) |
| `member_id` | `CHAR(9)` | NO | FK → `members.member_id` |
| `isbn` | `VARCHAR(13)` | NO | FK → `books.isbn` |
| `borrow_date` | `DATE` | NO | Date the book was issued |
| `due_date` | `DATE` | NO | Expected return date (borrow + 14 days) |
| `return_date` | `DATE` | YES | Actual return date — NULL while book is still out |
| `fine_amount` | `DECIMAL(5,2)` | NO | Late fee in Rs., defaults to `0.00` |

**Foreign keys:**
- `transaction_member_fk` → `members(member_id)` — `ON DELETE CASCADE`
- `transaction_book_fk` → `books(isbn)` — `ON DELETE CASCADE`

**Constraints:**
- `check_due_date` — `due_date > borrow_date`
- `check_return_date` — `return_date >= borrow_date`

**Active borrow check:** A transaction with `return_date IS NULL` is an active (unreturned) borrow. The 3-book member limit is enforced in `TransactionServices` by counting rows where `member_id = ? AND return_date IS NULL`.

---

### `reservations`

Tracks the reservation queue per book. Each row represents one member's place in line for a specific book.

| Column | Type | Nullable | Description |
|---|---|---|---|
| `isbn` | `VARCHAR(13)` | NO | FK → `books.isbn`, part of composite PK |
| `member_id` | `CHAR(9)` | NO | FK → `members.member_id`, part of composite PK |
| `notified` | `TINYINT(1)` | NO | `0` = not yet notified, `1` = notified (book ready) |
| `position` | `INT` | NO | Queue position for this book (1 = next in line) |

**Primary key:** Composite `(isbn, member_id)` — a member can only reserve the same book once.

**Unique key:** `(isbn, position)` — no two members can hold the same queue position for the same book.

**Foreign keys:**
- `reservation_book_fk` → `books(isbn)` — `ON DELETE CASCADE`. Deleting a book clears its reservation queue.
- `reservation_member_fk` → `members(member_id)` — `ON DELETE RESTRICT`. A member with active reservations cannot be deleted.

---

## Relationships

```
workers ──────────── salt_n_hash
  (1)                    (1)        worker_id FK, CASCADE DELETE

members ──────────── transactions
  (1)                    (N)        member_id FK

books ────────────── transactions
  (1)                    (N)        isbn FK

members ──────────── reservations
  (1)                    (N)        member_id FK, RESTRICT DELETE

books ────────────── reservations
  (1)                    (N)        isbn FK, CASCADE DELETE
```

---

## Design Notes

**Single `books` table for all book types**
All three book types (`PHYSICAL`, `EBOOK`, `AUDIOBOOK`) share one table with nullable type-specific columns rather than separate tables or a joined inheritance structure. This keeps queries simple at the current phase. The `book_type` column tells the Java layer which subclass to instantiate.

**`transactions` as the source of truth for borrow state**
Whether a member has an active borrow is determined by querying `transactions` (`return_date IS NULL`), not by a field on the member. This avoids stale state and keeps the member record clean.

**Password table isolation**
`salt_n_hash` is deliberately separate from `workers`. This means a `SELECT *` on workers never accidentally exposes credential data, and the table can have tighter access controls in a production environment.

**FULLTEXT indexes for search**
MySQL's `FULLTEXT` indexes on `books`, `members`, and `workers` support the flexible multi-field search feature without requiring multiple `LIKE` clauses per query.

**`CHAR` vs `VARCHAR` for IDs**
`member_id` and `worker_id` use `CHAR(9)` (fixed-length) since IDs always have a known fixed format. `isbn` uses `VARCHAR(13)` to accommodate both ISBN-10 and ISBN-13 formats. `transaction_id` uses `CHAR(36)` for UUID storage.