function validateForm() {
    const emailField = document.querySelector('input[name="email"]');
    const passwordField = document.querySelector('input[name="password"]');
    const errorMessage = document.getElementById('error-message');
    const email = emailField.value.trim();
    const password = passwordField.value.trim();
    const invalidPasswordRegex = /[<>"'%;()&]/;

    if (!email || !isValidEmail(email) || invalidPasswordRegex.test(password) || password === '') {
        errorMessage.style.display = "block";
        return false;
    }

    errorMessage.style.display = "none";
    return true;
}

function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function validateFormReg() {
    const nomeField = document.getElementById('nome');
    const cognomeField = document.getElementById('cognome');
    const emailField = document.getElementById('email');
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirm-password');
    const invalidRegex = /[<>\"'%;()&]/;
    let isValid = true;

    clearHints();

    if (nomeField.value.trim() === '' || invalidRegex.test(nomeField.value)) {
        showHint('nome-hint');
        nomeField.style.border = "2px solid red";
        isValid = false;
    }

    if (cognomeField.value.trim() === '' || invalidRegex.test(cognomeField.value)) {
        showHint('cognome-hint');
        cognomeField.style.border = "2px solid red";
        isValid = false;
    }

    if (!isValidEmail(emailField.value.trim())) {
        showHint('email-hint');
        emailField.style.border = "2px solid red";
        isValid = false;
    }

    if (passwordField.value.trim() === '' || invalidRegex.test(passwordField.value)) {
        showHint('password-hint');
        passwordField.style.border = "2px solid red";
        isValid = false;
    }

    if (passwordField.value.trim() !== confirmPasswordField.value.trim()) {
        showHint('confirm-password-hint');
        confirmPasswordField.style.border = "2px solid red";
        isValid = false;
    }

    return isValid;
}

function showHint(id) {
    const hintElement = document.getElementById(id);
    hintElement.style.display = "block";
}

function clearHints() {
    document.querySelectorAll('.hint').forEach(hint => {
        hint.style.display = "none";
    });
    document.querySelectorAll('input').forEach(input => {
        input.style.border = "";
    });
}

document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', function () {
        this.style.border = "";
        const hintId = this.id + '-hint';
        const hintElement = document.getElementById(hintId);
        if (hintElement) {
            hintElement.style.display = "none";
        }
    });
});