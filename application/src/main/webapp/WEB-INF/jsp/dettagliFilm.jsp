<jsp:useBean id="film" scope="request" type="it.unisa.application.model.entity.Film"/>
<%@ page import="java.util.Base64" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dettagli Film</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>static/style/style.css">
</head>
<body>

<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container my-5">
    <div class="row">
        <div class="col-md-4">
            <%
                String locandinaBase64 = null;
                if (film.getLocandina() != null) {
                    locandinaBase64 = Base64.getEncoder().encodeToString(film.getLocandina());
                }
            %>
            <% if (locandinaBase64 != null) { %>
            <img src="data:image/jpeg;base64,<%= locandinaBase64 %>"
                 alt="Locandina di <%= film.getTitolo() %>" class="img-fluid rounded shadow">
            <% } else { %>
            <img src="${pageContext.request.contextPath}/static/images/logo.png"
                 alt="Locandina non disponibile" class="img-fluid rounded shadow">
            <% } %>
        </div>
        <div class="col-md-8">
            <h1 class="text-danger"><%= film.getTitolo() %></h1>
            <p class="mt-4"><%= film.getDescrizione() %></p>
            <ul class="list-unstyled">
                <li style="letter-spacing: 1px;"><strong>Durata:</strong> <%= film.getDurata() %> minuti</li>
                <li style="letter-spacing: 1px"><strong>Genere:</strong> <%= film.getGenere() %></li>
                <li style="letter-spacing: 1px"><strong>Classificazione:</strong> <%= film.getClassificazione() %></li>
                <li>
                    <form method="post" action="${pageContext.request.contextPath}/ProiezioniFilm">
                        <input type="hidden" name="filmId" value="<%= film.getId() %>">
                        <input type="hidden" name="sedeId" value="<%= request.getAttribute("sedeId") %>">
                        <button type="submit" class="btn btn-primary">Prenota</button>
                    </form>
                </li>
            </ul>
        </div>
    </div>
</div>
<footer>
    <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>

</body>
</html>
