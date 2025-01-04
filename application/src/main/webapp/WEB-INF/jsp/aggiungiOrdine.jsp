<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Aggiungi Prenotazione</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<h1>Aggiungi Prenotazione</h1>

<form action="<%= request.getContextPath() %>/aggiungiOrdine" method="post">
  <label for="idProiezione">ID Proiezione:</label>
  <input type="text" id="idProiezione" name="idProiezione" required><br><br>

  <label for="posti">Posti (formato: idSala,fila,numero; esempio: 1,A,5):</label>
  <input type="text" id="posti" name="posti" required><br><br>

  <button type="submit">Aggiungi Prenotazione</button>
</form>

<% if (request.getAttribute("errorMessage") != null) { %>
<p style="color:red;"><%= request.getAttribute("errorMessage") %></p>
<% } %>
<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>
</html>
