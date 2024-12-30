<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.application.model.entity.Prenotazione" %>
<%@ page import="java.util.List" %>
<html>
<head>
  <title>Storico Ordini</title>
</head>
<body>
<h1>Storico Ordini</h1>

<%
  List<Prenotazione> storico = (List<Prenotazione>) request.getAttribute("storico");
  if (storico != null && !storico.isEmpty()) {
%>
<table border="1">
  <thead>
  <tr>
    <th>ID Prenotazione</th>
    <th>ID Proiezione</th>
    <th>Posti</th>
  </tr>
  </thead>
  <tbody>
  <% for (Prenotazione prenotazione : storico) { %>
  <tr>
    <td><%= prenotazione.getId() %></td>
    <td><%= prenotazione.getProiezione().getId() %></td>
    <td>
      <% for (int i = 0; i < prenotazione.getPostiPrenotazione().size(); i++) { %>
      <%= prenotazione.getPostiPrenotazione().get(i).getPosto().getFila() %>
      <%= prenotazione.getPostiPrenotazione().get(i).getPosto().getNumero() %>
      <% if (i < prenotazione.getPostiPrenotazione().size() - 1) { %>, <% } %>
      <% } %>
    </td>
  </tr>
  <% } %>
  </tbody>
</table>
<%
} else {
%>
<p>Nessuna prenotazione trovata.</p>
<% } %>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color:red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>
</body>
</html>
