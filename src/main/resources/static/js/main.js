/**
 * ACCOUNT HUB - Frontend Script
 * Handles password visibility toggle, real-time validation, and cache prevention.
 */

document.addEventListener('DOMContentLoaded', function () {
    // 1. Password Visibility Toggle
    const toggleButtons = document.querySelectorAll('.btn-toggle-pwd');
    toggleButtons.forEach(btn => {
        btn.addEventListener('click', function () {
            const targetId = this.getAttribute('data-target');
            const pwdInput = document.getElementById(targetId);
            const icon = this.querySelector('i');

            if (pwdInput) {
                if (pwdInput.type === 'password') {
                    pwdInput.type = 'text';
                    if (icon) {
                        icon.classList.remove('bi-eye');
                        icon.classList.add('bi-eye-slash');
                    }
                    this.setAttribute('aria-label', 'Ẩn mật khẩu');
                } else {
                    pwdInput.type = 'password';
                    if (icon) {
                        icon.classList.remove('bi-eye-slash');
                        icon.classList.add('bi-eye');
                    }
                    this.setAttribute('aria-label', 'Hiện mật khẩu');
                }
            }
        });
    });

    // 2. Real-time Password Strength Meter (if present on Register page)
    const pwdInput = document.getElementById('password');
    const meterFill = document.getElementById('pwdMeterFill');
    const meterText = document.getElementById('pwdStrengthText');

    if (pwdInput && meterFill) {
        pwdInput.addEventListener('input', function () {
            const val = pwdInput.value;
            let score = 0;

            if (val.length >= 8) score++;
            if (/[a-z]/.test(val)) score++;
            if (/[A-Z]/.test(val)) score++;
            if (/\d/.test(val)) score++;
            if (/[^a-zA-Z0-9]/.test(val)) score++;

            let width = 0;
            let color = '#ef4444';
            let label = '';

            if (val.length === 0) {
                width = 0;
                label = '';
            } else if (score <= 2) {
                width = 30;
                color = '#ef4444';
                label = 'Yếu (Cần tối thiểu 8 ký tự, gồm chữ hoa, chữ thường và số)';
            } else if (score === 3 || score === 4) {
                width = 70;
                color = '#f59e0b';
                label = 'Trung bình';
            } else {
                width = 100;
                color = '#10b981';
                label = 'Mạnh & An toàn';
            }

            meterFill.style.width = width + '%';
            meterFill.style.backgroundColor = color;
            if (meterText) {
                meterText.textContent = label;
                meterText.style.color = color;
            }
        });
    }

    // 3. Confirm Password Match Checker
    const confirmPwdInput = document.getElementById('confirmPassword');
    const confirmMatchText = document.getElementById('confirmMatchText');

    if (pwdInput && confirmPwdInput && confirmMatchText) {
        function checkPasswordMatch() {
            if (!confirmPwdInput.value) {
                confirmMatchText.textContent = '';
                return;
            }
            if (pwdInput.value === confirmPwdInput.value) {
                confirmMatchText.textContent = '✓ Mật khẩu xác nhận trùng khớp';
                confirmMatchText.className = 'text-success small mt-1';
            } else {
                confirmMatchText.textContent = '✕ Mật khẩu xác nhận chưa khớp';
                confirmMatchText.className = 'text-danger small mt-1';
            }
        }
        confirmPwdInput.addEventListener('input', checkPasswordMatch);
        pwdInput.addEventListener('input', checkPasswordMatch);
    }

    // 4. Client-side Form Validation for Bootstrap
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
});

// 5. Ngăn chặn hiển thị cache khi nhấn nút Back của trình duyệt sau khi Logout
window.addEventListener('pageshow', function (event) {
    if (event.persisted || (window.performance && window.performance.navigation && window.performance.navigation.type === 2)) {
        window.location.reload();
    }
});
