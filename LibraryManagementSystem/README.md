# Library Management System

A comprehensive Java-based Library Management System designed to streamline library operations including book inventory management, member registration, and borrowing/return transactions.

## 📋 Table of Contents

- [Features](#-features)
- [Project Structure](#️-project-structure)
- [Architecture](#️-architecture)
- [Technologies](#️-technologies)
- [Getting Started](#-getting-started)
- [Usage](#-usage)
- [System Components](#-system-components)
- [Exception Handling](#️-exception-handling)
- [Security Features](#-security-features)
- [Key Constraints](#-key-constraints)
- [Transaction Flow](#-transaction-flow)
- [Future Enhancement](#-future-enhancements)
- [Contributing](#-contributing)
- [License](#-license)
- [Author](#-author)
- [Support](#-support)

## ✨ Features

### Book Management

- **Multi-format Support**: Manage Physical Books, E-Books, and AudioBooks
- **Inventory Tracking**: Track available copies and book status
- **Book Search**: Search by title, author, ISBN, or genre
- **Catalog Management**: Add, remove, and view all books in the system
- **Updating Option**: Change Shelf Location, Total and Available Copies of books

### Member Management

- **Member Registration**: Register new library members with validation
- **Member Profiles**: Maintain member information (ID, name, contact, address, age)
- **Borrow Limits**: Enforce maximum 3 concurrent book borrowings per member
- **Member Search**: Search by name, ID, or email
- **Status Tracking**: Track member status (ACTIVE, INACTIVE)
- **Updating Option**: Change Name, Phone, Email, Address and Status

### Transaction Management

- **Book Borrowing**: Issue books to members with automatic due date calculation
- **Book Returns**: Process returns and calculate late fees
- **Overdue Tracking**: Monitor overdue transactions and calculate fines
- **Transaction History**: View all library transactions and borrowing records
- **Late Fee Calculation**: Automatic fine calculation (Rs. per day overdue)

### Staff Management

- **Worker Accounts**: Add library staff with secure password management
- **Authentication**: Login system with password hashing and verification
- **Updating Option**: Change Name, Phone, Email, Address and Password

### Search Functionality

- **Advanced Search**: Search books, members, and workers across multiple fields
- **Flexible Querying**: Search by various attributes (title, author, ISBN, genre, email, etc.)

### Reserving Functionality

- **Reserve**: Reserve Books if are not available to Borrow
- **Notify**: Notify when book is ready to be borrowed

## 🏗️ Project Structure

```
main
└───java
    └───com
        └───library
            │   Main.java
            │   
            ├───db
            │       DBConnection.java
            │       DBUtility.java
            │       
            ├───enums
            │       BookStatus.java
            │       BookType.java
            │       MemberStatus.java
            │       
            ├───exceptions
            │       BookAlreadyBorrowedException.java
            │       BookNotAvailableException.java
            │       BookNotFoundException.java
            │       CannotDeleteEntityException.java
            │       ChangesNotSavedException.java
            │       DatabaseException.java
            │       DuplicateISBNException.java
            │       DuplicatePupilException.java
            │       InvalidPasswordException.java
            │       MemberLimitExceededException.java
            │       MemberNotFoundException.java
            │       NoActiveBorrowRecordFoundException.java
            │       NoOutputReceivedException.java
            │       WorkerNotFoundException.java
            │       
            ├───interfaces
            │       Borrowable.java
            │       Pupil.java
            │       Reservable.java
            │       Searchable.java
            │       
            ├───models
            │   │   AbstractPupil.java
            │   │   Admin.java
            │   │   Member.java
            │   │   PasswordUtils.java
            │   │   Transaction.java
            │   │   ValidationUtils.java
            │   │   
            │   └───book
            │           AbstractBook.java
            │           AbstractDigitalBook.java
            │           AudioBook.java
            │           EBook.java
            │           PhysicalBook.java
            │           
            ├───services
            │       BookServices.java
            │       MemberServices.java
            │       TransactionServices.java
            │       WorkerServices.java
            │       
            └───ui
                    LibraryMenu.java
```

## 🏛️ Architecture

[**Documentation and UMLs**](./LibraryManagementSystem/docs/architecture.md)
[**Database and ERDs**](./LibraryManagementSystem/docs/database.md)

### Design Patterns Used

1. **Abstract Class Pattern**: `AbstractBook` and `AbstractDigitalBook` for code reuse and common functionality
2. **Interface Segregation**: Separate interfaces for different concerns (`Borrowable`, `Searchable`, `Reservable`, `Pupil`)
3. **Service Layer**: Business logic separated into service classes
4. **Exception Handling**: Custom exceptions for specific error scenarios
5. **Utility Classes**: Helper methods in `ValidationUtils` and `PasswordUtils`

### Object Hierarchy

```
AbstractPupil (Abstract Base)
├── Member (extends AbstractPupil, implements Searchable)
└── Admin (extends AbstractPupil, implements Searchable)

AbstractBook (Abstract Base, implements Borrowable, Reservable)
├── PhysicalBook
└── AbstractDigitalBook (Abstract)
    ├── EBook
    └── AudioBook
```

### Core Services

- **BookServices**: Book CRUD operations and search functionality
- **MemberServices**: Member registration and management
- **TransactionServices**: Borrowing, returning, and tracking transactions
- **WorkerServices**: Worker/Admin management

## 🛠️ Technologies

- **Language**: Java (Java 21)
- **IDE**: VS Code
- **Build System**: Maven
- **Data Storage**: MySQL
- **Security**: Password hashing with PBKDF2

## 🚀 Getting Started

### Prerequisites

- Java 21
- A Java IDE or command-line compiler

### Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/LibraryManagementSystem.git
   cd LibraryManagementSystem
   ```

2. **Compile the source code**

   ```bash
   javac -d bin src/com/library/**/*.java
   # or
   find src -name "*.java" | xargs javac -d bin
   # or on Windows:
   dir /s /b src\*.java > sources.txt && javac -d bin @sources.txt
   ```

3. **Run the application**

   ```bash
   java -cp bin com.library.ui.LibraryMenu
   ```

### First Run

- On first launch, you'll be prompted to create an admin account
- Enter admin credentials (Worker ID, Name, Email, Password, etc.)
- After admin creation, you'll be able to login to the main menu

## 💻 Usage

### Main Menu Navigation

```
1. Books           - Manage book inventory
2. Members         - Register and manage members
3. Borrow/Return   - Process book transactions
4. Search          - Search across system
5. Workers         - Manage library staff
6. Reserve         - Cancel, View, Peek Reservation Queue
9. Print Menu      - Reprint menu options
0. Log out         - Exit system
```

### Common Operations

#### Adding a Book

1. Select `Books` → `Add Physical/E-Book/Audio Book`
2. Enter ISBN, title, author, genre, publication year
3. For Physical: Enter shelf location and number of copies
4. For Digital: Enter download URL and file format

#### Registering a Member

1. Select `Members` → `Register Member`
2. Provide Member ID, name, contact information, age
3. Member automatically gets ACTIVE status

#### Borrowing a Book

1. Select `Borrow/Return` → `Borrow Book`
2. Enter ISBN and Member ID
3. System validates member limit (max 3 books) and book availability
4. Due date is automatically calculated (14 days)

#### Reserving a Book

1. Make an attempt to Borrow Book
2. If book not available to Borrow Select `Reserve`

#### Returning a Book

1. Select `Borrow/Return` → `Return Book`
2. Enter ISBN and Member ID
3. System calculates any overdue fines (if applicable)

#### Searching

1. Select `Search`
2. Choose search type (Books, Members, Workers)
3. Enter search query (flexible - searches multiple fields)

## 📦 System Components

### Models

- **AbstractPupil**: Base class for users (Members and Admin)
- **Member**: Library member with borrowing privileges
- **Admin**: Library worker with management privileges
- **Transaction**: Records book borrowing and return events
- **AbstractBook**: Base class for all book types
- **PhysicalBook**: Physical copies with shelf locations
- **EBook**: Digital e-books with download URLs
- **AudioBook**: Audio versions with narrator information

### Enums

- **BookStatus**: AVAILABLE, BORROWED, RESERVED
- **BookType**: PHYSICAL, EBOOK, AUDIOBOOK
- **MemberStatus**: ACTIVE, INACTIVE, SUSPENDED

### Interfaces

- **Borrowable**: Methods for borrowing/returning books
- **Searchable**: Methods for system-wide search capability
- **Reservable**: Book reservation functionality
- **Pupil**: Base interface for users

## ⚠️ Exception Handling

The system uses custom exceptions for better error handling:

- `BookNotFoundException`: When a book ISBN is not found
- `BookNotAvailableException`: When all copies are borrowed
- `MemberNotFoundException`: When a member ID is not found
- `WorkerNotFoundException`: When a worker is not found
- `MemberLimitExceededException`: When member exceeds borrow limit (>3)
- `DuplicateISBNException`: When adding a book with existing ISBN
- `DuplicatePupilException`: When registering duplicate member/worker
- `InvalidPasswordException`: When password doesn't meet requirements
- `BookAlreadyBorrowedException`: When a book is already borrowed by the Member
- `CannotDeleteEntityException`: When Book/Member cannot be deleted due to an Active Transaction
- `ChangesNotSavedException`: When for some reason changes aren't saved to Database
- `DatabaseException`: When Database returns an Error
- `NoActiveBorrowRecordFoundException`: When no Borrow found
- `NoOutputRecievedException`: When no output returned from Database

## 🔐 Security Features

- **Password Hashing**: Passwords are hashed with salt before storage
- **Password Verification**: Secure verification without storing plaintext
- **Input Validation**: All inputs validated for length and format
- **Worker Authentication**: Login required to access system

## 📊 Key Constraints

- **Borrow Limit**: Members can borrow maximum 3 books simultaneously
- **Borrow Duration**: Standard 14-day borrowing period
- **Late Fee**: Calculated per day overdue
- **Member Registration**: Minimum age requirement validation
- **ISBN Format**: Must be unique in system
- **ID Format**: Must follow validation rules

## 🔄 Transaction Flow

### Borrowing Flow

```
Member exists? → Book exists? → Book available? → Member < 3 books? 
→ Create Transaction → Update book status → Add to member transactions → Issue book
```

### Return Flow

```
Transaction exists? → Calculate overdue days → Calculate fine 
→ Update transaction → Restore book availability → Return status
```

## 🚧 Future Enhancements

- Book renewal functionality
- Email notifications for overdue books
- Report generation
- Data export functionality
- GUI interface (JavaFX/Swing)
- REST API

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Create a Pull Request

## 📄 License

This project is open-source and available for educational purposes.

## 👤 Author

**Muhammad Shahaam Siddiqui**

- GitHub: [@Shahaam-Sid](https://github.com/Shahaam-Sid)
- Email: muhammadgc821@gmail.com

## 📞 Support

For issues, questions, or suggestions, please open an issue in the GitHub repository.

---

**Last Updated**: May 2026
**Version**: 1.0.0
