<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.application.model.entity.Prenotazione" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.PostoProiezione" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow - Storico Ordini Cliente</title>
</head>
<body>
<div class="container my-5">
  <h1 class="text-center my-5">Storico Ordini di <%= ((it.unisa.application.model.entity.Cliente) request.getSession().getAttribute("cliente")).getNome() %></h1>
  <p class="text-center">Benvenuto, qui puoi visualizzare lo storico dei tuoi ordini su CineNow.</p>
  <ul class="list-group my-5">
    <%
      List<Prenotazione> storico = (List<Prenotazione>) request.getAttribute("storico");
      if (storico != null && !storico.isEmpty()) {
        for (Prenotazione prenotazione : storico) {
    %>
    <li class="list-group-item bg-dark text-white mb-3">
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
      <p><strong>Sede:</strong>
        <%= prenotazione.getProiezione().getSalaProiezione().getSede() != null && prenotazione.getProiezione().getSalaProiezione().getSede() != null
                ?  prenotazione.getProiezione().getSalaProiezione().getSede().getNome()
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
        Nessun posto prenotato.
        <%
          }
        %>
      </p>
    </li>
    <%
      }
    } else {
    %>
    <div class="card bg-light text-center my-5">
      <div class="card-body">
        <h5 class="card-title">Nessun ordine trovato</h5>
        <p class="card-text">Non hai effettuato alcuna prenotazione. Visita il nostro catalogo per prenotare il tuo prossimo film!</p>
      </div>
    </div>
    <% } %>
  </ul>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>
