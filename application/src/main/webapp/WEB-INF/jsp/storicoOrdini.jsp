<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.application.model.entity.Prenotazione" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.PostoProiezione" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow - Storico Ordini Cliente</title>
  <style>
    body { font-family: Arial, sans-serif; background-color: #1c1c1c; color: #ffffff; margin: 0; padding: 0; }
    header { background: #121212; color: #fff; padding: 15px; display: flex; align-items: center; justify-content: space-between; }
    header img { height: 40px; margin-right: 15px; }
    nav { display: flex; align-items: center; }
    nav a { color: #fff; margin: 0 15px; text-decoration: none; }
    .content { padding: 20px; max-width: 800px; margin: auto; text-align: center; }
    .order-list { list-style-type: none; padding: 0; margin-top: 20px; }
    .order-item { background-color: #222222; padding: 15px; margin-bottom: 10px; border-radius: 5px; }
    .order-item h3 { margin: 0 0 10px; font-size: 1.2em; }
    .order-item p { margin: 5px 0; font-size: 0.9em; color: #cccccc; }
    footer { background: #121212; color: #fff; text-align: center; padding: 10px; position: fixed; bottom: 0; width: 100%; }
  </style>
</head>
<body>

<header>
  <img src="<%= request.getContextPath() %>/images/logo.jpg" alt="CineNow Logo">
  <nav>
    <a href="<%= request.getContextPath() %>/index.jsp">Home</a>
    <a href="<%= request.getContextPath() %>/catalogo">Catalogo</a>
    <a href="<%= request.getContextPath() %>/profilo">Profilo</a>
    <a href="<%= request.getContextPath() %>/about">About</a>
  </nav>
</header>

<div class="content">
  <h1>Storico Ordini di <%= ((it.unisa.application.model.entity.Cliente) request.getSession().getAttribute("cliente")).getNome() %></h1>
  <p>Benvenuto, qui puoi visualizzare lo storico dei tuoi ordini su CineNow.</p>
  <ul class="order-list">
    <%
      List<Prenotazione> storico = (List<Prenotazione>) request.getAttribute("storico");
      if (storico != null && !storico.isEmpty()) {
        for (Prenotazione prenotazione : storico) {
    %>
    <li class="order-item">
      <h3>Codice Prenotazione: <%= String.format("%03d", prenotazione.getId()) %></h3>
      <p><strong>Film:</strong>
        <%= prenotazione.getProiezione() != null && prenotazione.getProiezione().getFilmProiezione() != null
                ? prenotazione.getProiezione().getFilmProiezione().getTitolo()
                : "Non disponibile" %>
      </p>
      <p><strong>Durata:</strong>
        <%= prenotazione.getProiezione() != null && prenotazione.getProiezione().getFilmProiezione() != null
                ? prenotazione.getProiezione().getFilmProiezione().getDurata() + " min"
                : "Non disponibile" %>
      </p>
      <p><strong>Sala:</strong>
        <%= prenotazione.getProiezione() != null && prenotazione.getProiezione().getSalaProiezione() != null
                ? "Sala " + prenotazione.getProiezione().getSalaProiezione().getNumeroSala()
                : "Non disponibile" %>
      </p>
      <p><strong>Data:</strong>
        <%= prenotazione.getProiezione() != null
                ? prenotazione.getProiezione().getDataProiezione()
                : "Non disponibile" %>
      </p>
      <p><strong>Orario:</strong>
        <%= prenotazione.getProiezione() != null && prenotazione.getProiezione().getOrarioProiezione() != null
                ? prenotazione.getProiezione().getOrarioProiezione().getOraInizio()
                : "Non disponibile" %>
      </p>
      <p><strong>Posti:</strong>
        <%
          if (prenotazione.getPostiPrenotazione() != null && !prenotazione.getPostiPrenotazione().isEmpty()) {
            StringBuilder posti = new StringBuilder();
            for (PostoProiezione postoProiezione : prenotazione.getPostiPrenotazione()) {
              posti.append(postoProiezione.getPosto().getFila())
                      .append(postoProiezione.getPosto().getNumero())
                      .append(", ");
            }
        %>
        <%= posti.substring(0, posti.length() - 2) %>
        <%
        } else {
        %>
        Nessun posto prenotato
        <%
          }
        %>
      </p>
    </li>
    <%
      }
    } else {
    %>
    <p>Nessuna prenotazione trovata.</p>
    <% } %>
  </ul>
</div>

<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>

</body>
</html>
