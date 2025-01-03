<jsp:useBean id="film" scope="request" type="it.unisa.application.model.entity.Film"/>
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
            <!--<img src="${film.locandina}" alt="Locandina ${film.titolo}" class="img-fluid rounded shadow">-->
            <img src="${pageContext.request.contextPath}/static/images/locandine/maria.jpg"
                 alt="Locandina ${film.titolo}" class="img-fluid rounded shadow">

        </div>
        <div class="col-md-8">
            <h1 class="text-danger">${film.titolo}</h1>
            <p class="mt-4">${film.descrizione}</p>
            <ul class="list-unstyled">
                <li><strong>Durata:</strong> ${film.durata} minuti</li>
                <li><strong>Genere:</strong> ${film.genere}</li>
                <li><strong>Classificazione:</strong> ${film.classificazione}</li>

                <li>
                    <form method="post" action="${pageContext.request.contextPath}/Proiezioni">
                    <button type="submit">Prenota</button>
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
