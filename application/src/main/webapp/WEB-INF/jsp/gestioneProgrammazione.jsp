<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Proiezione" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Programmazione</title>
</head>
<body>
<div class="content">
    <h1>Gestione Programmazione</h1>
    <a href="<%= request.getContextPath() %>/aggiungiProiezione?sedeId=<%= request.getParameter("sedeId") %>">Aggiungi Proiezione</a>
    <table>
        <thead>
        <tr>
            <th>Sala</th>
            <th>Data</th>
            <th>Orario</th>
            <th>Titolo Film</th>
        </tr>
        </thead>
        <tbody>
        <%
            List<Proiezione> programmazioni = (List<Proiezione>) request.getAttribute("programmazioni");
            for (Proiezione p : programmazioni) {
        %>
        <tr>
            <td><%= p.getSalaProiezione().getNumeroSala() %></td> <!-- Mostra il numero della sala -->
            <td><%= p.getDataProiezione() %></td>
            <td><%= p.getOrarioProiezione().getOraInizio() %></td>
            <td><%= p.getFilmProiezione().getTitolo() %></td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>
</body>
</html>
