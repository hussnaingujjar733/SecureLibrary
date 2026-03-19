package com.ssdlc.library.controller;

import com.ssdlc.library.service.UserService;
import jakarta.validation.constraints.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                            @RequestParam(required = false) String logout,
                            Model model) {
        if (error != null) model.addAttribute("error", "Invalid username or password.");
        if (logout != null) model.addAttribute("message", "You have been logged out.");
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam @NotBlank @Size(min=3, max=50) String username,
            @RequestParam @NotBlank @Email String email,
            @RequestParam @NotBlank @Size(min=8, max=100) String password,
            Model model) {
        try {
            userService.register(username, email, password);
            return "redirect:/auth/login?registered";
        } catch (IllegalArgumentException e) {
            // Generic message — no internal details exposed
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
