<%@ page import="it.unisa.application.model.entity.Proiezione" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>Gestione Programmazione</title>
    <style>
        body {
            background-color: #1c1c1c;
            color: #fff;
            font-family: Arial, sans-serif;
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
        .content {
            margin: 20px auto;
            max-width: 900px;
            padding: 20px;
            background-color: #2c2c2c;
            border-radius: 8px;
        }
        a {
            color: #fff;
            background-color: #ff3b30;
            padding: 8px 12px;
            border-radius: 5px;
            text-decoration: none;
            margin-bottom: 15px;
            display: inline-block;
        }
        a:hover {
            background-color: #e32a1d;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #444;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #444;
        }
        td {
            background-color: #222;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/headerSede.jsp"/>
<div class="content">
    <%
        Integer sedeId = (Integer) request.getAttribute("sedeId");
        if (sedeId == null) {
            sedeId = Integer.parseInt(request.getParameter("sedeId"));
        }
    %>
    <a href="<%= request.getContextPath() %>/aggiungiProiezione?sedeId=<%= sedeId %>">
        Aggiungi Programmazione
    </a>

    <%
        List<Proiezione> programmazioni =
                (List<it.unisa.application.model.entity.Proiezione>) request.getAttribute("programmazioni");
        if (programmazioni == null || programmazioni.isEmpty()) {
    %>
    <p>Nessuna proiezione presente dalla data odierna.</p>
    <%
    } else {
    %>
    <table>
        <thead>
        <tr>
            <th>Sala</th>
            <th>Data</th>
            <th>Ora Inizio</th>
            <th>Film</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (it.unisa.application.model.entity.Proiezione p : programmazioni) {
                if (p != null) {
        %>
        <tr>
            <td><%= p.getSalaProiezione().getNumeroSala() %></td>
            <td><%= p.getDataProiezione() %></td>
            <td><%= p.getMinOraInizioFormatted() %></td>
            <td><%= p.getFilmProiezione().getTitolo() %></td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
    <%
        }
    %>
</div>
<footer>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>
</html>
