package com.ssdlc.library.controller;

import com.ssdlc.library.model.*;
import com.ssdlc.library.service.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final UserService userService;

    public BookController(BookService bookService, BorrowService borrowService, UserService userService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.userService = userService;
    }

    @GetMapping({"", "/"})
    public String listBooks(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("books", bookService.search(search));
        model.addAttribute("search", search);
        return "books/list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("books", bookService.search(keyword));
        model.addAttribute("search", keyword);
        return "books/list";
    }

    @PostMapping("/{id}/borrow")
    public String requestBorrow(@PathVariable Long id,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {
        try {
            User user = userService.findByUsername(userDetails.getUsername());
            Book book = bookService.findById(id);
            borrowService.requestBorrow(user, book);
            return "redirect:/books?success=borrowed";
        } catch (IllegalStateException e) {
            model.addAttribute("books", bookService.findAll());
            model.addAttribute("error", e.getMessage());
            return "books/list";
        }
    }

    @GetMapping("/my-requests")
    public String myRequests(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = userService.findByUsername(userDetails.getUsername());
        model.addAttribute("requests", borrowService.findByUser(user));
        return "borrow/my-requests";
    }
}
