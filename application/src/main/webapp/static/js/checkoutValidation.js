document.getElementById('checkoutForm').addEventListener('submit', function (event) {
    event.preventDefault(); // Blocca l'invio predefinito del form

    const nomeCarta = document.getElementById('nomeCarta').value.trim();
    const numeroCarta = document.getElementById('numeroCarta').value.trim();
    const scadenzaCarta = document.getElementById('scadenzaCarta').value.trim();
    const cvv = document.getElementById('cvv').value.trim();
    const errorMessage = document.getElementById('error-message');

    // Validazione nome sulla carta (non vuoto, solo lettere e spazi)
    if (!/^[a-zA-Z\s]+$/.test(nomeCarta)) {
        errorMessage.textContent = "Il nome sulla carta non è valido. Inserisci solo lettere e spazi.";
        errorMessage.style.display = 'block';
        return;
    }

    // Validazione numero della carta (16 cifre)
    if (!/^\d{16}$/.test(numeroCarta)) {
        errorMessage.textContent = "Il numero della carta deve contenere esattamente 16 cifre.";
        errorMessage.style.display = 'block';
        return;
    }

    // Validazione data di scadenza (MM/AA)
    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(scadenzaCarta)) {
        errorMessage.textContent = "La data di scadenza non è valida. Usa il formato MM/AA.";
        errorMessage.style.display = 'block';
        return;
    }

    // Validazione CVV (3 cifre)
    if (!/^\d{3}$/.test(cvv)) {
        errorMessage.textContent = "Il CVV deve contenere esattamente 3 cifre.";
        errorMessage.style.display = 'block';
        return;
    }

    // Tutti i campi sono validi, invio del form
    errorMessage.style.display = 'none';
    this.submit();
});
