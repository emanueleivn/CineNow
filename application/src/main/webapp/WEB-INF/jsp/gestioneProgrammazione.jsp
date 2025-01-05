<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="it.unisa.application.model.entity.Proiezione" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestione Programmazione</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/headerSede.jsp"/>

<div class="container mt-5">
    <h1 class="text-center mb-4">Gestione Programmazione</h1>
    <a href="<%= request.getContextPath() %>/aggiungiProiezione?sedeId=<%= request.getParameter("sedeId") %>"
       class="btn btn-primary mb-4">Aggiungi Proiezione</a>
    <div class="table-responsive">
        <%
            List<Proiezione> programmazioni = (List<Proiezione>) request.getAttribute("programmazioni");
            if (programmazioni != null && !programmazioni.isEmpty()) {
        %>
        <table class="table table-striped table-bordered">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Sala</th>
                <th scope="col">Data</th>
                <th scope="col">Ora Inizio</th>
                <th scope="col">Film</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (Proiezione p : programmazioni) {
            %>
            <tr>
                <td>Sala <%= p.getSalaProiezione().getNumeroSala() %></td>
                <td><%= p.getDataProiezione() %></td>
                <td><%= p.getOrarioProiezione().getOraInizio() %></td>
                <td><%= p.getFilmProiezione().getTitolo() %></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>
        <%
        } else {
        %>
        <div class="alert alert-warning text-center" role="alert">
            Nessuna proiezione presente.
        </div>
        <%
            }
        %>
    </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
