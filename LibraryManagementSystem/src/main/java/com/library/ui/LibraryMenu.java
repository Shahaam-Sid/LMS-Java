package com.library.ui;

import com.library.exceptions.BookNotAvailableException;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DuplicateISBNException;
import com.library.exceptions.DuplicatePupilException;
import com.library.exceptions.InvalidPasswordException;
import com.library.exceptions.MemberLimitExceededException;
import com.library.exceptions.MemberNotFoundException;
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
import java.util.List;
import java.util.Scanner;

public class LibraryMenu {
    private final BookServices bookServices;
    private final MemberServices memberServices;
    private final WorkerServices workerServices;
    private final TransactionServices transactionServices;
    private final Scanner scanner;

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
                case 1:
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
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 0:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid Choice, Try Again");
                    break;
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
                System.out.println("\n =======Main Menu=======\n");

                int choice = readInt("Enter Choice > ");

                switch (choice) {
                    case 1:
                        printBookMenu();
                        bookMenu();
                        break;
                    case 2:
                        printMemberMenu();
                        memberMenu();
                        break;
                    case 3:
                        printTransactionMenu();
                        transactionMenu();
                        break;
                    case 4:
                        printSearchMenu();
                        searchMenu();
                        break;
                    case 5:
                        printWorkerMenu();
                        workerMenu();
                        break;
                    case 9:
                        printMainMenu();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid Choice, Try Again");
                        break;
                }
            } catch (Exception e) {System.out.println("Error: " + e.getMessage());}
        }
    }
    private void checkFirstStart() {
        if (workerServices.isEmpty()) {
            System.out.println("Add Admin\n");
            addWorker();
        }
    }


    // Books Menu and Functions
    private void bookMenu() {

        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n =======Book Menu=======\n");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> addPhysicalBook();
                case 2 -> addEBook();
                case 3 -> addAudioBook();
                case 4 -> veiwAllBooks();
                case 5 -> removeBook();
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
        } catch (DuplicateISBNException | IllegalArgumentException e) {System.out.println("Error: " + e.getMessage());}
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
        } catch (DuplicateISBNException | IllegalArgumentException e) {System.out.println("Error: " + e.getMessage());}
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
        } catch (DuplicateISBNException | IllegalArgumentException e) {System.out.println("Error: " + e.getMessage());}
    }
    private void veiwAllBooks() {
        List<AbstractBook> books = bookServices.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No Books Found");
            return;
        }

        System.out.println("\n ~~All Books~~");
        for (AbstractBook book : books) System.out.println(book);
    }
    private void removeBook() {
        String isbn = readString("Enter ISBN to remove: ");
        try {
            bookServices.removeBook(isbn);
            System.out.println("Book Removed");
        } catch (BookNotFoundException e) {System.out.println("Error: " + e.getMessage());}
    }


    // Member menu and functions
    private void memberMenu() {

        boolean inMenu = true;

        while (inMenu) {
            System.out.println("\n =======Member Menu=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> registerMember();
                case 2 -> viewAllMembers();
                case 3 -> removeMember();
                case 9 -> printMemberMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }
    private void registerMember() {
        System.out.println("\n ~~Register Member~~");
        String id = readString("Member ID: ");
        String name = readString("Name: ");
        String phone = readString("Phone #: ");
        String email = readString("E-Mail: ");
        String address = readString("Address: ");
        int age = readInt("Age: ");

        try {
            memberServices.registerMember(new Member(id, name, phone, email, address, age));
        } catch (IllegalArgumentException | DuplicatePupilException e) {System.out.println("Error: " + e.getMessage());}

    }
    private void viewAllMembers() {
        List<Member> members = memberServices.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No Members found");
            return;
        }

        System.out.println("\n ~~All Members~~");
        for (Member member : members) System.out.println(member);
    }
    private void removeMember() {
        String id = readString("Member ID to remove: ");

        try {
            memberServices.removeMember(id);
        } catch (MemberNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
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
        } catch (MemberLimitExceededException e) {
            System.out.println("Limit Exceeded: " + e.getMessage());
        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
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
        } catch (BookNotFoundException | MemberNotFoundException e) {
            System.out.println("Not Found: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void viewAllTransactions() {
        List<Transaction> list = transactionServices.getAllTransactions();

        if (list.isEmpty()) {
            System.out.println("No Transactions yet");
            return;
        }

        System.out.println("\n ~~All Transaction~~");
        for (Transaction t : list) System.out.println(t);
    }
    private void viewOverdueTransactions() {
        List<Transaction> list = transactionServices.getOverdueTransactions();

        if (list.isEmpty()) {
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
            System.out.println("\n =======Worker Menu=======");

            int choice = readInt("Enter Choice > ");

            switch (choice) {
                case 1 -> addWorker();
                case 2 -> viewAllWorkers();
                case 3 -> removeWorker();
                case 9 -> printWorkerMenu();
                case 0 -> inMenu = false;
                default -> System.out.println("Invalid Choice");
            }
        }
    }
    private void addWorker() {
        String workerID = readString("Worker ID: ");
        String name = readString("Name: ");
        String phone = readString("Phone #: ");
        String email = readString("E-Mail: ");
        String address = readString("Address: ");
        int age = readInt("Age: ");
        String password = readString("Password: ");

        try {
            workerServices.admitWorker(new Admin(workerID, name, phone, email, address, age, password));
            System.out.println("Worker Added");
        } catch (DuplicatePupilException | IllegalArgumentException | InvalidPasswordException e) {
            System.out.println("Error: " + e.getMessage());
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
        } catch (WorkerNotFoundException e) {
            System.out.println("Not Found: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void viewAllWorkers() {
        List<Admin> workers = workerServices.getAllWorkers();

        if (workers.isEmpty()) {
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
        List<AbstractBook> results = bookServices.searchBooks(query);

        if (results.isEmpty()) {
            System.out.println("No Books found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (AbstractBook book : results) { System.out.println(book);
        }
    }
    private void searchMembers() {
        String query = readString("Search Members (name/id/email): ");
        List<Member> results = memberServices.searchMembers(query);

        if (results.isEmpty()) {
            System.out.println("No Members found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (Member member : results) { System.out.println(member);
        }
    }
    private void searchWorkers() {
        String query = readString("Search Workers (name/id/email): ");
        List<Admin> results = workerServices.searchWorker(query);

        if (results.isEmpty()) {
            System.out.println("No Workers found for: " + query);
            return;
        }

        System.out.println("\n ~~Search Results~~");
        for (Admin worker : results) { System.out.println(worker);
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
        System.out.println("\n ====BORROW / RETURN MENU====");
        System.out.println("1. Borrow Book \n2. Return Book \n3. View All Transactions");
        System.out.println("4. View Overdue Transactions \n9. Print Transaction Menu \n0. Back");
        System.out.println(" =============================");
    }
    private static void printSearchMenu() {
        System.out.println("\n =======SEARCH MENU=======");
        System.out.println("1. Search Books \n2. Search Members \n9. Print Search Menu \n0. Back");
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