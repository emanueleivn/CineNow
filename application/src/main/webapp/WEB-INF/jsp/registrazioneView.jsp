<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineNow - Registrazione</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #1c1c1c; color: #ffffff; margin: 0; padding: 0; }
        header { background: #121212; color: #fff; padding: 15px; display: flex; align-items: center; justify-content: space-between; }
        header img { height: 40px; }
        nav { display: flex; }
        nav a { color: #fff; margin: 0 15px; text-decoration: none; }
        footer { background: #121212; color: #fff; text-align: center; padding: 10px; position: fixed; bottom: 0; width: 100%; }
        .content { padding: 20px; max-width: 400px; margin: auto; }
        input[type="text"], input[type="password"], input[type="email"], .btn { width: 100%; padding: 10px; margin: 10px 0; border: 1px solid #333333; border-radius: 5px; box-sizing: border-box; background-color: #333333; color: #ffffff; }
        .btn { background: #ff3b30; color: #fff; text-decoration: none; text-align: center; cursor: pointer; border: none; }
    </style>
    <script src="${pageContext.request.contextPath}/static/js/validation.js"></script>
</head>
<body>

<header>
    <img src="logo.jpg" alt="CineNow Logo">
    <nav>
        <a href="<%= request.getContextPath() %>/index.jsp">Home</a>
        <a href="<%= request.getContextPath() %>/catalogo">Catalogo</a>
        <a href="<%= request.getContextPath() %>/profilo">Profilo</a>
        <a href="<%= request.getContextPath() %>/about">About</a>
        <a href="<%= request.getContextPath() %>/login">Login</a>
    </nav>
</header>

<div class="content">
    <h2>Crea un nuovo account</h2>
    <form action="<%= request.getContextPath() %>/registrazione" method="post" onsubmit="return validateFormReg()">
        <input type="text" id="nome" name="nome" placeholder="Nome" required>
        <div id="nome-hint" class="hint" style="display: none; color: red; font-size: 12px;">Nome non valido. Non deve contenere caratteri speciali come < > " ' % ; ( ) &.</div>

        <input type="text" id="cognome" name="cognome" placeholder="Cognome" required>
        <div id="cognome-hint" class="hint" style="display: none; color: red; font-size: 12px;">Cognome non valido. Non deve contenere caratteri speciali come < > " ' % ; ( ) &.</div>
        <input type="text" id="email" name="email" placeholder="Email" required>
        <div id="email-hint" class="hint" style="display: none; color: red; font-size: 12px;">Email non valida. Deve avere un formato come esempio@dominio.com.</div>
        <input type="password" id="password" name="password" placeholder="Password" required>
        <div id="password-hint" class="hint" style="display: none; color: red; font-size: 12px;">Password non valida. Non deve contenere caratteri speciali come < > " ' % ; ( ) &.</div>
        <input type="password" id="confirm-password" name="confirm-password" placeholder="Ripeti Password" required>
        <div id="confirm-password-hint" class="hint" style="display: none; color: red; font-size: 12px;">Le password non corrispondono.</div>
        <button type="submit" class="btn">Registrati</button>
    </form>
    <p>Già iscritto? <a href="<%= request.getContextPath() %>/login" style="color: #ff3b30;">Login</a>.</p>
</div>

<footer>&copy; 2023 CineNow - Tutti i diritti riservati.</footer>

</body>
</html>

