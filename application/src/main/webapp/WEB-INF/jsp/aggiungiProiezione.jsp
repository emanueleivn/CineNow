<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Film" %>
<%@ page import="it.unisa.application.model.entity.Sala" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Aggiungi Proiezione</title>
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
    $(document).ready(function() {
      function caricaSlotDisponibili() {
        const filmId = $("#film").val();
        const data = $("#data").val();
        const salaId = $("#sala").val();
        if (filmId && data && salaId) {
          $.ajax({
            url: '<%= request.getContextPath() %>/slotDisponibili',
            method: 'GET',
            data: {
              filmId: filmId,
              data: data,
              salaId: salaId
            },
            success: function(resp) {
              $("#slot-container").empty();
              if (resp.length > 0) {
                resp.forEach(function(slot) {
                  $("#slot-container").append(
                          "<label>" +
                          "<input type='checkbox' name='slot' value='" + slot.id + "'>" +
                          slot.oraInizio +
                          "</label><br>"
                  );
                });
              } else {
                $("#slot-container").append("<p>Nessuno slot disponibile.</p>");
              }
            },
            error: function() {
              $("#slot-container").empty();
              $("#slot-container").append("<p>Errore nel caricamento degli slot disponibili.</p>");
            }
          });
        }
      }

      $("#film, #data, #sala").change(caricaSlotDisponibili);
    });
  </script>
</head>
<body>
<h1>Aggiungi Proiezione</h1>
<form action="<%= request.getContextPath() %>/aggiungiProiezione" method="post">
  <input type="hidden" name="sedeId" value="<%= request.getAttribute("sedeId") %>">
  <div>
    <label for="film">Seleziona Film:</label>
    <select name="film" id="film" required>
      <%
        List<Film> films = (List<Film>) request.getAttribute("films");
        for (Film f : films) {
      %>
      <option value="<%= f.getId() %>"><%= f.getTitolo() %></option>
      <%
        }
      %>
    </select>
  </div>
  <div>
    <label for="data">Data:</label>
    <input type="date" id="data" name="dataProiezione" required>
  </div>
  <div>
    <label for="sala">Seleziona Sala:</label>
    <select name="sala" id="sala" required>
      <%
        List<Sala> sale = (List<Sala>) request.getAttribute("sale");
        for (Sala s : sale) {
      %>
      <option value="<%= s.getId() %>">Sala <%= s.getNumeroSala() %></option>
      <%
        }
      %>
    </select>
  </div>
  <div id="slot-container">
    <p>Seleziona un film, una data e una sala per vedere gli slot disponibili.</p>
  </div>
  <button type="submit">Aggiungi Proiezione</button>
</form>
</body>
</html>
