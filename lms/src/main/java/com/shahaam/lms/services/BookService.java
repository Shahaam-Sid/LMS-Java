package com.shahaam.lms.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shahaam.lms.dto.book.AudioBookRequestDTO;
import com.shahaam.lms.dto.book.AudioBookResponseDTO;
import com.shahaam.lms.dto.book.BookRequestDTO;
import com.shahaam.lms.dto.book.BookResponseDTO;
import com.shahaam.lms.dto.book.BookUpdateRequestDTO;
import com.shahaam.lms.dto.book.EBookRequestDTO;
import com.shahaam.lms.dto.book.EBookResponseDTO;
import com.shahaam.lms.dto.book.PhysicalBookRequestDTO;
import com.shahaam.lms.dto.book.PhysicalBookResponseDTO;
import com.shahaam.lms.enums.BookStatus;
import com.shahaam.lms.enums.BookType;
import com.shahaam.lms.exceptions.BookNotFoundException;
import com.shahaam.lms.exceptions.InvalidBookTypeException;
import com.shahaam.lms.models.book.AbstractBook;
import com.shahaam.lms.models.book.AbstractDigitalBook;
import com.shahaam.lms.models.book.AudioBook;
import com.shahaam.lms.models.book.EBook;
import com.shahaam.lms.models.book.PhysicalBook;
import com.shahaam.lms.repositories.BookRepository;


@Service
public class BookService {
    private final BookRepository bookRepo;
    
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Transactional(readOnly = true)
    public long getBooksCount() {
        return bookRepo.count();
    }
    @Transactional
    public BookResponseDTO addBook(BookRequestDTO req) {
        AbstractBook book = mapFromRequestDTO(req);
        return mapToResponseDTO(bookRepo.save(book));
    }
    @Transactional(readOnly = true)
    public boolean doesBookExist(String isbn) {
        return bookRepo.existsById(isbn);
    }
    @Transactional(readOnly = true)
    public BookResponseDTO getBook(String isbn) {
        return mapToResponseDTO(getBookAsObj(isbn));
    }
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        return bookRepo.findAll().stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional
    public void removeBook(String isbn) {
        if (!doesBookExist(isbn)) throw new BookNotFoundException(isbn);
        bookRepo.deleteById(isbn);
    }
    @Transactional
    public BookResponseDTO updateBook(BookUpdateRequestDTO req, String isbn) {
        AbstractBook book = getBookAsObj(isbn);

        if (req.status() != null) {book.setStatus(req.status());}

        if (req.shelfLocation() == null && req.shelfLocation() == null &&
        req.availableCopies() == null && req.totalCopies() == null) return mapToResponseDTO(book);

        if (book instanceof PhysicalBook physicalBook) {
            if (req.shelfLocation() != null) {physicalBook.setShelfLocation(req.shelfLocation());}
            if (req.totalCopies() != null) {physicalBook.setTotalCopies(req.totalCopies());}
            if (req.availableCopies() != null) {physicalBook.setAvailableCopies(req.availableCopies());}
        } else throw new InvalidBookTypeException(isbn);
        
        return mapToResponseDTO(physicalBook);
    }
    @Transactional(readOnly = true)
    public List<BookResponseDTO> searchKeyword(String keyword) {
        return bookRepo.searchMatching(keyword).stream().map(this::mapToResponseDTO).toList();
    }
    @Transactional(readOnly = true)
    public boolean isBookAvailable(String isbn) {
        AbstractBook book = getBookAsObj(isbn);
        if (book instanceof PhysicalBook pBook) {return pBook.isAvailable();}
        else if (book instanceof AbstractDigitalBook adBook) {return adBook.isAvailable();}
        else throw new InvalidBookTypeException(isbn);
    }
    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAvailableBooks() {
        return bookRepo.findAvailableBooks().stream().map(this::mapToResponseDTO).toList();
    }



    // helper methods

    @Transactional(readOnly = true)
    private AbstractBook getBookAsObj(String isbn) {
        return bookRepo.findById(isbn)
        .orElseThrow(() -> new BookNotFoundException(isbn));
    }

    private BookResponseDTO mapToResponseDTO(AbstractBook book) {

        return switch(book) {
            case PhysicalBook pb -> new PhysicalBookResponseDTO(
                pb.getISBN(), pb.getTitle(), pb.getAuthor(), pb.getGenre(),
                pb.getPublishedYear(), BookStatus.valueOf(pb.getStatus()), pb.getShelfLocation(),
                BookType.valueOf("PHYSICAL")
            );
            case EBook eb -> new EBookResponseDTO(
                eb.getISBN(), eb.getTitle(), eb.getAuthor(), eb.getGenre(),
                eb.getPublishedYear(), BookStatus.valueOf(eb.getStatus()),
                BookType.valueOf("EBOOK")
            );
            case AudioBook ab -> new AudioBookResponseDTO(
                ab.getISBN(), ab.getTitle(), ab.getAuthor(), ab.getGenre(),
                 ab.getPublishedYear(), BookStatus.valueOf(ab.getStatus()), ab.getNarrator(),
                BookType.valueOf("AUDIOBOOK")
                );
            default -> null; 
        };
    }
    private AbstractBook mapFromRequestDTO(BookRequestDTO req) {

        return switch(req) {
            case PhysicalBookRequestDTO pReq -> new PhysicalBook(
                pReq.ISBN(), pReq.title(), pReq.author(), pReq.genre(),
                pReq.publishedYear(), pReq.shelfLocation(), pReq.totalCopies()
            );
            case EBookRequestDTO eReq -> new EBook(
                eReq.ISBN(), eReq.title(), eReq.author(), eReq.genre(), eReq.publishedYear(),
                eReq.downloadURL(), eReq.format(), eReq.fileSizeMB()
            );
            case AudioBookRequestDTO aReq -> new AudioBook(
                aReq.ISBN(), aReq.title(), aReq.author(), aReq.genre(), aReq.publishedYear(),
                aReq.downloadURL(), aReq.format(), aReq.fileSizeMB(), aReq.narrator()
            );
        };
    }
}