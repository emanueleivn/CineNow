<%@ page import="it.unisa.application.model.entity.Film" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo Film</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}static/style/style.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />
<div class="container mt-5">
    <h1 class="text-center mb-4">Catalogo - <%=request.getAttribute("sede")%></h1>
    <div class="row">
        <%
            List<it.unisa.application.model.entity.Film> catalogo =
                    (List<Film>) request.getAttribute("catalogo");
            if (catalogo != null && !catalogo.isEmpty()) {
                for (it.unisa.application.model.entity.Film film : catalogo) {
        %>
        <div class="col-md-4 mb-4">
            <div class="film-card">
                <img src="${pageContext.request.contextPath}/static/images/locandine/maria.jpg" alt="Locandina di <%= film.getTitolo() %>" class="img-fluid">
                <div class="film-card-body">
                    <h5 class="film-title"><%= film.getTitolo() %></h5>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="col-12 text-center">
            <p class="text-muted">Nessun film disponibile per la sede selezionata.</p>
        </div>
        <% } %>
    </div>
</div>
<jsp:include page="/WEB-INF/jsp/footer.jsp" />
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
