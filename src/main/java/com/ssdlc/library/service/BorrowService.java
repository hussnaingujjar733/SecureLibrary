package com.ssdlc.library.service;

import com.ssdlc.library.model.*;
import com.ssdlc.library.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowService {

    private static final Logger log = LoggerFactory.getLogger(BorrowService.class);

    private final BorrowRequestRepository borrowRepository;
    private final BookRepository bookRepository;

    public BorrowService(BorrowRequestRepository borrowRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
    }

    public List<BorrowRequest> findAllPending() {
        return borrowRepository.findByStatus(BorrowRequest.Status.PENDING);
    }

    public List<BorrowRequest> findByUser(User user) {
        return borrowRepository.findByUser(user);
    }

    public List<BorrowRequest> findAll() {
        return borrowRepository.findAll();
    }

    @Transactional
    public BorrowRequest requestBorrow(User user, Book book) {
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available");
        }
        boolean alreadyPending = borrowRepository.existsByUserAndBookIdAndStatus(
            user, book.getId(), BorrowRequest.Status.PENDING);
        if (alreadyPending) {
            throw new IllegalStateException("You already have a pending request for this book");
        }
        BorrowRequest req = new BorrowRequest(user, book);
        BorrowRequest saved = borrowRepository.save(req);
        log.info("Borrow request created: user={}, book={}", user.getUsername(), book.getTitle());
        return saved;
    }

    @Transactional
    public void approve(Long requestId) {
        BorrowRequest req = borrowRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        req.setStatus(BorrowRequest.Status.APPROVED);
        req.setResolvedAt(LocalDateTime.now());
        Book book = req.getBook();
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        borrowRepository.save(req);
        log.info("Borrow request approved: id={}, user={}, book={}",
            requestId, req.getUser().getUsername(), book.getTitle());
    }

    @Transactional
    public void reject(Long requestId) {
        BorrowRequest req = borrowRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found"));
        req.setStatus(BorrowRequest.Status.REJECTED);
        req.setResolvedAt(LocalDateTime.now());
        borrowRepository.save(req);
        log.info("Borrow request rejected: id={}, user={}",
            requestId, req.getUser().getUsername());
    }
}
