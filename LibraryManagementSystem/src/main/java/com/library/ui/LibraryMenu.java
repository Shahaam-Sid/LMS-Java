package com.library.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.library.exceptions.BookAlreadyBorrowedException;
import com.library.exceptions.BookNotAvailableException;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.CannotDeleteEntityException;
import com.library.exceptions.ChangesNotSavedException;
import com.library.exceptions.DatabaseException;
import com.library.exceptions.DuplicateISBNException;
import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.InvalidPasswordException;
import com.library.exceptions.MemberLimitExceededException;
import com.library.exceptions.MemberNotFoundException;
import com.library.exceptions.NoActiveBorrowRecordFoundException;
import com.library.exceptions.WorkerNotFoundException;
import com.library.models.Admin;
import com.library.models.Member;
import com.library.models.PasswordUtils;
import com.library.models.Transaction;
import com.library.models.book.AbstractBook;
import com.library.models.book.AudioBook;
import com.library.models.book.EBook;
import com.library.models.book.PhysicalBook;
import com.library.services.BookServices;
import com.library.services.MemberServices;
import com.library.services.TransactionServices;
import com.library.services.WorkerServices;

public class LibraryMenu {
    private final BookServices bookServices;
    private final MemberServices memberServices;
    private final WorkerServices workerServices;
    private final TransactionServices transactionServices;
    private final Scanner scanner;
    private static final Logger logger = LoggerFactory.getLogger(LibraryMenu.class);

    public LibraryMenu(BookServices bookServices, MemberServices memberServices,
        WorkerServices workerServices, TransactionServices transactionServices) {

        this.bookServices = bookServices;
        this.memberServices = memberServices;
        this.workerServices = workerServices;
        this.transactionServices = transactionServices;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        checkFirstStart();
        login();
    }

    private void login() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n ==Library Management System - Login==");
            System.out.println("1.Log in \n0. Exit");
            int choice = readInt("Enter Choice > ");
            switch (choice) {
                case 1 -> {
                    try {
                        String workerID = readString("Worker ID: ");
                        Admin worker = workerServices.getWorker(workerID);
                        String password = readString("Password: ");
                        if (PasswordUtils.verifyPassword(password, worker.getSalt(), worker.getHash())) {
                            mainMenu();
                        } else {
                            System.out.println("Wrong Password");
                        }
                        
                    } catch (WorkerNotFoundException e) {
                        System.out.println("Not Found: " + e.getMessage());
                    } catch (DatabaseException e) {
                        System.out.println(e.getUserMessage());
                        logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                } case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice, Try Again");
            }
        }
        System.out.println("PROGRAM ENDED");
        scanner.close();
    }
    private void mainMenu() {
        System.out.println("==========================================");
        System.out.println("   Welcome to Library Management System   ");
        System.out.println("==========================================");

        boolean running = true;
        
        printMainMenu();

        while (running) {
            try {
                System.out.println("\n =======MAIN MENU=======\n");

                int choice = readInt("Enter Choice > ");

                switch (choice) {
                    case 1 -> {
                        printBookMenu();
                        bookMenu();
                    } case 2 -> {
                        printMemberMenu();
                        memberMenu();
                    } case 3 -> {
                        printTransactionMenu();
                        transactionMenu();
                    } case 4 -> {
                        printSearchMenu();
                        searchMenu();
                    } case 5 -> {
                        printWorkerMenu();
                        workerMenu();
                    } case 6 ->{
                        printReserveMenu();
                        reserveMenu();
                    } case 9 -> printMainMenu();
                    case 0 -> running = false;
                    default ->System.out.println("Invalid Choice, Try Again");
                }
            } catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        }
    }
    private void checkFirstStart() {
        if (workerServices.isEmpty()) {
            System.out.println("Add Admin\n");
            addWorkerOnStartup();
        }
    }


    // Books Menu and Functions
    private void bookMenu() {

        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n =======BOOK MENU=======\n");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> addPhysicalBook();
                case 2 -> addEBook();
                case 3 -> addAudioBook();
                case 4 -> veiwAllBooks();
                case 5 -> removeBook();
                case 6 -> updateBook();
                case 9 -> printBookMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }   
    private void addPhysicalBook() {
        System.out.println("\n ~~Add Physical Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre = readString("Genre: ");
        int year = readInt("Published Year: ");
        String shelf = readString("Shelf Location: ");
        int copies = readInt("Total Copies: ");

        try {
            bookServices.addNewBook(new PhysicalBook(isbn, title, author, genre, year, shelf, copies));
            System.out.println("Physical Book Added");
        } catch (DuplicateISBNException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void addEBook() {
        System.out.println("\n ~~Add E-Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre = readString("Genre: ");
        int year = readInt("Published Year: ");
        String url = readString("Download URL: ");
        String format = readString("Format: ");
        double fileSizeMB = readDouble("File Size (mb): ");

        try {
            bookServices.addNewBook(new EBook(isbn, title, author, genre, year, url, format, fileSizeMB));
            System.out.println("E-Book Added");
        } catch (DuplicateISBNException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void addAudioBook() {
        System.out.println("\n ~~Add Audio Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre = readString("Genre: ");
        int year = readInt("Published Year: ");
        String url = readString("Download URL: ");
        String format = readString("Format: ");
        String narrator = readString("Narrator: ");
        double fileSizeMB = readDouble("File Size (mb): ");

        try {
            bookServices.addNewBook(new AudioBook(isbn, title, author, genre, year, url, format, fileSizeMB, narrator));
            System.out.println("Audio Book Added");
        } catch (DuplicateISBNException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void veiwAllBooks() {
        
        List<AbstractBook> books = null;
        
        try {
            books = bookServices.getAllBooks();
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

        if (books == null || books.isEmpty()) {
            System.out.println("No Books Found");
            return;
        }

        System.out.println("\n ~~All Books~~");
        for (AbstractBook book : books) System.out.println(book);
    }
    private void updateBook() {
        String isbn = readString("Book ISBN to update: ");
        String status = readString("New Status (AVAILABLE/BORROWED/RESERVED/LOST/UNDER_MAINTENANCE, leave empty if unchanged): ");
        String shelfLocation = readString("New Shelf Location (leave empty if unchanged): ");
        String totalCopies = ("New Total Copies (leave empty if unchanged): ");
        String availableCopies = ("New Available Copies (leave empty if unchanged): ");

        try {
            bookServices.updateBook(isbn, status, shelfLocation, totalCopies, availableCopies);
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void removeBook() {
        String isbn = readString("Enter ISBN to remove: ");
        try {
            bookServices.removeBook(isbn);
            System.out.println("Book Removed");
        } catch (BookNotFoundException | ChangesNotSavedException | CannotDeleteEntityException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    } 


    // Member menu and functions
    private void memberMenu() {

        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n =======MEMBER MENU=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> registerMember();
                case 2 -> viewAllMembers();
                case 3 -> removeMember();
                case 4 -> updateMember();
                case 9 -> printMemberMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }
    private void registerMember() {
        System.out.println("\n ~~Register Member~~");
        String name = readString("Name: ");
        String phone = readString("Phone #: ");
        String email = readString("E-Mail: ");
        String address = readString("Address: ");
        int age = readInt("Year of Birth: ");

        try {
            try {
            Member member = new Member(name, phone, email, address, age);
            memberServices.registerMember(member);
            System.out.println("Member Added");
            System.out.println("Member ID: " + member.getMemberID());
            } catch (SQLException e) {
                throw new DatabaseException(e.getErrorCode(), e);
            }
        } catch (IllegalArgumentException | DuplicatePupilException | ChangesNotSavedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

    }
    private void viewAllMembers() {
        List<Member> members = null;
        try {
            members = memberServices.getAllMembers();
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

        if (members == null || members.isEmpty()) {
            System.out.println("No Members found");
            return;
        }

        System.out.println("\n ~~All Members~~");
        for (Member member : members) System.out.println(member);
    }
    private void updateMember() {
        String id = readString("Member ID to update: ");
        String name = readString("New Name (Leave Empty if Unchanged): ");
        String phone = readString("New Phone # (Leave Empty if Unchanged): ");
        String email = readString("New E-Mail (Leave Empty if Unchanged): ");
        String address = readString("New Address (Leave Empty if Unchanged): ");
        String status = readString("New Status (ACTIVE/SUSPENDED/EXPIRED, Leave Empty if Unchanged): ");
        try {
            memberServices.updateMember(id, name, phone, email, address, status);
        } catch (IllegalArgumentException | MemberNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void removeMember() {
        String id = readString("Member ID to remove: ");

        try {
            memberServices.removeMember(id);
        } catch (MemberNotFoundException | CannotDeleteEntityException | ChangesNotSavedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }


    // Transaction menu and function
    private void transactionMenu() {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n ====BORROW / RETURN MENU====");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> borrowBook();
                case 2 -> returnBook();
                case 3 -> viewAllTransactions();
                case 4 -> viewOverdueTransactions();
                case 9 -> printTransactionMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }
    private void borrowBook() {
        String isbn = readString("Enter ISBN: ");
        String memberID = readString("Enter Member ID: ");

        try {
            Transaction t = transactionServices.borrowBook(isbn, memberID);
            System.out.println("Book Issued");
            System.out.println("Due Date: " + t.getDueDate());
        } catch (BookNotFoundException | MemberNotFoundException e) {
            System.out.println("Not Found: " + e.getMessage());
        } catch (BookNotAvailableException e) {
            System.out.println("Not Available: " + e.getMessage());
            int wantToReserve = readInt("Want to reserve book? press 1 for yes and 0 for no: ");
            if (wantToReserve == 1) reserveBook();
        } catch (BookAlreadyBorrowedException e) {
            System.out.println("Already Borrowed: " + e.getMessage());
        } catch (MemberLimitExceededException e) {
            System.out.println("Limit Exceeded: " + e.getMessage());
        } catch (UnsupportedOperationException | IllegalArgumentException | ChangesNotSavedException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void returnBook() {
        String isbn = readString("Enter ISBN: ");
        String memberID = readString("Enter Member ID: ");

        try {
            Transaction t = transactionServices.returnBook(isbn, memberID);
            System.out.println("Book Returned");
            if (t.getFineAmount() > 0)
                System.out.println("Fine: Rs." + t.getFineAmount() + " (" + t.getDaysOverdue() + " days overdue)");
            ((PhysicalBook) bookServices.getBook(isbn)).notifyNextInQueue();
        } catch (BookNotFoundException | MemberNotFoundException | NoActiveBorrowRecordFoundException e) {
            System.out.println("Not Found: " + e.getMessage());
        } catch (ChangesNotSavedException e) {
           System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private void viewAllTransactions() {
        List<Transaction> list = null;
        try {
            list = transactionServices.getAllTransactions();
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
        if (list == null || list.isEmpty()) {
            System.out.println("No Transactions yet");
            return;
        }

        System.out.println("\n ~~All Transaction~~");
        for (Transaction t : list) System.out.println(t);
    }
    private void viewOverdueTransactions() {
        List<Transaction> list = null;
        try {
        list = transactionServices.getOverdueTransactions();
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

        if (list == null || list.isEmpty()) {
            System.out.println("No Overdue Transactions");
            return;
        }

        System.out.println("\n ~~Overdue Transaction~~");
        for (Transaction t : list) System.out.println(t);
    }

    
    // worker menu and function
    private void workerMenu() {

        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n =======WORKER MENU=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> addWorker();
                case 2 -> viewAllWorkers();
                case 3 -> removeWorker();
                case 4 -> updateWorker();
                case 9 -> printWorkerMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }
    private void addWorker() {
        String name = readString("Name: ");
        String phone = readString("Phone #: ");
        String email = readString("E-Mail: ");
        String address = readString("Address: ");
        int age = readInt("Year of Birth: ");
        String password = readString("Password: ");

        try {
            Admin worker = new Admin(name, phone, email, address, age, password);
            workerServices.admitWorker(worker);
            System.out.println("Worker Added");
            System.out.println("Worker ID: " + worker.getWorkerID());
        } catch (DuplicatePupilException | IllegalArgumentException | InvalidPasswordException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void addWorkerOnStartup() {
        String id = "00ADMIN00";
        String name = readString("Name: ");
        String phone = readString("Phone #: ");
        String email = readString("E-Mail: ");
        String address = readString("Address: ");
        int age = readInt("Year of Birth: ");
        String password = readString("Password: ");

        try {
            workerServices.admitWorker(new Admin(id, name, phone, email, address, age, password));
            System.out.println("Worker Added");
            System.out.println("Worker ID : " + id);

        } catch (DuplicatePupilException | IllegalArgumentException | InvalidPasswordException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void updateWorker() {
        String workerID = readString("Worker ID to Update: ");
        Admin worker = workerServices.getWorker(workerID);
        String oldPassword = readString("Enter Old Password: ");

        try {
            if (!PasswordUtils.verifyPassword(oldPassword, worker.getSalt(), worker.getHash())) {
                System.out.println("Wrong Password");
                return;
            }
    
            String name = readString("New Name (Leave Empty if Unchanged): ");
            String phone = readString("New Phone # (Leave Empty if Unchanged): ");
            String email = readString("New E-Mail (Leave Empty if Unchanged): ");
            String address = readString("New Address (Leave Empty if Unchanged): ");
            String password = readString("New Password (Leave Empty if Unchanged): ");

            workerServices.updateWorker(workerID, name, phone, email, address, password);
        } catch (WorkerNotFoundException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void removeWorker() {
        String WorkerID = readString("Worker ID to remove: ");
        try {
            Admin target = workerServices.getWorker(WorkerID);

            String password = readString("Enter Password: ");

            if (PasswordUtils.verifyPassword(password, target.getSalt(), target.getHash())) {
                workerServices.removeWorker(WorkerID);
                System.out.println("Worker Removed");
            } else {
                System.out.println("Wrong Password");
            }
        } catch (WorkerNotFoundException | ChangesNotSavedException e) {
            System.out.println("Not Found: " + e.getMessage());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void viewAllWorkers() {
        List<Admin> workers = null;
        try {
            workers = workerServices.getAllWorkers();
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

        if (workers == null || workers.isEmpty()) {
            System.out.println("No Workers found");
            return;
        }

        System.out.println("\n ~~All Workers~~");
        for (Admin worker : workers) System.out.println(worker);
    }


    // search menu and function
    private void searchMenu() {
        
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n =======SEARCH MENU=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> searchBooks();
                case 2 -> searchMembers();
                case 3 -> searchWorkers();
                case 9 -> printSearchMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }            
        }
    }
    private void searchBooks() {
        String query = readString("Search Books (title/author/ISBN/genre): ");
        List<AbstractBook> results = null;

        try {
            results = bookServices.searchBooks(query);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
        if (results == null || results.isEmpty()) {
            System.out.println("No Books found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (AbstractBook book : results) { System.out.println(book);
        }
    }
    private void searchMembers() {
        String query = readString("Search Members (name/id/email): ");
        List<Member> results = null;

        try {
            results = memberServices.searchMembers(query);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }

        if (results == null || results.isEmpty()) {
            System.out.println("No Members found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (Member member : results) { System.out.println(member);
        }
    }
    private void searchWorkers() {
        String query = readString("Search Workers (name/id/email): ");
        List<Admin> results = null;

        try {
            results = workerServices.searchWorker(query);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
        if (results == null || results.isEmpty()) {
            System.out.println("No Workers found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (Admin worker : results) { System.out.println(worker);
        }
    }
    private void reserveMenu() {
        
        boolean inMenu = true;
        
        while (inMenu) {
            System.out.println("\n =======RESERVE MENU=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> cancelReservation();
                case 2 -> peakReservation();
                case 3 -> getQueuePosition();
                case 4 -> printQueue();
                case 9 -> printReserveMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }            
        }
    }
    private void reserveBook() {
        String isbn = readString("Book ISBN to reserve: ");
        String memberID  = readString("Member ID of Customer: ");

        try {
            PhysicalBook book = (PhysicalBook) bookServices.getBook(isbn);
            Member member = memberServices.getMember(memberID);

            boolean reserved = book.reserve(member);
            if (!reserved) System.out.println("Book " + isbn + " may be reserved by the member " + memberID);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void cancelReservation() {
        String isbn = readString("Book ISBN to cancel Reservation: ");
        String memberID  = readString("Member ID of Customer: ");

        try {
            PhysicalBook book = (PhysicalBook) bookServices.getBook(isbn);
            Member member = memberServices.getMember(memberID);

            boolean cancelled = book.cancelReservation(member);
            if (!cancelled) System.out.println("Book " + isbn + " may not be reserved by the member " + memberID);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void peakReservation() {
        String isbn = readString("ISBN of book");

        try {
            PhysicalBook book = (PhysicalBook) bookServices.getBook(isbn);
            System.out.println(book.peakReservationQueue());
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }
    private void getQueuePosition() {
        String isbn = readString("ISBN of book: ");
        String memberID = readString("Target member ID: ");

        try {
            PhysicalBook book = (PhysicalBook) bookServices.getBook(isbn);
            Member member = memberServices.getMember(memberID);

            int pos = book.getQueuePosition(member);
            System.out.println("Position of Member " + memberID + " for Book " + isbn + " is: " + pos);
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }
    private void printQueue() {
        String isbn = readString("ISBN of book: ");
        try {
            PhysicalBook book = (PhysicalBook) bookServices.getBook(isbn);

            System.out.println(book.getQueue());
            
        } catch (DatabaseException e) {
            System.out.println(e.getUserMessage());
            logger.error("DB error [{}]: {}", e.getErrorCode(), e.getCause());
        }
    }

    // print menu methods
    private static void printMainMenu() {
        System.out.println("\n =======Main Menu=======");
        System.out.println("1. Books \n2. Members \n3. Borrow/Return");
        System.out.println("4. Search \n5. Workers\n9. Print Menu \n0. Log out");
        System.out.println(" =======================");
    }
    private static void printBookMenu() {
        System.out.println("\n =======Book Menu=======");
        System.out.println("1. Add Physical Book \n2. Add E-book \n3. Add Audio Book");
        System.out.println("4. View All Books \n5. Remove Book \n9. Print Book Menu \n0. Back");
        System.out.println(" =======================");
    }
    private static void printMemberMenu() {
        System.out.println("\n =======Member Menu=======");
        System.out.println("1. Register Member \n2. View All Members \n3. Remove Member");
        System.out.println("9. Print Member Menu \n0. Back");
        System.out.println(" =========================");
    }
    private static void printWorkerMenu() {
        System.out.println("\n =======Worker Menu=======");
        System.out.println("1. Register Worker \n2. View All Workers \n3. Remove Worker");
        System.out.println("9. Print Worker Menu \n0. Back");
        System.out.println(" =========================");
    }
    private static void printTransactionMenu() {
        System.out.println("\n ====Borrow / Return MENU====");
        System.out.println("1. Borrow Book \n2. Return Book \n3. View All Transactions");
        System.out.println("4. View Overdue Transactions \n9. Print Transaction Menu \n0. Back");
        System.out.println(" =============================");
    }
    private static void printSearchMenu() {
        System.out.println("\n =======Search Menu=======");
        System.out.println("1. Search Books \n2. Search Members \n9. Print Search Menu \n0. Back");
        System.out.println(" =========================");

    }
    private static void printReserveMenu() {
        System.out.println("\n =======Reserve Menu=======");
        System.out.println("1. Cancel Reservation \n2. Peak Reservation \n3. Get Queue Position");
        System.out.println("4. Print Queue \n9. Print Reserve Menu \n0. Back");
        System.out.println(" =========================");

    }

    // Helper Method
    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}