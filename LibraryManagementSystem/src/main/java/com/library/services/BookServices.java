package com.library.services;

import com.library.exceptions.BookNotFoundException;
import com.library.exceptions.DuplicateISBNException;
import com.library.interfaces.Searchable;
import com.library.models.book.AbstractBook;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for BookServices
 * 
 * @author Muhammad Shahaam Siddiqui
 */
public class BookServices {

    private Map<String, AbstractBook> books = new HashMap<>();

    /**
     * adds new book to List
     * @param book to add
     * @throws DuplicateISBNException if book already exists
     */
    public void addNewBook(AbstractBook book) throws DuplicateISBNException {
        if (books.containsKey(book.getISBN()))
            throw new DuplicateISBNException(book.getISBN());
        
        books.put(book.getISBN(), book);
    }
    /**
     * gets book object from List
     * @param isbn of book
     * @return book
     * @throws BookNotFoundException if book not in list
     */
    public AbstractBook getBook(String isbn) throws BookNotFoundException {
        AbstractBook book = books.get(isbn);
        if (book == null) throw new BookNotFoundException(isbn);
        
        return book;
    }
    /**
     * removes book from List
     * @param isbn of book
     * @throws BookNotFoundException if book not in list
     */
    public void removeBook(String isbn) throws BookNotFoundException {
        if (!books.containsKey(isbn)) throw new BookNotFoundException(isbn);

        books.remove(isbn);
    }
    /**
     * searches for book
     * @param query string for book to search
     * @return list of results matched
     */
    public List<AbstractBook> searchBooks(String query) {
        List<AbstractBook> results = new ArrayList<>();
        for (AbstractBook book : books.values()) {
            if (book instanceof Searchable) {
                if (((Searchable) book).matchesQuery(query))
                    results.add(book);
            }
        }
        
        return results;
    }
    /**
     * returns list of all books
     * @return list of books
     */
    public List<AbstractBook> getAllBooks() {
        return new ArrayList<>(books.values());
    }
}