@echo off
cd /d "%~dp0"
title ACCOUNT HUB - Spring Boot Server
echo ================================================================
echo           ACCOUNT HUB - QUAN LY TAI KHOAN VA CATEGORY
echo                  Spring Boot 3.3.5 + JSP + SiteMesh 3
echo ================================================================
echo.

if exist "C:\Program Files\Java\jdk-21\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    set "PATH=%JAVA_HOME%\bin;%PATH%"
)

echo [*] Kiem tra va giai phong cong 8090 neu dang bi chiem...
for /f "tokens=5" %%a in ('netstat -ano ^| findstr ":8090 " ^| findstr "LISTENING"') do (
    if not "%%a"=="0" (
        echo [!] Dang dung tien trinh PID %%a tren cong 8090...
        taskkill /F /PID %%a >nul 2>&1
    )
)
timeout /t 2 /nobreak >nul

echo [*] Moi truong: Java 21, Cong: 8090
echo [*] Dang khoi dong server... Trinh duyet se tu dong mo khi san sang.
echo.

start "" /B powershell -NoProfile -WindowStyle Hidden -Command "for ($i=0; $i -lt 60; $i++) { Start-Sleep -Seconds 1; try { $r = Invoke-WebRequest -Uri 'http://localhost:8090/login' -UseBasicParsing -TimeoutSec 2; if ($r.StatusCode -eq 200) { Start-Process 'http://localhost:8090/login'; break } } catch {} }"

call "%~dp0mvnw.cmd" spring-boot:run

echo.
echo [!] Server da dung lai.
pause
