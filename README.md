# LMS-Java

A Library Management System Software

### Project Structure

```
LMS-Java
│   .gitignore
│   README.md
│
└───LibraryManagementSystem
    └───src
        └───com
            └───library
                ├───enums
                │       BookStatus.java
                │       BookType.java
                │       MemberStatus.java
                │
                ├───exceptions
                │       BookNotAvailableException.java
                │       BookNotFoundException.java
                │       DuplicateISBNException.java
                │       DuplicatePupilException.java
                │       MemberLimitExceededException.java
                │       MemberNotFoundException.java
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
``