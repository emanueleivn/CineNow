<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Aggiungi Prenotazione</title>
</head>
<body>
<h1>Aggiungi Prenotazione</h1>
<form action="<%= request.getContextPath() %>/aggiungiOrdine" method="post">
  <label for="emailCliente">Email Cliente:</label>
  <input type="email" id="emailCliente" name="emailCliente" required><br>

  <label for="idProiezione">ID Proiezione:</label>
  <input type="number" id="idProiezione" name="idProiezione" required><br>

  <label for="posti">Posti (Formato: idSala,fila,numero separati da punto e virgola):</label>
  <input type="text" id="posti" name="posti" placeholder="Esempio: 1,A,1;1,A,2" required><br>

  <button type="submit">Prenota</button>
</form>
</body>
</html>
