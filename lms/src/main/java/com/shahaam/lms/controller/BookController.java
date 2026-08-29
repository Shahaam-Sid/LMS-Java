package com.shahaam.lms.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shahaam.lms.dto.book.AudioBookResponseDTO;
import com.shahaam.lms.dto.book.BookRequestDTO;
import com.shahaam.lms.dto.book.BookResponseDTO;
import com.shahaam.lms.dto.book.BookUpdateRequestDTO;
import com.shahaam.lms.dto.book.EBookResponseDTO;
import com.shahaam.lms.dto.book.PhysicalBookResponseDTO;
import com.shahaam.lms.services.BookService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/books")
@Validated
public class BookController {

    private final BookService bs;

    @GetMapping
    public ResponseEntity<List<BookResponseDTO>> getAllBooks() {
        List<BookResponseDTO> books = bs.getAllBooks();
        return ResponseEntity.ok(books);
    }
    @GetMapping("/{isbn}")
    public ResponseEntity<BookResponseDTO> getBook(
        @PathVariable @NotBlank @Size(min=10, max=13, message = "Invalid ISBN") String isbn
    ) {
        return ResponseEntity.ok(bs.getBook(isbn));
    }
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDTO>> searchBooks(@RequestParam @NotBlank String q) {
        List<BookResponseDTO> results = bs.searchKeyword(q);
        return ResponseEntity.ok(results);
    }
    @GetMapping("/available")
    public ResponseEntity<List<BookResponseDTO>> getAvailableBooks() {
        List<BookResponseDTO> results = bs.getAvailableBooks();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<BookResponseDTO> addBook(@RequestBody @Valid BookRequestDTO req) {
        BookResponseDTO output =  bs.addBook(req);
        String isbn;
        switch (output) {
            case PhysicalBookResponseDTO pb -> isbn = pb.ISBN();
            case EBookResponseDTO eb -> isbn = eb.ISBN();
            case AudioBookResponseDTO ab -> isbn = ab.ISBN();
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
        .buildAndExpand(isbn).toUri();
        return ResponseEntity.created(location).body(output);
    }

    @PatchMapping("/{isbn}")
    public ResponseEntity<BookResponseDTO> updateBook(
        @PathVariable @NotBlank @Size(min=10, max=13, message = "Invalid ISBN") String isbn,
        @RequestBody @Valid BookUpdateRequestDTO req
    ) {
        BookResponseDTO output = bs.updateBook(req, isbn);
        return ResponseEntity.ok(output);
    }

    @DeleteMapping("/{isbn}")
    public ResponseEntity<Void> deleteBook(
        @PathVariable @NotBlank @Size(min=10, max=13, message = "Invalid ISBN") String isbn
    ) {
        bs.removeBook(isbn);
        return ResponseEntity.noContent().build();
    }   
}