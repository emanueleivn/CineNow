<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CineNow - Login</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #1c1c1c;
            color: #ffffff;
            margin: 0;
            padding: 0;
        }

        header {
            background: #121212;
            color: #fff;
            padding: 15px;
            display: flex;
            align-items: center;
        }

        header img {
            height: 40px;
            margin-right: 15px;
        }

        nav a {
            color: #fff;
            margin: 0 15px;
            text-decoration: none;
        }

        footer {
            background: #121212;
            color: #fff;
            text-align: center;
            padding: 10px;
            position: fixed;
            bottom: 0;
            width: 100%;
        }

        .content {
            padding: 20px;
            max-width: 400px;
            margin: auto;
        }

        input[type="text"], input[type="password"], .btn {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #333333;
            border-radius: 5px;
            box-sizing: border-box;
            background-color: #333333;
            color: #ffffff;
        }

        .btn {
            background: #ff3b30;
            color: #fff;
            text-decoration: none;
            text-align: center;
            cursor: pointer;
            border: none;
        }
    </style>
    <script src="${pageContext.request.contextPath}/static/js/validation.js"></script>
</head>
<body>

<header>
    <img src="<%= request.getContextPath() %>/images/logo.jpg" alt="CineNow Logo">
    <nav>
        <a href="<%= request.getContextPath() %>/">Home</a>
        <a href="<%= request.getContextPath() %>/catalogo">Catalogo</a>
        <a href="<%= request.getContextPath() %>/profilo">Profilo</a>
        <a href="<%= request.getContextPath() %>/about">About</a>
    </nav>
</header>

<div class="content">
    <h2>Accedi al tuo account</h2>
    <div id="error-message" class="error-message" style="display: none;">
        Email o password non corretti.
    </div>
    <form action="<%= request.getContextPath() %>/login" method="post" onsubmit="return validateForm()">
        <input type="text" name="email" placeholder="Email" autofocus required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit" class="btn">Accedi</button>
    </form>
    <p>Non sei registrato? <a href="<%=request.getContextPath() %>/registrazione" style="color: #ff3b30;">Iscriviti
        ora</a>.</p>
</div>

<footer>&copy; 2023 CineNow - Tutti i diritti riservati.</footer>
</body>
</html>

