<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Home</title>
</head>
<body>
<h1>Benvenuto in CineNow</h1>
<ul>
  <li><a href="<%= request.getContextPath() %>/aggiungiOrdine">Aggiungi Prenotazione</a></li>
  <li><a href="<%= request.getContextPath() %>/catalogo">Visualizza Catalogo</a></li>
  <li><a href="<%= request.getContextPath() %>/storicoOrdini">Storico Ordini</a></li>
</ul>


</body>
</html>
