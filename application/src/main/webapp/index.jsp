<%@ page import="it.unisa.application.model.entity.Cliente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Home</title>
  <style>
    body { text-align: center; font-family: Arial, sans-serif; }
    .welcome { margin: 20px auto; font-size: 1.5rem; color: green; }
  </style>
</head>
<body>
<%
  HttpSession session1 = request.getSession(false);
  Cliente clienteLoggato = (session1 != null) ? (Cliente) session.getAttribute("clienteLoggato") : null;
%>

<h1>Benvenuto in CineNow</h1>
<% if (clienteLoggato != null) { %>
<div class="welcome">Ciao <%= clienteLoggato.getNome() %> <%= clienteLoggato.getCognome() %>, sei loggato!</div>
<% } %>
<ul>
  <li><a href="<%= request.getContextPath() %>/aggiungiOrdine">Aggiungi Prenotazione</a></li>
  <li><a href="<%= request.getContextPath() %>/catalogo">Visualizza Catalogo</a></li>
  <li><a href="<%= request.getContextPath() %>/storicoOrdini">Storico Ordini</a></li>
  <% if (clienteLoggato == null) { %>
  <li><a href="<%= request.getContextPath() %>/login">Login</a></li>
  <% } else { %>
  <li><a href="<%= request.getContextPath() %>/logout">Logout</a></li>
  <% } %>
</ul>

</body>
</html>

