package com.accounthub.controller;

import com.accounthub.entity.User;
import com.accounthub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class HomeController {

    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "web/home";
    }

    @GetMapping({"/admin/dashboard", "/dashboard"})
    public String showDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Optional<User> userOptional = userService.findByUsernameOrEmail(username);
        userOptional.ifPresent(user -> model.addAttribute("currentUser", user));

        return "admin/dashboard";
    }

    @GetMapping({"/admin/profile", "/profile"})
    public String showProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        Optional<User> userOptional = userService.findByUsernameOrEmail(username);
        userOptional.ifPresent(user -> model.addAttribute("currentUser", user));

        return "admin/profile";
    }
}
