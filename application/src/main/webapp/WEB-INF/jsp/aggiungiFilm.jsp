<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Aggiungi Nuovo Film</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/headerCatena.jsp"/>
<h1>Aggiungi Nuovo Film</h1>
<form action="<%= request.getContextPath() %>/addFilm" method="POST" enctype="multipart/form-data">
  <label for="titolo">Titolo:</label>
  <input type="text" id="titolo" name="titolo" required><br>
  <label for="durata">Durata (min):</label>
  <input type="number" id="durata" name="durata" required><br>
  <label for="descrizione">Descrizione:</label>
  <textarea id="descrizione" name="descrizione" required></textarea><br>
  <label for="locandina">Locandina (immagine):</label>
  <input type="file" id="locandina" name="locandina" accept="image/*" required><br>
  <label for="genere">Genere:</label>
  <input type="text" id="genere" name="genere" required><br>
  <label for="classificazione">Classificazione:</label>
  <input type="text" id="classificazione" name="classificazione" required><br>
  <button type="submit">Salva Film</button>
</form>
</body>
</html>
