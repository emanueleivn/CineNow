<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Film" %>
<%@ page import="java.util.Base64" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Gestione Catalogo Film</title>
</head>
<body>
<h1>Gestione Catalogo Film</h1>
<button onclick="location.href='<%= request.getContextPath() %>/addFilm'">Aggiungi Film</button>

<table border="1">
  <thead>
  <tr>
    <th>Titolo</th>
    <th>Durata</th>
    <th>Descrizione</th>
    <th>Classificazione</th>
    <th>Genere</th>
    <th>Locandina</th>
  </tr>
  </thead>
  <tbody>
  <%
    List<Film> catalogo = (List<Film>) request.getAttribute("catalogo");
    if (catalogo != null && !catalogo.isEmpty()) {
      for (Film film : catalogo) {
        String base64Image = null;
        if (film.getLocandina() != null) {
          base64Image = Base64.getEncoder().encodeToString(film.getLocandina());
        }
  %>
  <tr>
    <td><%= film.getTitolo() %></td>
    <td><%= film.getDurata() %> min</td>
    <td><%= film.getDescrizione() %></td>
    <td><%= film.getClassificazione() %></td>
    <td><%= film.getGenere() %></td>
    <td>
      <% if (base64Image != null) { %>
      <img src="data:image/jpeg;base64,<%= base64Image %>"
           alt="Locandina di <%= film.getTitolo() %>"
           width="100">
      <% } else { %>
      Nessuna locandina disponibile
      <% } %>
    </td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="6">Nessun film disponibile.</td>
  </tr>
  <%
    }
  %>
  </tbody>
</table>
</body>
</html>
