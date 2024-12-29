<%@ page import="it.unisa.application.model.entity.Film" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Catalogo Film</title>
</head>
<body>
<h1>Catalogo Film</h1>
<table border="1">
  <thead>
  <tr>
    <th>Titolo</th>
    <th>Genere</th>
    <th>Durata</th>
  </tr>
  </thead>
  <tbody>
  <%
    List<Film> catalogo = (List<Film>) request.getAttribute("catalogo");
    if (catalogo != null) {
      for (Film film : catalogo) {
  %>
  <tr>
    <td><%= film.getTitolo() %></td>
    <td><%= film.getGenere() %></td>
    <td><%= film.getDurata() %></td>
  </tr>
  <%
    }
  } else {
  %>
  <tr>
    <td colspan="3">Nessun film trovato.</td>
  </tr>
  <%
    }
  %>
  </tbody>

</table>
<a href="<%= request.getContextPath() %>/index.jsp">Torna alla Home</a>
</body>
</html>
