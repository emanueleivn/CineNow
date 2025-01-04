<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Pagamento</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1 class="text-center">Dati di Pagamento</h1>
    <form action="${pageContext.request.contextPath}/Checkout" method="post">
        <div class="form-group">
            <label for="nome">Nome</label>
            <input type="text" class="form-control" id="nome" name="nome" required>
        </div>
        <div class="form-group">
            <label for="cognome">Cognome</label>
            <input type="text" class="form-control" id="cognome" name="cognome" required>
        </div>
        <div class="form-group">
            <label for="numeroCarta">Numero Carta</label>
            <input type="text" class="form-control" id="numeroCarta" name="numeroCarta" maxlength="16" required>
        </div>
        <div class="form-row">
            <div class="form-group col-md-6">
                <label for="scadenza">Scadenza</label>
                <input type="month" class="form-control" id="scadenza" name="scadenza" required>
            </div>
            <div class="form-group col-md-6">
                <label for="cvv">CVV</label>
                <input type="password" class="form-control" id="cvv" name="cvv" maxlength="3" required>
            </div>
        </div>

        <!-- Dati nascosti da inviare -->
        <input type="hidden" name="proiezioneId" value="${proiezioneId}">
        <input type="hidden" name="posti" value="${posti}">
        <input type="hidden" name="totale" value="${totale}">

        <button type="submit" class="btn btn-primary btn-block">Conferma e Paga</button>
    </form>
</div>
</body>
</html>
