package com.accounthub;

import com.accounthub.entity.Category;
import com.accounthub.entity.User;
import com.accounthub.repository.CategoryRepository;
import com.accounthub.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.util.HtmlUtils;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebIntegrationTest {

    private static final Pattern INPUT_TAG_PATTERN = Pattern.compile("<input\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern CSRF_NAME_PATTERN = Pattern.compile("\\bname\\s*=\\s*[\"']_csrf[\"']", Pattern.CASE_INSENSITIVE);
    private static final Pattern VALUE_ATTR_PATTERN = Pattern.compile("\\bvalue\\s*=\\s*[\"']([^\"']*)[\"']", Pattern.CASE_INSENSITIVE);

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private CookieManager newCookieManager() {
        return new CookieManager(null, CookiePolicy.ACCEPT_ALL);
    }

    private HttpClient newClient(CookieManager cookieManager) {
        return HttpClient.newBuilder()
                .cookieHandler(cookieManager)
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
    }

    private HttpResponse<String> get(HttpClient client, String path) throws Exception {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + path))
                .GET()
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private HttpResponse<String> postForm(HttpClient client, String path, Map<String, String> params) throws Exception {
        StringBuilder body = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (body.length() > 0) {
                body.append('&');
            }
            body.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            body.append('=');
            body.append(URLEncoder.encode(entry.getValue() != null ? entry.getValue() : "", StandardCharsets.UTF_8));
        }

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + path))
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
                .build();
        return client.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }

    private List<String> extractAllCsrfTokens(String html) {
        List<String> tokens = new ArrayList<>();
        Matcher inputMatcher = INPUT_TAG_PATTERN.matcher(html);
        while (inputMatcher.find()) {
            String tag = inputMatcher.group();
            if (CSRF_NAME_PATTERN.matcher(tag).find()) {
                Matcher valueMatcher = VALUE_ATTR_PATTERN.matcher(tag);
                if (valueMatcher.find()) {
                    tokens.add(valueMatcher.group(1));
                }
            }
        }
        return tokens;
    }

    private String extractFirstCsrfToken(String html) {
        List<String> tokens = extractAllCsrfTokens(html);
        assertFalse(tokens.isEmpty(), "Expected at least one _csrf token input in HTML");
        String token = tokens.get(0);
        assertFalse(token.isBlank(), "_csrf token must not be blank");
        return token;
    }

    private HttpClient loginAs(String identifier, String password, CookieManager cm) throws Exception {
        HttpClient client = newClient(cm);
        HttpResponse<String> loginPage = get(client, "/login");
        assertEquals(200, loginPage.statusCode());
        String csrf = extractFirstCsrfToken(loginPage.body());

        Map<String, String> form = new LinkedHashMap<>();
        form.put("_csrf", csrf);
        form.put("identifier", identifier);
        form.put("password", password);

        HttpResponse<String> loginPost = postForm(client, "/login", form);
        assertEquals(302, loginPost.statusCode());
        String location = loginPost.headers().firstValue("Location").orElse("");
        assertTrue(location.endsWith("/admin/dashboard"), "Expected redirect to /admin/dashboard, got: " + location);
        return client;
    }

    @Test
    @DisplayName("E2E 1: JSP, SiteMesh 3 decorators, UTF-8 Vietnamese, Static CSS/JS/SVG & Route Protection")
    void testJspSiteMeshStaticAndRouteProtection() throws Exception {
        HttpClient client = newClient(newCookieManager());

        // Home page wrapped by decorators/web.jsp
        HttpResponse<String> homeResp = get(client, "/");
        assertEquals(200, homeResp.statusCode());
        String homeHtml = homeResp.body();
        assertTrue(homeHtml.contains("ACCOUNT HUB"));
        assertTrue(homeHtml.contains("app-navbar"), "Expected web.jsp decorator navbar");
        assertTrue(homeHtml.contains("app-footer"), "Expected web.jsp decorator footer");
        assertTrue(homeHtml.contains("Hệ thống Quản lý Category"), "Expected Vietnamese UTF-8 rendering");

        // Login page excluded from SiteMesh decorators
        HttpResponse<String> loginResp = get(client, "/login");
        assertEquals(200, loginResp.statusCode());
        String loginHtml = loginResp.body();
        assertTrue(loginHtml.contains("ACCOUNT HUB"));
        assertFalse(loginHtml.contains("app-navbar"), "Login page must not be wrapped by navbar decorator");
        assertFalse(loginHtml.contains("app-footer"), "Login page must not be wrapped by footer decorator");
        extractFirstCsrfToken(loginHtml);

        // Register page excluded from SiteMesh decorators and has exactly 1 _csrf input
        HttpResponse<String> registerResp = get(client, "/register");
        assertEquals(200, registerResp.statusCode());
        String registerHtml = registerResp.body();
        assertFalse(registerHtml.contains("app-navbar"), "Register page must not be wrapped by navbar decorator");
        assertFalse(registerHtml.contains("app-footer"), "Register page must not be wrapped by footer decorator");
        List<String> registerCsrfTokens = extractAllCsrfTokens(registerHtml);
        assertEquals(1, registerCsrfTokens.size(), "Register form must render exactly one _csrf input");
        assertFalse(registerCsrfTokens.get(0).isBlank());

        // Static CSS, JS, and SVG image
        HttpResponse<String> cssResp = get(client, "/static/css/style.css");
        assertEquals(200, cssResp.statusCode());
        assertTrue(cssResp.body().contains("ACCOUNT HUB"));

        HttpResponse<String> jsResp = get(client, "/static/js/main.js");
        assertEquals(200, jsResp.statusCode());
        assertTrue(jsResp.body().contains("ACCOUNT HUB"));

        HttpResponse<String> imgResp = get(client, "/static/images/logo.svg");
        assertEquals(200, imgResp.statusCode());
        assertTrue(imgResp.body().contains("<svg"));

        // Unauthenticated requests to protected URLs redirect to /login
        for (String protectedPath : List.of(
                "/admin/dashboard",
                "/admin/profile",
                "/admin/categories/searchpaginated",
                "/dashboard",
                "/profile")) {
            HttpResponse<String> resp = get(client, protectedPath);
            assertEquals(302, resp.statusCode(), "Expected 302 for unauthenticated " + protectedPath);
            String loc = resp.headers().firstValue("Location").orElse("");
            assertTrue(loc.endsWith("/login"), "Expected redirect to /login for " + protectedPath + ", got " + loc);
        }
    }

    @Test
    @DisplayName("E2E 2: Register Validation, Duplicate Email/Username, Success & BCrypt Storage")
    void testRegisterFlow() throws Exception {
        String testUsername = "tranminh99";
        String testEmail = "minhtran@example.com";
        userRepository.findByUsername(testUsername).ifPresent(userRepository::delete);

        try {
            HttpClient client = newClient(newCookieManager());

            // Empty submit
            String csrf = extractFirstCsrfToken(get(client, "/register").body());
            Map<String, String> emptyForm = new LinkedHashMap<>();
            emptyForm.put("_csrf", csrf);
            emptyForm.put("fullName", "");
            emptyForm.put("email", "");
            emptyForm.put("username", "");
            emptyForm.put("password", "");
            emptyForm.put("confirmPassword", "");
            HttpResponse<String> emptyResp = postForm(client, "/register", emptyForm);
            assertEquals(200, emptyResp.statusCode());
            assertTrue(HtmlUtils.htmlUnescape(emptyResp.body()).contains("không được để trống"));

            // Weak password + mismatch
            csrf = extractFirstCsrfToken(emptyResp.body());
            Map<String, String> badPwdForm = new LinkedHashMap<>();
            badPwdForm.put("_csrf", csrf);
            badPwdForm.put("fullName", "Trần Minh");
            badPwdForm.put("email", "badpwd@example.com");
            badPwdForm.put("username", "badpwduser");
            badPwdForm.put("password", "123");
            badPwdForm.put("confirmPassword", "456");
            HttpResponse<String> badPwdResp = postForm(client, "/register", badPwdForm);
            assertEquals(200, badPwdResp.statusCode());
            String badPwdUnescaped = HtmlUtils.htmlUnescape(badPwdResp.body());
            assertTrue(badPwdUnescaped.contains("không trùng khớp"));
            assertTrue(badPwdUnescaped.contains("tối thiểu 8 ký tự") || badPwdUnescaped.contains("chữ hoa"));

            // Duplicate email
            csrf = extractFirstCsrfToken(badPwdResp.body());
            Map<String, String> dupEmailForm = new LinkedHashMap<>();
            dupEmailForm.put("_csrf", csrf);
            dupEmailForm.put("fullName", "Người Test");
            dupEmailForm.put("email", "nguyenbaongan@example.com");
            dupEmailForm.put("username", "uniqueuser1");
            dupEmailForm.put("password", "StrongPass@123");
            dupEmailForm.put("confirmPassword", "StrongPass@123");
            HttpResponse<String> dupEmailResp = postForm(client, "/register", dupEmailForm);
            assertEquals(200, dupEmailResp.statusCode());
            assertTrue(HtmlUtils.htmlUnescape(dupEmailResp.body()).contains("Email này đã được sử dụng"));

            // Duplicate username
            csrf = extractFirstCsrfToken(dupEmailResp.body());
            Map<String, String> dupUserForm = new LinkedHashMap<>();
            dupUserForm.put("_csrf", csrf);
            dupUserForm.put("fullName", "Người Test");
            dupUserForm.put("email", "unique1@example.com");
            dupUserForm.put("username", "nguyenbao");
            dupUserForm.put("password", "StrongPass@123");
            dupUserForm.put("confirmPassword", "StrongPass@123");
            HttpResponse<String> dupUserResp = postForm(client, "/register", dupUserForm);
            assertEquals(200, dupUserResp.statusCode());
            assertTrue(HtmlUtils.htmlUnescape(dupUserResp.body()).contains("Tên đăng nhập này đã tồn tại"));

            // Successful registration
            csrf = extractFirstCsrfToken(dupUserResp.body());
            Map<String, String> validForm = new LinkedHashMap<>();
            validForm.put("_csrf", csrf);
            validForm.put("fullName", "Trần Thị Minh");
            validForm.put("email", testEmail);
            validForm.put("username", testUsername);
            validForm.put("password", "StrongPass@123");
            validForm.put("confirmPassword", "StrongPass@123");
            HttpResponse<String> successResp = postForm(client, "/register", validForm);
            assertEquals(302, successResp.statusCode());
            String loc = successResp.headers().firstValue("Location").orElse("");
            assertTrue(loc.contains("/login?registered=true"), "Expected redirect to /login?registered=true, got: " + loc);

            HttpResponse<String> loginAfterReg = get(client, "/login?registered=true");
            assertEquals(200, loginAfterReg.statusCode());
            assertTrue(loginAfterReg.body().contains("Đăng ký tài khoản thành công"));

            Optional<User> savedOpt = userRepository.findByUsername(testUsername);
            assertTrue(savedOpt.isPresent());
            User saved = savedOpt.get();
            assertEquals("Trần Thị Minh", saved.getFullName());
            assertEquals(testEmail, saved.getEmail());
            assertNotEquals("StrongPass@123", saved.getPassword());
            assertTrue(saved.getPassword().startsWith("$2a$") || saved.getPassword().startsWith("$2b$"));
            assertEquals(60, saved.getPassword().length());
            assertTrue(passwordEncoder.matches("StrongPass@123", saved.getPassword()));
        } finally {
            userRepository.findByUsername(testUsername).ifPresent(userRepository::delete);
        }
    }

    @Test
    @DisplayName("E2E 3: Login (Wrong Password, Locked Account, Username & Email), Admin SiteMesh, Logout & Session Invalidation")
    void testLoginAdminDecoratorAndLogoutFlow() throws Exception {
        // 1. Bad credentials
        HttpClient badClient = newClient(newCookieManager());
        String badCsrf = extractFirstCsrfToken(get(badClient, "/login").body());
        HttpResponse<String> badPost = postForm(badClient, "/login", Map.of(
                "_csrf", badCsrf,
                "identifier", "nguyenbao",
                "password", "WrongPassword123"
        ));
        assertEquals(302, badPost.statusCode());
        String badLoc = badPost.headers().firstValue("Location").orElse("");
        assertTrue(badLoc.contains("/login?error=bad_credentials"), "Got: " + badLoc);
        HttpResponse<String> badPage = get(badClient, URI.create(badLoc).getPath() + "?" + URI.create(badLoc).getRawQuery());
        assertEquals(200, badPage.statusCode());
        assertTrue(badPage.body().contains("Tên đăng nhập/email hoặc mật khẩu không chính xác."));
        assertTrue(badPage.body().contains("value=\"nguyenbao\""));

        // 2. Locked account
        HttpClient lockedClient = newClient(newCookieManager());
        String lockedCsrf = extractFirstCsrfToken(get(lockedClient, "/login").body());
        HttpResponse<String> lockedPost = postForm(lockedClient, "/login", Map.of(
                "_csrf", lockedCsrf,
                "identifier", "lockeduser",
                "password", "Password@123"
        ));
        assertEquals(302, lockedPost.statusCode());
        String lockedLoc = lockedPost.headers().firstValue("Location").orElse("");
        assertTrue(lockedLoc.contains("/login?error=account_locked"), "Got: " + lockedLoc);
        HttpResponse<String> lockedPage = get(lockedClient, URI.create(lockedLoc).getPath() + "?" + URI.create(lockedLoc).getRawQuery());
        assertEquals(200, lockedPage.statusCode());
        assertTrue(lockedPage.body().contains("Tài khoản của bạn đã bị khóa"));

        // 3. Login by username + Admin SiteMesh + Cache-Control + Profile + Web navbar auth state
        CookieManager cm = newCookieManager();
        HttpClient authClient = loginAs("nguyenbao", "Password@123", cm);

        HttpResponse<String> dashResp = get(authClient, "/admin/dashboard");
        assertEquals(200, dashResp.statusCode());
        String cacheHeader = dashResp.headers().firstValue("Cache-Control").orElse("");
        assertTrue(cacheHeader.contains("no-store"), "Expected no-store Cache-Control header, got: " + cacheHeader);
        String dashHtml = dashResp.body();
        assertTrue(dashHtml.contains("ACCOUNT HUB - ADMIN"));
        assertTrue(dashHtml.contains("Nguyễn Bảo Ngân"));
        assertTrue(dashHtml.contains("Đăng Xuất"));
        assertTrue(dashHtml.contains("nguyenbao"));

        HttpResponse<String> profileResp = get(authClient, "/admin/profile");
        assertEquals(200, profileResp.statusCode());
        assertTrue(profileResp.body().contains("ACCOUNT HUB - ADMIN"));
        assertTrue(profileResp.body().contains("nguyenbaongan@example.com"));
        assertTrue(profileResp.body().matches("(?s).*\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}.*"),
                "Expected formatted createdAt timestamp dd/MM/yyyy HH:mm:ss");

        HttpResponse<String> authedHome = get(authClient, "/");
        assertEquals(200, authedHome.statusCode());
        assertTrue(authedHome.body().contains("Dashboard"));
        assertTrue(authedHome.body().contains("Đăng Xuất"));

        // Capture session id before logout
        String sessionIdBeforeLogout = cm.getCookieStore().getCookies().stream()
                .filter(c -> "JSESSIONID".equalsIgnoreCase(c.getName()))
                .map(HttpCookie::getValue)
                .findFirst()
                .orElse("");
        assertFalse(sessionIdBeforeLogout.isBlank());

        // Logout
        String logoutCsrf = extractFirstCsrfToken(dashHtml);
        HttpResponse<String> logoutPost = postForm(authClient, "/logout", Map.of("_csrf", logoutCsrf));
        assertEquals(302, logoutPost.statusCode());
        String logoutLoc = logoutPost.headers().firstValue("Location").orElse("");
        assertTrue(logoutLoc.endsWith("/login?logout=true"), "Got: " + logoutLoc);

        HttpResponse<String> logoutPage = get(authClient, "/login?logout=true");
        assertEquals(200, logoutPage.statusCode());
        assertTrue(logoutPage.body().contains("Bạn đã đăng xuất thành công khỏi hệ thống."));

        // Protected pages blocked after logout (same client & replayed old JSESSIONID)
        HttpResponse<String> afterLogoutDash = get(authClient, "/admin/dashboard");
        assertEquals(302, afterLogoutDash.statusCode());
        assertTrue(afterLogoutDash.headers().firstValue("Location").orElse("").endsWith("/login"));

        HttpClient replayClient = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NEVER).build();
        HttpRequest replayReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl() + "/admin/categories/searchpaginated"))
                .header("Cookie", "JSESSIONID=" + sessionIdBeforeLogout)
                .GET()
                .build();
        HttpResponse<String> replayResp = replayClient.send(replayReq, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        assertEquals(302, replayResp.statusCode());
        assertTrue(replayResp.headers().firstValue("Location").orElse("").endsWith("/login"));

        // 4. Login by email
        HttpClient emailClient = loginAs("nguyenbaongan@example.com", "Password@123", newCookieManager());
        HttpResponse<String> emailDash = get(emailClient, "/admin/dashboard");
        assertEquals(200, emailDash.statusCode());
        assertTrue(emailDash.body().contains("Nguyễn Bảo Ngân"));
    }

    @Test
    @DisplayName("E2E 4: Category CRUD, Case-Insensitive Vietnamese Search, Pagination & Keyword Retention")
    void testCategoryCrudSearchAndPagination() throws Exception {
        HttpClient client = loginAs("nguyenbao", "Password@123", newCookieManager());

        // 1. Default pagination (8 seeded items, page size 3 => 3 pages)
        HttpResponse<String> listPage1 = get(client, "/admin/categories/searchpaginated");
        assertEquals(200, listPage1.statusCode());
        String html1 = listPage1.body();
        assertTrue(html1.contains("ACCOUNT HUB - ADMIN"));
        assertTrue(html1.contains("Trang 1 / 3"));
        assertTrue(html1.contains("<strong>3</strong> / <strong>8</strong>"));
        assertTrue(html1.contains("aria-label=\"Trang trước\""));
        assertTrue(html1.contains("aria-label=\"Trang sau\""));

        // 2. Partial Vietnamese case-insensitive search ("điện")
        HttpResponse<String> partialSearch = get(client,
                "/admin/categories/searchpaginated?name=" + URLEncoder.encode("điện", StandardCharsets.UTF_8));
        assertEquals(200, partialSearch.statusCode());
        assertTrue(partialSearch.body().contains("Điện thoại di động"));

        // 3. Exact search
        HttpResponse<String> exactSearch = get(client,
                "/admin/categories/searchpaginated?name=" + URLEncoder.encode("Laptop & Máy tính xách tay", StandardCharsets.UTF_8));
        assertEquals(200, exactSearch.statusCode());
        assertTrue(exactSearch.body().contains("Laptop"));
        assertTrue(exactSearch.body().contains("<strong>1</strong> / <strong>1</strong>"));

        // 4. Empty search result
        HttpResponse<String> noResult = get(client, "/admin/categories/searchpaginated?name=khongtontai999");
        assertEquals(200, noResult.statusCode());
        assertTrue(noResult.body().contains("No Category"));

        // 5. Switch to page 2
        HttpResponse<String> page2 = get(client, "/admin/categories/searchpaginated?page=2&size=3");
        assertEquals(200, page2.statusCode());
        assertTrue(page2.body().contains("Trang 2 / 3"));

        // 6. Change page size to 5
        HttpResponse<String> size5 = get(client, "/admin/categories/searchpaginated?size=5");
        assertEquals(200, size5.statusCode());
        assertTrue(size5.body().contains("Trang 1 / 2"));
        assertTrue(size5.body().contains("<strong>5</strong> / <strong>8</strong>"));

        // 7. Out-of-bounds page clamps cleanly to last page
        HttpResponse<String> overflowPage = get(client, "/admin/categories/searchpaginated?page=99&size=3");
        assertEquals(200, overflowPage.statusCode());
        assertTrue(overflowPage.body().contains("Trang 3 / 3"));

        // 8. Retain keyword when changing page
        String kwEncoded = URLEncoder.encode("máy", StandardCharsets.UTF_8);
        HttpResponse<String> keywordPage2 = get(client, "/admin/categories/searchpaginated?name=" + kwEncoded + "&size=2&page=2");
        assertEquals(200, keywordPage2.statusCode());
        assertTrue(keywordPage2.body().contains("Trang 2 / 2"));
        assertTrue(keywordPage2.body().toLowerCase().contains("name=" + kwEncoded.toLowerCase())
                        || keywordPage2.body().contains("name=máy"),
                () -> "Pagination links must retain encoded keyword. Body:\n" + keywordPage2.body());

        // 9. Add Category (blank name rejected + valid save)
        Long createdId = null;
        try {
            HttpResponse<String> addPage = get(client, "/admin/categories/add");
            assertEquals(200, addPage.statusCode());
            String addCsrf = extractFirstCsrfToken(addPage.body());

            HttpResponse<String> blankSave = postForm(client, "/admin/categories/save", Map.of(
                    "_csrf", addCsrf,
                    "name", "   ",
                    "images", "",
                    "status", "1"
            ));
            assertEquals(302, blankSave.statusCode());
            assertTrue(blankSave.headers().firstValue("Location").orElse("").endsWith("/admin/categories/add"));
            HttpResponse<String> addErrorPage = get(client, "/admin/categories/add");
            assertTrue(addErrorPage.body().contains("Tên danh mục không được để trống!"));

            String createdName = "Thiết bị mạng Test";
            String saveCsrf = extractFirstCsrfToken(addErrorPage.body());
            HttpResponse<String> saveResp = postForm(client, "/admin/categories/save", Map.of(
                    "_csrf", saveCsrf,
                    "name", createdName,
                    "images", "router.png",
                    "status", "1"
            ));
            assertEquals(302, saveResp.statusCode());
            assertTrue(saveResp.headers().firstValue("Location").orElse("").endsWith("/admin/categories/searchpaginated"));

            HttpResponse<String> afterAddList = get(client, "/admin/categories/searchpaginated");
            assertTrue(afterAddList.body().contains("Thêm danh mục mới thành công!"));

            List<Category> found = categoryRepository.findByNameContaining(createdName);
            assertEquals(1, found.size());
            createdId = found.get(0).getId();

            // 10. Edit Category
            HttpResponse<String> editPage = get(client, "/admin/categories/edit/" + createdId);
            assertEquals(200, editPage.statusCode());
            assertTrue(editPage.body().contains(createdName));
            String editCsrf = extractFirstCsrfToken(editPage.body());

            String updatedName = "Thiết bị mạng Updated";
            HttpResponse<String> updateResp = postForm(client, "/admin/categories/save", Map.of(
                    "_csrf", editCsrf,
                    "id", String.valueOf(createdId),
                    "name", updatedName,
                    "images", "router2.png",
                    "status", "0"
            ));
            assertEquals(302, updateResp.statusCode());

            HttpResponse<String> afterEditList = get(client,
                    "/admin/categories/searchpaginated?name=" + URLEncoder.encode(updatedName, StandardCharsets.UTF_8));
            assertEquals(200, afterEditList.statusCode());
            assertTrue(afterEditList.body().contains(updatedName));
            assertTrue(afterEditList.body().contains("router2.png"));
            assertTrue(afterEditList.body().contains("Tạm khóa"));

            // 11. Delete Category
            HttpResponse<String> deleteResp = get(client, "/admin/categories/delete/" + createdId);
            assertEquals(302, deleteResp.statusCode());
            assertTrue(deleteResp.headers().firstValue("Location").orElse("").endsWith("/admin/categories/searchpaginated"));

            HttpResponse<String> afterDeleteList = get(client, "/admin/categories/searchpaginated");
            assertTrue(afterDeleteList.body().contains("Xóa danh mục thành công!"));
            assertTrue(categoryRepository.findById(createdId).isEmpty());
            createdId = null;
        } finally {
            if (createdId != null) {
                categoryRepository.deleteById(createdId);
            }
        }
    }

    @Test
    @DisplayName("E2E 5: Verify BCrypt password hashes in database/data.sql")
    void testDataSqlBcryptHashes() throws Exception {
        String sql = Files.readString(Path.of("database", "data.sql"), StandardCharsets.UTF_8);
        Matcher m = Pattern.compile("\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}").matcher(sql);
        List<String> hashes = new ArrayList<>();
        while (m.find()) {
            hashes.add(m.group());
        }
        assertEquals(3, hashes.size(), "Expected 3 valid 60-character BCrypt hashes in database/data.sql");
        assertTrue(passwordEncoder.matches("Password@123", hashes.get(0)), "nguyenbao hash must match Password@123");
        assertTrue(passwordEncoder.matches("Admin@123", hashes.get(1)), "admin hash must match Admin@123");
        assertTrue(passwordEncoder.matches("Password@123", hashes.get(2)), "lockeduser hash must match Password@123");
    }
}
