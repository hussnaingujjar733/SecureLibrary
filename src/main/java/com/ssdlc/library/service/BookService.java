package com.ssdlc.library.service;

import com.ssdlc.library.model.Book;
import com.ssdlc.library.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

@Service
public class BookService {

    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public List<Book> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        // Uses parameterized JPQL — safe from SQL injection
        return bookRepository.searchByTitleOrCategory(keyword.trim());
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Book not found"));
    }

    public Book save(Book book) {
        Book saved = bookRepository.save(book);
        log.info("Book saved/updated: id={}, title={}", saved.getId(), saved.getTitle());
        return saved;
    }

    public void delete(Long id) {
        bookRepository.deleteById(id);
        log.info("Book deleted: id={}", id);
    }
}
