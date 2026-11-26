package com.accounthub.controller;

import com.accounthub.dto.RegisterDto;
import com.accounthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            @RequestParam(value = "identifier", required = false) String identifier,
            Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/admin/dashboard";
        }

        if (error != null) {
            String errorMsg;
            switch (error) {
                case "account_locked":
                    errorMsg = "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên.";
                    break;
                case "account_disabled":
                    errorMsg = "Tài khoản của bạn chưa được kích hoạt hoặc đã bị vô hiệu hóa.";
                    break;
                case "bad_credentials":
                default:
                    errorMsg = "Tên đăng nhập/email hoặc mật khẩu không chính xác.";
                    break;
            }
            model.addAttribute("errorMessage", errorMsg);
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "Bạn đã đăng xuất thành công khỏi hệ thống.");
        }

        if (registered != null) {
            model.addAttribute("successMessage", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
        }

        if (identifier != null && !identifier.isBlank()) {
            model.addAttribute("identifier", identifier);
        }

        return "web/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/admin/dashboard";
        }

        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new RegisterDto());
        }
        return "web/register";
    }

    @PostMapping("/register")
    public String handleRegister(
            @Valid @ModelAttribute("user") RegisterDto registerDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (registerDto.getPassword() != null && !registerDto.getPassword().isEmpty()
                && registerDto.getConfirmPassword() != null
                && !registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.confirmPassword",
                    "Mật khẩu và xác nhận mật khẩu không trùng khớp");
        }

        if (registerDto.getEmail() != null && !registerDto.getEmail().isBlank()) {
            if (userService.existsByEmail(registerDto.getEmail().trim())) {
                bindingResult.rejectValue("email", "error.email",
                        "Email này đã được sử dụng. Vui lòng chọn email khác");
            }
        }

        if (registerDto.getUsername() != null && !registerDto.getUsername().isBlank()) {
            if (userService.existsByUsername(registerDto.getUsername().trim())) {
                bindingResult.rejectValue("username", "error.username",
                        "Tên đăng nhập này đã tồn tại. Vui lòng chọn tên khác");
            }
        }

        if (bindingResult.hasErrors()) {
            return "web/register";
        }

        try {
            userService.registerUser(registerDto);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Đăng ký tài khoản thành công! Vui lòng đăng nhập để tiếp tục.");
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "web/register";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại sau.");
            return "web/register";
        }
    }
}
