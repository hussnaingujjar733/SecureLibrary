package com.ssdlc.library.controller;

import com.ssdlc.library.model.Book;
import com.ssdlc.library.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('LIBRARIAN')")   // RBAC — only LIBRARIAN role allowed
public class AdminController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final BookService bookService;
    private final BorrowService borrowService;

    public AdminController(BookService bookService, BorrowService borrowService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("pendingRequests", borrowService.findAllPending());
        model.addAttribute("allRequests", borrowService.findAll());
        return "admin/dashboard";
    }

    // ── Book CRUD ─────────────────────────────────────────────

    @GetMapping("/books/new")
    public String newBookForm(Model model) {
        model.addAttribute("book", new Book());
        return "admin/book-form";
    }

    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book) {
        bookService.save(book);
        return "redirect:/admin?bookSaved";
    }

    @GetMapping("/books/{id}/edit")
    public String editBookForm(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.findById(id));
        return "admin/book-form";
    }

    @PostMapping("/books/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return "redirect:/admin?bookDeleted";
    }

    // ── Borrow request management ─────────────────────────────

    @PostMapping("/borrow/{id}/approve")
    public String approve(@PathVariable Long id) {
        try {
            borrowService.approve(id);
        } catch (Exception e) {
            log.warn("Approve failed for request {}: {}", id, e.getMessage());
        }
        return "redirect:/admin";
    }

    @PostMapping("/borrow/{id}/reject")
    public String reject(@PathVariable Long id) {
        borrowService.reject(id);
        return "redirect:/admin";
    }
}
