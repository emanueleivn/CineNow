document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    const titoloInput = document.getElementById("titolo");
    const descrizioneInput = document.getElementById("descrizione");
    const durataInput = document.getElementById("durata");

    form.addEventListener("submit", function (event) {
        let isValid = true;
        const specialCharsRegex = /[^a-zA-Z0-9 ,.!()""-_;\[\]{}*+]/;
        if (specialCharsRegex.test(titoloInput.value)) {
            alert("Il campo Titolo non può contenere caratteri speciali non consentiti.");
            isValid = false;
        }
        if (specialCharsRegex.test(descrizioneInput.value)) {
            alert("Il campo Descrizione non può contenere caratteri speciali non consentiti.");
            isValid = false;
        }
        if (isNaN(durataInput.value) || durataInput.value <= 0) {
            alert("Il campo Durata deve essere un numero positivo.");
            isValid = false;
        }
        if (!isValid) {
            event.preventDefault();
        }
    });
});
