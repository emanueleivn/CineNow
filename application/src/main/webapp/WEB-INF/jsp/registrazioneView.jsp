<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineNow - Registrazione</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/static/style/style.css">
    <script src="${pageContext.request.contextPath}/static/js/validation.js"></script>
</head>
<body>

<header>
    <jsp:include page="/WEB-INF/jsp/header.jsp"/>
</header>

<div class="content">
    <h2>Crea un nuovo account</h2>
    <form action="<%= request.getContextPath() %>/registrazione" method="post" onsubmit="return validateFormReg()">
        <input type="text" id="nome" name="nome" placeholder="Nome" required>
        <div id="nome-hint" class="hint" style="display: none; color: red; font-size: 12px;">Nome non valido. Non deve
            contenere caratteri speciali come < > " ' % ; ( ) &.
        </div>

        <input type="text" id="cognome" name="cognome" placeholder="Cognome" required>
        <div id="cognome-hint" class="hint" style="display: none; color: red; font-size: 12px;">Cognome non valido. Non
            deve contenere caratteri speciali come < > " ' % ; ( ) &.
        </div>
        <input type="text" id="email" name="email" placeholder="Email" required>
        <div id="email-hint" class="hint" style="display: none; color: red; font-size: 12px;">Email non valida. Deve
            avere un formato come esempio@dominio.com.
        </div>
        <input type="password" id="password" name="password" placeholder="Password" required>
        <div id="password-hint" class="hint" style="display: none; color: red; font-size: 12px;">Password non valida.
            Deve essere lunga almeno 8 caratteri e contenere almeno un carattere speciale, esclusi: < > " ' % ; ( ) &.
        </div>
        <input type="password" id="confirm-password" name="confirm-password" placeholder="Ripeti Password" required>
        <div id="confirm-password-hint" class="hint" style="display: none; color: red; font-size: 12px;">Le password non
            corrispondono.
        </div>
        <button type="submit" class="btn">Registrati</button>
    </form>
    <p>Già iscritto? <a href="<%= request.getContextPath() %>/login" style="color: #ff3b30;">Login</a>.</p>
</div>

<footer><jsp:include page="/WEB-INF/jsp/footer.jsp"/></footer>

</body>
</html>

