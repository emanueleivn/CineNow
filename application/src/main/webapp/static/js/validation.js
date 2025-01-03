function validateForm() {
    const emailField = document.querySelector('input[name="email"]');
    const passwordField = document.querySelector('input[name="password"]');
    const errorMessage = document.getElementById('error-message');
    const email = emailField.value.trim();
    const password = passwordField.value.trim();
    const invalidPasswordRegex = /[<>"'%;()&]/;
    let hasError = false;

    // Rimuovi classe di errore prima della validazione
    emailField.classList.remove('input-error');
    passwordField.classList.remove('input-error');

    if (!email || !isValidEmail(email)) {
        emailField.classList.add('input-error');
        hasError = true;
    }

    if (!password || password.length < 8 || invalidPasswordRegex.test(password)) {
        passwordField.classList.add('input-error');
        hasError = true;
    }

    if (hasError) {
        errorMessage.style.display = "block";
        return false;
    }

    errorMessage.style.display = "none";
    return true;
}

function isValidEmail(email) {
    // Regex migliorata per validare correttamente un'email
    const emailRegex = /^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/;
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

    // Pulisce gli indizi e gli errori precedenti
    clearHints();

    if (nomeField.value.trim() === '' || invalidRegex.test(nomeField.value)) {
        showHint('nome-hint');
        nomeField.classList.add('input-error');
        isValid = false;
    }

    if (cognomeField.value.trim() === '' || invalidRegex.test(cognomeField.value)) {
        showHint('cognome-hint');
        cognomeField.classList.add('input-error');
        isValid = false;
    }

    if (!isValidEmail(emailField.value.trim())) {
        showHint('email-hint');
        emailField.classList.add('input-error');
        isValid = false;
    }

    if (passwordField.value.trim() === '' || passwordField.value.trim().length < 8 || invalidRegex.test(passwordField.value)) {
        showHint('password-hint');
        passwordField.classList.add('input-error');
        isValid = false;
    }

    if (passwordField.value.trim() !== confirmPasswordField.value.trim()) {
        showHint('confirm-password-hint');
        confirmPasswordField.classList.add('input-error');
        isValid = false;
    }

    return isValid;
}

function showHint(id) {
    const hintElement = document.getElementById(id);
    hintElement.style.display = "block";
}

function clearHints() {
    // Nascondi tutti gli indizi di errore
    document.querySelectorAll('.hint').forEach(hint => {
        hint.style.display = "none";
    });

    // Rimuovi la classe di errore dagli input
    document.querySelectorAll('input').forEach(input => {
        input.classList.remove('input-error');
    });
}

// Aggiungi evento per rimuovere errori quando l'utente digita
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', function () {
        this.classList.remove('input-error'); // Rimuove il bordo rosso
        const errorMessage = document.getElementById('error-message');
        if (errorMessage) errorMessage.style.display = "none"; // Nasconde il messaggio generale
    });
});
