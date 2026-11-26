package com.accounthub.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Xử lý lỗi đăng nhập an toàn, cung cấp thông báo rõ ràng cho người dùng
 * mà không tiết lộ thông tin nhạy cảm của hệ thống.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String errorCode;
        if (exception instanceof LockedException) {
            errorCode = "account_locked";
        } else if (exception instanceof DisabledException) {
            errorCode = "account_disabled";
        } else if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            errorCode = "bad_credentials";
        } else {
            errorCode = "login_failed";
        }

        // Lưu lại username/email người dùng vừa nhập để điền lại vào form (nếu muốn)
        String enteredIdentifier = request.getParameter("identifier");
        String redirectUrl = "/login?error=" + errorCode;
        if (enteredIdentifier != null && !enteredIdentifier.isBlank()) {
            redirectUrl += "&identifier=" + URLEncoder.encode(enteredIdentifier, StandardCharsets.UTF_8);
        }

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
