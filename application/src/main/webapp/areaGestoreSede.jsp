<%@ page import="it.unisa.application.model.entity.GestoreSede" %>
<%@ page import="it.unisa.application.model.entity.Sede" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Area Gestore Sede</title>
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
      justify-content: space-between;
    }
    header img {
      height: 100px;
      margin-right: 15px;
    }
    nav {
      display: flex;
      align-items: center;
    }
    nav a {
      color: #fff;
      margin: 0 15px;
      text-decoration: none;
    }
    .content {
      padding: 20px;
      text-align: center;
    }
    .btn {
      padding: 10px 20px;
      background: #ff3b30;
      color: #fff;
      text-decoration: none;
      border-radius: 5px;
      display: inline-block;
      margin-top: 20px;
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
  </style>
</head>
<body>
<%
  HttpSession session1 = request.getSession(false);
  GestoreSede gestore = (session1 != null) ? (GestoreSede) session1.getAttribute("gestoreSede") : null;
%>

<header>
  <img src="static/images/logo.png" alt="CineNow Logo">
  <nav>
    <a href="<%= request.getContextPath() %>/logout">Logout</a>
    <%
      if (gestore != null && gestore.getSede() != null) {
    %>
    <a href="<%= request.getContextPath() %>/gestioneProgrammazione?sedeId=<%= gestore.getSede().getId() %>">Gestisci Programmazione</a>
    <%
      }
    %>
  </nav>
</header>

<div class="content">
  <h1>Area Gestore Sede</h1>
  <%
    if (gestore != null) {
  %>
  <p>Benvenuto, <%= gestore.getEmail() %>!</p>
  <p>Ruolo: <%= gestore.getRuolo() %></p>
  <%
    if (gestore.getSede() != null) {
  %>
  <p>Sede associata: <%= gestore.getSede().getNome() %></p>
  <%
  } else {
  %>
  <p>Errore: nessuna sede associata.</p>
  <%
    }
  } else {
  %>
  <p>Errore: non risulti autenticato come Gestore di sede.</p>
  <%
    }
  %>
</div>

<footer>&copy; 2023 CineNow - Tutti i diritti riservati.</footer>
</body>
</html>
