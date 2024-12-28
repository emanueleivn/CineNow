<%@ page import="it.unisa.application.model.entity.Prenotazione" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Storico Ordini</title>
</head>
<body>
<h1>Storico Ordini</h1>
<table border="1">
  <thead>
  <tr>
    <th>ID Prenotazione</th>
    <th>ID Proiezione</th>
  </tr>
  </thead>
  <tbody>
  <%
    List<Prenotazione> storico = (List<Prenotazione>) request.getAttribute("storico");
    if (storico != null) {
      for (Prenotazione prenotazione : storico) {
  %>
  <tr>
    <td><%= prenotazione.getId() %></td>
    <td><%= prenotazione.getIdProiezione() %></td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="2">Nessuna prenotazione trovata.</td>
  </tr>
  <%
    }
  %>
  </tbody>

</table>
<a href="<%= request.getContextPath() %>/index.jsp">Torna alla Home</a>
</body>
</html>
