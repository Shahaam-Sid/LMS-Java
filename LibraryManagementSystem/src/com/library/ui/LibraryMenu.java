package com.library.ui;

import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DuplicateISBNException;
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
    private BookServices bookServices;
    private MemberServices memberServices;
    private WorkerServices workerServices;
    private TransactionServices transactionServices;
    private Scanner scanner;

    public LibraryMenu(BookServices bookServices, MemberServices memberServices,
        WorkerServices workerServices, TransactionServices transactionServices) {

        this.bookServices = bookServices;
        this.memberServices = memberServices;
        this.workerServices = workerServices;
        this.transactionServices = transactionServices;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("==========================================");
        System.out.println("   Welcome to Library Management System   ");
        System.out.println("==========================================");

        boolean running = true;
        
        while (running) {
            try {
                int choice = readInt("Enter Choice > ");

                switch (choice) {
                    case 1:
                        printBookMenu();
                        bookMenu();
                        break;
                    case 2:
                        printMemberMenu();
                        break;
                    case 3:
                        printTransactionMenu();
                        break;
                    case 4:
                        printSearchMenu();
                        break;
                    case 5:
                        printWorkerMenu();
                        break;
                    case 6:
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

        System.out.println("PROGRAM ENDED");
        scanner.close();
    }


    // Books Menu and Functions
    private void bookMenu() {

        int choice = readInt("Enter Choice > ");

        switch (choice) {
            case 1 -> addPhysicalBook();
            case 2 -> addEBook();
            case 3 -> addAudioBook();
            case 4 -> veiwAllBooks();
            case 5 -> removeBook();
            case 6 -> printBookMenu();
            case 0 -> {}
            default -> System.out.println("Invalid Choice");
        }
    }   

    private void addPhysicalBook() {
        System.out.println("\n ~~Add Physical Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre  = readString("Genre: ");
        int year = readInt("Published Year: ");
        String shelf = readString("Shelf Location: ");
        int copies = readInt("Total Copies: ");

        try {
            bookServices.addNewBook(new PhysicalBook(isbn, title, author, genre, year, shelf, copies));
            System.out.println("Physical Book Added");
        } catch (DuplicateISBNException e) {System.out.println("Error: " + e.getMessage());}
    }
    private void addEBook() {
        System.out.println("\n ~~Add E-Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre  = readString("Genre: ");
        int year = readInt("Published Year: ");
        String url = readString("Download URL: ");
        String format = readString("Format: ");

        try {
            bookServices.addNewBook(new EBook(isbn, title, author, genre, year, url, format));
            System.out.println("E-Book Added");
        } catch (DuplicateISBNException e) {System.out.println("Error: " + e.getMessage());}
    }
    private void addAudioBook() {
        System.out.println("\n ~~Add Audio Book~~");
        String isbn = readString("ISBN: ");
        String title = readString("Title: ");
        String author = readString("Author: ");
        String genre  = readString("Genre: ");
        int year = readInt("Published Year: ");
        String url = readString("Download URL: ");
        String format = readString("Format: ");
        String narrator = readString("Narrator: ");

        try {
            bookServices.addNewBook(new AudioBook(isbn, title, author, genre, year, url, format, narrator));
            System.out.println("Audio Book Added");
        } catch (DuplicateISBNException e) {System.out.println("Error: " + e.getMessage());}
    }
    private void veiwAllBooks() {
        List<AbstractBook> books = bookServices.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No Books Found");
            return;
        }

        System.out.println("\n ~~All Books~~");
        for (AbstractBook book : books) {
            System.out.println(books);
        }
    }
    private void removeBook() {
        String isbn = readString("Enter ISBN to remove: ");
        try {
            bookServices.removeBook(isbn);
            System.out.println("Book Removed");
        } catch (BookNotFoundException e) {System.out.println("Error: " + e.getMessage());}
    }


    // print menu methods
    private static void printMainMenu() {
        System.out.println("\n =======Main Menu=======");
        System.out.println("1. Books \n2. Members \n3. Borrow/Return");
        System.out.println("4. Search \n5. Workers\n6. Print Menu \n0. Exit");
        System.out.println(" =======================");
    }
    private static void printBookMenu() {
        System.out.println("\n =======Book Menu=======");
        System.out.println("1. Add Physical Book \n2. Add E-book \n3. Add Audio Book");
        System.out.println("4. View All Books \n5. Remove Book \n6. Print Book Menu \n0. Back");
        System.out.println(" =======================");
    }
    private static void printMemberMenu() {
        System.out.println("\n =======Member Menu=======");
        System.out.println("1. Register Member \n2. View All Members \n3. Remove Member");
        System.out.println("4. Print Member Menu \n0. Back");
        System.out.println(" =========================");
    }
    private static void printWorkerMenu() {
        System.out.println("\n =======Worker Menu=======");
        System.out.println("1. Register Worker \n2. View All Workers \n3. Remove Worker");
        System.out.println("4. Print Worker Menu \n0. Back");
        System.out.println(" =========================");
    }
    private static void printTransactionMenu() {
        System.out.println("\n ====BORROW / RETURN MENU====");
        System.out.println("1. Borrow Book \n2. Return Book \n3. View All Transactions");
        System.out.println("4. View Overdue Transactions \n5. Print Transaction Menu \n0. Back");
        System.out.println(" =============================");
    }
    private static void printSearchMenu() {
        System.out.println("\n =======SEARCH MENU=======");
        System.out.println("1. Search Books \n2. Search Members \n3. Print Search Menu \n0. Back");
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
    public static void main(String[] args) {
        BookServices bs = new BookServices();
        MemberServices ms = new MemberServices();
        WorkerServices ws = new WorkerServices();
        TransactionServices ts = new TransactionServices(bs, ms);
        LibraryMenu lm = new LibraryMenu(bs, ms, ws, ts);
        LibraryMenu.printSearchMenu();
    }
}   