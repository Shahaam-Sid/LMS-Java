package com.library.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.library.db.DBConnection;
import com.library.db.DBUtility;
import com.library.enums.BookStatus;
import com.library.enums.BookType;
import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DuplicateISBNException;
import com.library.models.book.AbstractBook;
import com.library.models.book.AudioBook;
import com.library.models.book.EBook;
import com.library.models.book.PhysicalBook;

/**
 * Class for BookServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class BookServices {

    private static final String TABLE = "books";
    private static final String UIDCOL = "isbn";

    /**
     * Checks if database is empty
     * @return true if empty, else not
     */
    public boolean isEmpty() {return DBUtility.isEmpty(TABLE);}
    /**
     * adds new book to Database
     * @param book to add
     * @throws DuplicateISBNException if book already exists
     * @throws SQLException Error from Database
     */
    public void addNewBook(AbstractBook book) throws DuplicateISBNException {
    
        try (Connection conn = DBConnection.getConnection()) {
            if (DBUtility.doesRowExists(TABLE, UIDCOL, book.getISBN(), conn))
                throw new DuplicateISBNException(book.getISBN());

            String ISBN = book.getISBN();
            String title = book.getTitle();
            String author = book.getAuthor();
            String genre = book.getGenre();
            int publishedYear = book.getPublishedYear();
            String status = book.getStatus();
            String type = book.getType();

            switch (book.getType()) {
                case "PHYSICAL" -> {
                    PhysicalBook physBook = (PhysicalBook) book;
                    String q = """
                    INSERT INTO books (isbn, title, author, genre, published_year, 
                        book_status, book_type, shelf_location, total_copies, available_copies)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
                    try (PreparedStatement ps2 = conn.prepareStatement(q)) {
                        ps2.setString(1, ISBN);
                        ps2.setString(2, title);
                        ps2.setString(3, author);
                        ps2.setString(4, genre);
                        ps2.setInt(5, publishedYear);
                        ps2.setString(6, status);
                        ps2.setString(7, type);
                        ps2.setString(8, physBook.getShelfLocation());
                        ps2.setInt(9, physBook.getTotalCopies());
                        ps2.setInt(10, physBook.getAvailableCopies());
                        ps2.executeUpdate();
                    }
                } case "EBOOK" -> {
                    EBook ebook = (EBook) book;
                    String q2 = """
                    INSERT INTO books (isbn, title, author, genre, published_year, 
                        book_status, book_type, download_url, file_format, file_size_mb)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
                    try (PreparedStatement ps3 = conn.prepareStatement(q2)) {
                        ps3.setString(1, ISBN);
                        ps3.setString(2, title);
                        ps3.setString(3, author);
                        ps3.setString(4, genre);
                        ps3.setInt(5, publishedYear);
                        ps3.setString(6, status);
                        ps3.setString(7, type);
                        ps3.setString(8, ebook.getDownloadURL());
                        ps3.setString(9, ebook.getFormat());
                        ps3.setDouble(10, ebook.getFileSizeMB());
                        ps3.executeUpdate();
                    }
                } case "AUDIOBOOK" -> {
                    AudioBook adBook = (AudioBook) book;
                    String q3 = """
                    INSERT INTO books (isbn, title, author, genre, published_year, 
                        book_status, book_type, download_url, file_format, file_size_mb, narrator)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
                    try (PreparedStatement ps4 = conn.prepareStatement(q3)) {
                        ps4.setString(1, ISBN);
                        ps4.setString(2, title);
                        ps4.setString(3, author);
                        ps4.setString(4, genre);
                        ps4.setInt(5, publishedYear);
                        ps4.setString(6, status);
                        ps4.setString(7, type);
                        ps4.setString(8, adBook.getDownloadURL());
                        ps4.setString(9, adBook.getFormat());
                        ps4.setDouble(10, adBook.getFileSizeMB());
                        ps4.setString(11, adBook.getNarrator());
                        ps4.executeUpdate();
                    }
                }
                default -> System.out.println("Invalid Book Type");
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    /**
     * gets book object from List
     * @param isbn of book
     * @return book
     * @throws BookNotFoundException if book not in list
     * @throws SQLException Error from Database
     */
    public AbstractBook getBook(String isbn) throws BookNotFoundException {
        
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM books WHERE isbn = ?")) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapBookFromDB(rs);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        throw new BookNotFoundException(isbn);
    }
    public void updateBook(String targetISBN, String status, 
        String shelfLocation, String totalCopies, String availableCopies) 
        throws BookNotFoundException, IllegalArgumentException {

        AbstractBook book = getBook(targetISBN);

        try (Connection conn = DBConnection.getConnection()) {
            int rowsAffected = 0;
            int countChangesMade = 0;
            try {
                conn.setAutoCommit(false);
                if ((status != null && !status.isEmpty()) && !status.equals(book.getStatus())) {
                    countChangesMade++;
                    book.setStatus(BookStatus.valueOf(status));

                    try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET book_status = ? WHERE isbn = ?")) {
                        ps.setString(1, book.getStatus());
                        ps.setString(2, book.getISBN());
                        
                        rowsAffected += ps.executeUpdate();
                    }
                }
                PhysicalBook pBook = (PhysicalBook) book;
                if ((shelfLocation != null && !shelfLocation.isEmpty()) && !shelfLocation.equals(pBook.getShelfLocation())) {
                    countChangesMade++;
                    pBook.setShelfLocation(shelfLocation);

                    try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET shelf_location = ? WHERE isbn = ?")) {
                        ps.setString(1, pBook.getShelfLocation());
                        ps.setString(1, pBook.getISBN());

                        rowsAffected += ps.executeUpdate();
                    }
                }
                if ((totalCopies != null && !totalCopies.isEmpty()) && (pBook.getTotalCopies() != Integer.parseInt(totalCopies))) {
                    countChangesMade++;
                    pBook.setTotalCopies(Integer.parseInt(totalCopies));

                    try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET total_copies = ? WHERE isbn = ?")) {
                        ps.setInt(1, pBook.getTotalCopies());
                        ps.setString(2, pBook.getISBN());

                        rowsAffected += ps.executeUpdate();
                    }
                }
                if (availableCopies != null && !availableCopies.isEmpty() && (pBook.getAvailableCopies() != Integer.parseInt(availableCopies))) {
                    countChangesMade++;
                    pBook.setAvailableCopies(Integer.parseInt(availableCopies));

                    try (PreparedStatement ps = conn.prepareStatement("UPDATE books SET available_copies = ? WHERE isbn = ?")) {
                        ps.setInt(1, pBook.getAvailableCopies());
                        ps.setString(2, pBook.getISBN());

                        rowsAffected += ps.executeUpdate();
                    }
                }

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                if (rowsAffected == countChangesMade) conn.commit();
                else conn.rollback();
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    } // => Handle the user input on this method AND if totalCopies updates then availableCopies must also be updated
    /**
     * removes book from List
     * @param isbn of book
     * @throws BookNotFoundException if book not in list
     * @throws RuntimeException if query not executes correctly
     * @throws SQLException Error from Database
     */
    public void removeBook(String isbn) throws BookNotFoundException, RuntimeException {
        try (Connection conn = DBConnection.getConnection()) {
            if (!DBUtility.doesRowExists(TABLE, UIDCOL, isbn, conn))
                throw new BookNotFoundException(isbn);
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE isbn = ?")) {
                ps.setString(1, isbn);
                int output = ps.executeUpdate();
                if (output == 0) throw new RuntimeException("Unexpected Error Occurred");   
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
    }
    /**
     * searches for book
     * @param query string for book to search
     * @return list of results matched
     * @throws SQLException Error from Database
     */
    public List<AbstractBook> searchBooks(String query) {
        List<AbstractBook> results = new ArrayList<>();
        String sql = """
                SELECT * FROM books
                WHERE LOWER(isbn) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(title) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(author) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(genre) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(shelf_location) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(file_format) LIKE LOWER(CONCAT('%', ?, '%')) OR
                LOWER(narrator) LIKE LOWER(CONCAT('%', ?, '%'))
                """;
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ps.setString(4, query);
            ps.setString(5, query);
            ps.setString(6, query);
            ps.setString(7, query);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) results.add(mapBookFromDB(rs));
            }
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return results;
    }
    /**
     * returns list of all books
     * @return list of books
     */
    public List<AbstractBook> getAllBooks() {
        List<AbstractBook> results = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM books");
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) results.add(mapBookFromDB(rs));
        } catch (SQLException e) {
            DBUtility.SQLExceptionLoop(e);
        }
        return results;
    }


    public static AbstractBook mapBookFromDB(ResultSet rs) 
                            throws IllegalArgumentException, SQLException {

        if (rs == null) throw new IllegalArgumentException("Invalid Response from Database");

        String isbn = rs.getString("isbn");
        String title = rs.getString("title");
        String author = rs.getString("author");
        String genre = rs.getString("genre");
        int publishedYear = rs.getInt("published_year");
        BookStatus status = BookStatus.valueOf(rs.getString("book_status"));
        BookType type = BookType.valueOf(rs.getString("book_type"));

        switch (type) {
            case PHYSICAL -> {
                String shelfLocation = rs.getString("shelf_location");
                int totalCopies = rs.getInt("total_copies");
                int availableCopies = rs.getInt("available_copies");

                PhysicalBook book = new PhysicalBook(isbn, title, author, genre, publishedYear,
                     shelfLocation, totalCopies);
                book.setStatus(status);
                book.setAvailableCopies(availableCopies);
                return book;
            } case EBOOK -> {
                String downloadURL = rs.getString("download_url");
                String format = rs.getString("file_format");
                double fileSizeMB = rs.getDouble("file_size_mb");

                EBook book = new EBook(isbn, title, author, genre, publishedYear,
                     downloadURL, format, fileSizeMB);
                book.setStatus(status);
                return book;
            } case AUDIOBOOK -> {
                String downloadURL = rs.getString("download_url");
                String format = rs.getString("file_format");
                double fileSizeMB = rs.getDouble("file_size_mb");
                String narrator = rs.getString("narrator");
                
                AudioBook book = new AudioBook(isbn, title, author, genre, publishedYear,
                     downloadURL, format, fileSizeMB, narrator);
                book.setStatus(status);
                return book;
            } default -> throw new ClassCastException("Invalid data cannot map to book");
        }
    }
} // => Prevent Book rom deletion when is borrowed