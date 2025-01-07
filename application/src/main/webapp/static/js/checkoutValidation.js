document.getElementById('checkoutForm').addEventListener('submit', function (event) {
    event.preventDefault();

    const nomeCarta = document.getElementById('nomeCarta').value.trim();
    const numeroCarta = document.getElementById('numeroCarta').value.trim();
    const scadenzaCarta = document.getElementById('scadenzaCarta').value.trim();
    const cvv = document.getElementById('cvv').value.trim();
    const errorMessage = document.getElementById('error-message');

    if (!/^[a-zA-Z\s]+$/.test(nomeCarta)) {
        errorMessage.textContent = "Il nome sulla carta non è valido. Inserisci solo lettere e spazi.";
        errorMessage.style.display = 'block';
        return;
    }

    if (!/^\d{16}$/.test(numeroCarta)) {
        errorMessage.textContent = "Il numero della carta deve contenere esattamente 16 cifre.";
        errorMessage.style.display = 'block';
        return;
    }

    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(scadenzaCarta)) {
        errorMessage.textContent = "La data di scadenza non è valida. Usa il formato MM/AA.";
        errorMessage.style.display = 'block';
        return;
    }

    if (!/^\d{3}$/.test(cvv)) {
        errorMessage.textContent = "Il CVV deve contenere esattamente 3 cifre.";
        errorMessage.style.display = 'block';
        return;
    }

    errorMessage.style.display = 'none';
    this.submit();
});
