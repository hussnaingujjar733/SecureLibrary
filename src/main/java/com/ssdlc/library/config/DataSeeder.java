package com.ssdlc.library.config;

import com.ssdlc.library.model.Book;
import com.ssdlc.library.model.User;
import com.ssdlc.library.repository.BookRepository;
import com.ssdlc.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository u, BookRepository b, PasswordEncoder p) {
        this.userRepository = u;
        this.bookRepository = b;
        this.passwordEncoder = p;
    }

    @Override
    public void run(String... args) {
        // Seed librarian account — password is hashed with BCrypt
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", "admin@library.com",
                passwordEncoder.encode("Admin@1234"), User.Role.LIBRARIAN));
            log.info("Default librarian account created");
        }

        // Seed regular user
        if (!userRepository.existsByUsername("hussnain")) {
            userRepository.save(new User("hussnain", "hussnain@library.com",
                passwordEncoder.encode("User@1234"), User.Role.USER));
            log.info("Default user account created");
        }

        // Seed sample books
        if (bookRepository.count() == 0) {
            bookRepository.save(new Book("Clean Code", "Robert C. Martin", "Programming",
                "A handbook of agile software craftsmanship.", 3));
            bookRepository.save(new Book("The Pragmatic Programmer", "Andrew Hunt", "Programming",
                "Your journey to mastery.", 2));
            bookRepository.save(new Book("Web Application Hacker's Handbook", "Stuttard & Pinto", "Security",
                "Finding and exploiting security flaws.", 1));
            bookRepository.save(new Book("OWASP Testing Guide", "OWASP Foundation", "Security",
                "Comprehensive web application security testing.", 2));
            bookRepository.save(new Book("Design Patterns", "Gang of Four", "Architecture",
                "Elements of reusable object-oriented software.", 2));
            log.info("Sample books seeded");
        }
    }
}
