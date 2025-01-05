<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineNow - Checkout</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style/style.css">
    <script src="${pageContext.request.contextPath}/static/js/checkoutValidation.js" defer></script>
    <style>
        a {
            text-decoration: none;
        }

        a:hover {
            color: coral;
        }

        a:focus, a:active {
            color: coral;
        }

        .form-container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        .form-container h2, .form-container h3 {
            text-align: center;
            color: #333;
        }

        .form-container input {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 5px;
        }

        .form-container button {
            width: 100%;
            padding: 10px;
            background-color: #ff3b30;
            border: none;
            border-radius: 5px;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        .form-container button:hover {
            background-color: #e63328;
        }

        .error-message {
            color: red;
            font-size: 14px;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>
</header>

<div class="content">
    <div class="form-container">
        <h3>Totale: €<%= request.getParameter("totale") %></h3>
        <h2>Pagamento</h2>
        <div id="error-message" class="error-message" style="display: none;"></div>
        <form id="checkoutForm" action="${pageContext.request.contextPath}/AggiungiOrdine" method="post">
            <input type="text" id="nomeCarta" name="nomeCarta" placeholder="Nome sulla carta" required>
            <input type="text" id="numeroCarta" name="numeroCarta" placeholder="Numero della carta" maxlength="16" required>
            <input type="text" id="scadenzaCarta" name="scadenzaCarta" placeholder="Data di scadenza (MM/AA)" maxlength="5" required>
            <input type="text" id="cvv" name="cvv" placeholder="CVV" maxlength="3" required>
            <input type="hidden" name="proiezioneId" value="<%= request.getParameter("proiezioneId") %>">
            <input type="hidden" name="posti" value="<%= request.getParameter("posti") %>">
            <input type="hidden" name="totale" value="<%= request.getParameter("totale") %>">

            <button type="submit">Conferma</button>
        </form>
    </div>
</div>

<footer>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>
</html>
