package com.accounthub.config;

import com.accounthub.service.CustomUserDetailsService;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;

/**
 * Cấu hình bảo mật toàn diện bằng Spring Security.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationFailureHandler failureHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAuthenticationFailureHandler failureHandler) {
        this.userDetailsService = userDetailsService;
        this.failureHandler = failureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setHideUserNotFoundExceptions(true); // Không để lộ tài khoản có tồn tại hay không
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Phân quyền truy cập các URL
                .authorizeHttpRequests(authorize -> authorize
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.INCLUDE, DispatcherType.ERROR).permitAll()
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/error",
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico"
                        ).permitAll()
                        .requestMatchers("/admin/**", "/dashboard", "/profile").authenticated()
                        .anyRequest().authenticated()
                )

                // 2. Cấu hình Form Login
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("identifier") // Hỗ trợ cả email hoặc username
                        .passwordParameter("password")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .failureHandler(failureHandler)
                        .permitAll()
                )

                // 3. Cấu hình Logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // 4. Quản lý Session
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession()
                        .maximumSessions(2)
                )

                // 5. Cấu hình Headers và Chống Cache trên các trang bảo vệ
                .headers(headers -> headers
                        .cacheControl(org.springframework.security.config.Customizer.withDefaults())
                )

                // 6. Kích hoạt bảo vệ CSRF
                .csrf(org.springframework.security.config.Customizer.withDefaults())

                // Gắn Authentication Provider
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
}
