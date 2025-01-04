<%@ page import="it.unisa.application.model.entity.Film" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Catalogo Film</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}static/style/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container mt-5">
    <h1 class="text-center mb-4">Catalogo - <%= request.getAttribute("sede") %></h1>
    <div class="row">
        <%
            List<Film> catalogo = (List<Film>) request.getAttribute("catalogo");
            if (catalogo != null && !catalogo.isEmpty()) {
                for (Film film : catalogo) {
                    String locandinaBase64 = null;
                    if (film.getLocandina() != null) {
                        locandinaBase64 = Base64.getEncoder().encodeToString(film.getLocandina());
                    }
        %>
        <div class="col-md-4 mb-4">
            <form action="${pageContext.request.contextPath}/DettagliFilm" method="post">
                <input type="hidden" name="filmId" value="<%= film.getId() %>">
                <input type="hidden" name="sedeId" value="<%= request.getAttribute("sedeId") %>">
                <button type="submit" class="film-button" style="border: none;">
                    <div class="film-card">
                        <% if (locandinaBase64 != null) { %>
                        <img src="data:image/jpeg;base64,<%= locandinaBase64 %>"
                             alt="Locandina di <%= film.getTitolo() %>" class="img-fluid">
                        <% } else { %>
                        <img src="${pageContext.request.contextPath}/static/images/logo.png"
                             alt="Locandina non disponibile" class="img-fluid">
                        <% } %>
                        <div class="film-card-body">
                            <h5 class="film-title"><%= film.getTitolo() %></h5>
                        </div>
                    </div>
                </button>
            </form>
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

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
