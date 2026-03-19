package com.ssdlc.library.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {

    // Custom error page — never exposes stack traces or internal details
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) statusCode = 500;

        String message = switch (statusCode) {
            case 403 -> "You do not have permission to access this page.";
            case 404 -> "The page you requested was not found.";
            default  -> "An unexpected error occurred. Please try again.";
        };

        model.addAttribute("status", statusCode);
        model.addAttribute("message", message);
        return "error";
    }
}
