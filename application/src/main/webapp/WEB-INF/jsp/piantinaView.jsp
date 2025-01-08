<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.unisa.application.model.entity.PostoProiezione" %>
<%@ page import="it.unisa.application.model.entity.Proiezione" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seleziona Posto</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .posto {
            width: 30px;
            height: 30px;
            margin: 2px;
            text-align: center;
            line-height: 30px;
            border-radius: 5px;
            cursor: pointer;
        }

        .posto-disponibile {
            background-color: #28a745;
            color: white;
        }

        .posto-occupato {
            background-color: #dc3545;
            color: white;
            cursor: not-allowed;
        }

        .posto-selezionato {
            background-color: #ffc107;
            color: black;
        }

        .posto:hover:not(.posto-occupato) {
            background-color: #218838;
        }

        .fila-label {
            font-weight: bold;
            margin-right: 10px;
            letter-spacing: 2px;
        }

        .checkout-section {
            padding: 15px;
        }

        .checkout-section h3, .checkout-section h4 {
            font-weight: bold;
        }

        .checkout-section button {
            width: 100%;
        }

        .schermo {
            width: 80%;
            background-color: #343a40;
            color: white;
            border-radius: 5px;
            font-weight: bold;
            letter-spacing: 2px;
        }

    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-8 mb-5">
            <h1 class="text-center mb-4">Seleziona un Posto</h1>
            <div class="text-center mb-3">
                <span class="posto posto-disponibile">Disponibile</span>
                <span> - </span>
                <span class="posto posto-occupato">Occupato</span>
                <span> - </span>
                <span class="posto posto-selezionato">Selezionato</span>
            </div>
            <div class="d-flex justify-content-center mb-4">
                <div class="schermo p-2 text-center">
                    Schermo
                </div>
            </div>

            <div class="d-flex flex-wrap justify-content-center">
                <%
                    List<PostoProiezione> posti = (List<PostoProiezione>) request.getAttribute("postiProiezione");
                    if (posti != null && !posti.isEmpty()) {
                        char currentFila = 0;
                        for (PostoProiezione postoProiezione : posti) {
                            char fila = postoProiezione.getPosto().getFila();
                            int numero = postoProiezione.getPosto().getNumero();
                            boolean disponibile = postoProiezione.isStato();

                            if (fila != currentFila) {
                                if (currentFila != 0) {
                %>
            </div>
            <div class="d-flex flex-wrap justify-content-center">
                <%
                    }
                    currentFila = fila;
                %>
                <div class="col-12 text-center fila-label mb-2">Fila <%= fila %></div>
                <%
                    }
                %>
                <div class="posto <%= disponibile ? "posto-disponibile" : "posto-occupato" %> m-1"
                     data-fila="<%= String.valueOf(fila) %>"
                     data-numero="<%= numero %>"
                     id="posto-<%= fila %>-<%= numero %>">
                    <%= numero %>
                </div>
                <%
                    }
                } else {
                %>
                <div class="col-12 text-center">
                    <p class="text-muted">Nessun posto disponibile per questa proiezione.</p>
                </div>
                <%
                    }
                %>
            </div>
        </div>

        <div class="col-md-4">
            <div class="checkout-section sticky-top" style="top: 20px;">
                <h3 class="text-center">Riepilogo</h3>
                <h4 class="text-center">Totale: €<span id="totalPrice">0</span></h4>
                <div class="mt-4">
                    <button class="btn btn-danger mb-2" onclick="resetSelection()">Annulla Selezione</button>
                    <form id="checkoutForm" action="${pageContext.request.contextPath}/Checkout" method="get">
                        <input type="hidden" name="proiezioneId"
                               value="<%= ((Proiezione) request.getAttribute("proiezione")).getId() %>">
                        <input type="hidden" name="posti" id="selectedPostiInput">
                        <input type="hidden" name="totale" id="totalPriceInput">
                        <button type="submit" class="btn btn-primary">Checkout</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>

<script>
    let selectedSeats = [];
    let total = 0;
    const seatPrice = 7;

    const availableSeats = document.querySelectorAll('.posto-disponibile');
    const totalPriceEl = document.getElementById('totalPrice');
    const totalPriceInput = document.getElementById('totalPriceInput');
    const selectedPostiInput = document.getElementById('selectedPostiInput');
    const checkoutButton = document.querySelector('#checkoutForm button[type="submit"]');

    function updateSummary() {
        totalPriceEl.textContent = total;
        totalPriceInput.value = total;
        selectedPostiInput.value = selectedSeats.join(',');
    }

    availableSeats.forEach(seat => {
        seat.addEventListener('click', function() {
            if (seat.classList.contains('posto-selezionato')) {
                seat.classList.remove('posto-selezionato');
                total -= seatPrice;
                const seatKey = seat.dataset.fila + "-" + seat.dataset.numero;
                selectedSeats = selectedSeats.filter(item => item !== seatKey);
            } else {
                seat.classList.add('posto-selezionato');
                total += seatPrice;
                const seatKey = seat.dataset.fila + "-" + seat.dataset.numero;
                selectedSeats.push(seatKey);
            }
            updateSummary();
        });
    });

    function resetSelection() {
        document.querySelectorAll('.posto-selezionato').forEach(seat => {
            seat.classList.remove('posto-selezionato');
        });
        selectedSeats = [];
        total = 0;
        updateSummary();
    }

    checkoutButton.addEventListener('click', function(event) {
        if (selectedSeats.length === 0) {
            event.preventDefault();
            alert("Devi selezionare almeno un posto prima di procedere al checkout.");
        }
    });
</script>

</body>
</html>
