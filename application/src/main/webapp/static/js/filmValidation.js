document.addEventListener("DOMContentLoaded", () => {
    const simboliNonAccettati = /[#$%^&*":{}|<>]/;

    function validaCampo(event) {
        const campo = event.target;
        const erroreId = `${campo.id}-errore`;
        let messaggioErrore = document.getElementById(erroreId);

        if (!messaggioErrore) {
            messaggioErrore = document.createElement("div");
            messaggioErrore.id = erroreId;
            messaggioErrore.style.color = "red";
            messaggioErrore.style.fontSize = "small";
            messaggioErrore.style.marginTop = "5px";
            campo.insertAdjacentElement("afterend", messaggioErrore);
        }

        if (simboliNonAccettati.test(campo.value)) {
            messaggioErrore.textContent = "Questo campo contiene simboli non ammessi.";
            campo.classList.add("error");
        } else {
            messaggioErrore.textContent = "";
            campo.classList.remove("error");
        }
    }

    const campiDaValidare = document.querySelectorAll("#titolo, #descrizione");
    campiDaValidare.forEach(campo => {
        campo.addEventListener("change", validaCampo);
    });
});