package com.jwp.uts.uts_danielalfarezi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    private final List<Book> books = new ArrayList<>();
    private int nextId = 1;

    
    @GetMapping
    public List<Book> getAllBooks() {
        return books;
    }

 
    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        if (isInvalid(book)) {
            return ResponseEntity.badRequest().body("field ini tidak boleh kosong" );
        }
        book.setId(nextId++);
        books.add(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(book);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book bookDetails) {
        Optional<Book> bookOptional = books.stream().filter(b -> b.getId().equals(id)).findFirst();
        if (bookOptional.isPresent()) {
            if (isInvalid(bookDetails)) {
                return ResponseEntity.badRequest().body("field ini tidak boleh kosong");
            }
            Book book = bookOptional.get();
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setPrice(bookDetails.getPrice());
            return ResponseEntity.ok(book);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }

    private boolean isInvalid(Book book) {
        return book.getTitle() == null || book.getTitle().trim().isEmpty()
                || book.getAuthor() == null || book.getAuthor().trim().isEmpty()
                || book.getPrice() == null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        Optional<Book> bookOptional = books.stream().filter(b -> b.getId().equals(id)).findFirst();
        if (bookOptional.isPresent()) {
            books.remove(bookOptional.get());
            return ResponseEntity.ok().body("Book deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
    }
    
}

