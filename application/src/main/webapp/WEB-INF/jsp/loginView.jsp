
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineNow - Login</title>
    <script src="${pageContext.request.contextPath}/static/js/validation.js" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style/style.css">
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

    </style>
</head>
<body>

<header>

    <jsp:include page="/WEB-INF/jsp/header.jsp"/>

</header>

<div class="content">
    <h2>Accedi al tuo account</h2>
    <div id="error-message" class="error-message">
        Email o password non corretti.
    </div>
    <form action="<%= request.getContextPath() %>/login" method="post" onsubmit="return validateForm()">
        <input type="text" id="email" name="email" placeholder="Email" autofocus required>
        <input type="password" id="password" name="password" placeholder="Password" required>
        <button type="submit" class="btn">Accedi</button>
    </form>
    <p>Non sei registrato? <a href="<%=request.getContextPath() %>/registrazione" style="color: #ff3b30;">Iscriviti
        ora</a>.</p>
</div>

<footer>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>
</html>
