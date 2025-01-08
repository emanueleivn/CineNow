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
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #1c1c1c;
      color: #fff;
      margin: 0;
      padding: 0;
    }
    header {
      background-color: #121212;
      padding: 15px;
      text-align: center;
    }
    h1 {
      margin: 0;
    }
    form {
      margin: 20px auto;
      padding: 20px;
      max-width: 800px;
      background-color: #2c2c2c;
      border-radius: 10px;
    }
    form div {
      margin-bottom: 15px;
    }
    label {
      display: block;
      margin-bottom: 5px;
      font-size: 14px;
    }
    select,
    input[type="date"],
    button {
      width: 100%;
      padding: 10px;
      font-size: 14px;
      border: 1px solid #444;
      border-radius: 5px;
      background-color: #444;
      color: #fff;
    }
    button {
      background-color: #ff3b30;
      border: none;
      cursor: pointer;
    }
    button:hover {
      background-color: #e32a1d;
    }
    #calendar-container {
      margin-top: 20px;
      padding: 10px;
      background-color: #333;
      border-radius: 5px;
      overflow-x: auto;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      min-width: 700px;
    }
    th,
    td {
      padding: 10px;
      border: 1px solid #555;
      text-align: center;
    }
    th {
      background-color: #444;
    }
    td {
      background-color: #222;
    }
    td.slot-available {
      cursor: pointer;
      background-color: #2ecc71;
    }
    td.slot-unavailable {
      background-color: #e74c3c;
      cursor: not-allowed;
    }
    td.slot-selected {
      background-color: #3498db;
    }
    td:hover.slot-available {
      background-color: #27ae60;
    }
    .error-msg {
      color: #ff3b30;
      margin-top: 10px;
      font-weight: bold;
    }
  </style>
  <script>
    $(document).ready(function () {
      let durataFilm = 0;

      function caricaCalendario() {
        $(".error-msg").remove();
        $("#calendar-container").html("");
        const filmId = $("#film").val();
        const dataInizio = $("#dataInizio").val();
        const dataFine = $("#dataFine").val();
        const salaId = $("#sala").val();

        if (filmId && dataInizio && dataFine && salaId) {
          const oggi = new Date().toISOString().split('T')[0];
          if (dataInizio < oggi) {
            $("#calendar-container").html("<p class='error-msg'>La data di inizio non può essere precedente ad oggi.</p>");
            return;
          }
          if (dataInizio > dataFine) {
            $("#calendar-container").html("<p class='error-msg'>La data di inizio non può essere successiva alla data di fine.</p>");
            return;
          }

          $.ajax({
            url: "<%= request.getContextPath() %>/slotDisponibili",
            method: 'GET',
            dataType: 'json',
            data: { filmId, salaId, dataInizio, dataFine },
            success: function (resp) {
              durataFilm = resp.durataFilm || 0;

              if (resp.calendar && resp.calendar.length > 0) {
                const orariSet = new Set();
                resp.calendar.forEach(g => {
                  g.slots.forEach(s => orariSet.add(s.oraInizio));
                });
                const orariArray = Array.from(orariSet).sort();

                let table = "<table><thead><tr><th>Orario</th>";
                resp.calendar.forEach(g => {
                  table += "<th>" + g.data + "</th>";
                });
                table += "</tr></thead><tbody>";

                orariArray.forEach(ora => {
                  table += "<tr><td><b>" + ora + "</b></td>";
                  resp.calendar.forEach(g => {
                    const found = g.slots.find(x => x.oraInizio === ora);
                    if (!found) {
                      table += "<td class='slot-unavailable'>--</td>";
                    } else if (found.occupato) {
                      table += "<td class='slot-unavailable'>" + found.film + "</td>";
                    } else {
                      table +=
                              "<td class='slot-available' data-id='" + found.id + "' data-day='" + g.data + "' data-orainizio='" + found.oraInizio + "'>" +
                              "Disponibile</td>";
                    }
                  });
                  table += "</tr>";
                });
                table += "</tbody></table>";
                $("#calendar-container").html(table);

                $(".slot-available").click(function () {
                  $(".slot-selected").removeClass("slot-selected");
                  const blocchi = Math.ceil(durataFilm / 30);
                  const startRow = $(this).closest("tr").index();
                  const colIndex = $(this).index();
                  const $tbody = $(this).closest("tbody");

                  let validSelection = true;
                  let slotsUsed = 0;

                  for (let i = 0; i < blocchi; i++) {
                    const $row = $tbody.find("tr").eq(startRow + i);
                    const $slot = $row.find("td").eq(colIndex);

                    // Ignora gli slot oltre l'orario massimo (es. 22:00)
                    if ($slot.length === 0) {
                      slotsUsed++;
                      continue;
                    }


                    if ($slot.hasClass("slot-unavailable")) {
                      validSelection = false;
                      break;
                    }

                    $slot.addClass("slot-selected");
                    slotsUsed++;
                  }

                  $(".error-msg").remove();
                  if (!validSelection || slotsUsed < blocchi) {
                    $("#calendar-container").after("<p class='error-msg'>Non ci sono abbastanza slot disponibili per questa proiezione.</p>");
                    $(".slot-selected").removeClass("slot-selected");
                  }
                });
              } else {
                $("#calendar-container").html("<p>Nessuno slot disponibile.</p>");
              }
            },
            error: function () {
              $("#calendar-container").html("<p class='error-msg'>Errore nel caricamento del calendario.</p>");
            }
          });
        }
      }

      $("#film, #dataInizio, #dataFine, #sala").change(caricaCalendario);

      $("form").submit(function (e) {
        $("input[name='slot']").remove();
        const selectedSlots = $(".slot-selected");
        if (selectedSlots.length === 0) {
          alert("Bisogna selezionare almeno uno slot per aggiungere una proiezione.");
          e.preventDefault();
          return;
        }
        selectedSlots.each(function () {
          const slotId = $(this).data("id");
          const day = $(this).data("day");
          $("<input>").attr("type", "hidden").attr("name", "slot").val(slotId + ":" + day).appendTo("form");
        });
      });
    });
  </script>


</head>
<body>
<jsp:include page="/WEB-INF/jsp/headerSede.jsp"/>

<form action="<%= request.getContextPath() %>/aggiungiProiezione" method="post">
  <input type="hidden" name="sedeId" value="<%= request.getAttribute("sedeId") %>">

  <div>
    <label for="film">Seleziona Film:</label>
    <select id="film" name="film" required>
      <option value="">-- Seleziona --</option>
      <%
        List<Film> films = (List<Film>) request.getAttribute("films");
        for (Film film : films) {
      %>
      <option value="<%= film.getId() %>"><%= film.getTitolo() %></option>
      <% } %>
    </select>
  </div>

  <div>
    <label for="dataInizio">Data Inizio:</label>
    <input type="date" id="dataInizio" name="dataInizio" required>
  </div>

  <div>
    <label for="dataFine">Data Fine:</label>
    <input type="date" id="dataFine" name="dataFine" required>
  </div>

  <div>
    <label for="sala">Seleziona Sala:</label>
    <select id="sala" name="sala" required>
      <option value="">-- Seleziona --</option>
      <%
        List<Sala> sale = (List<Sala>) request.getAttribute("sale");
        for (Sala sala : sale) {
      %>
      <option value="<%= sala.getId() %>">Sala <%= sala.getNumeroSala() %></option>
      <% } %>
    </select>
  </div>

  <div id="calendar-container">
    <p>Seleziona un film, un intervallo di date e una sala per visualizzare il calendario.</p>
  </div>

  <button type="submit">Aggiungi Proiezione</button>
  <button type="button" onclick="window.location.href='<%= request.getContextPath() %>/gestioneProgrammazione?sedeId=<%= request.getAttribute("sedeId") %>'" style="margin-top: 5px">Annulla</button>
</form>
<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>
</html>
