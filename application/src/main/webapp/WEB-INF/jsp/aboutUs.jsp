<%@ include file="/WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - CineNow</title>
    <link rel="stylesheet" href="static/css/styles.css">
</head>
<body>
<div class="container my-5">
    <div class="text-center my-5">
        <h1>Chi siamo</h1>
        <p class="lead">Scopri di pi&ugrave; su CineNow, il futuro della prenotazione cinematografica.</p>
    </div>

    <div class="row my-5">
        <div class="col-md-6">
            <img src="static/images/logo.png" alt="Cinema" class="img-fluid rounded shadow-sm">
        </div>
        <div class="col-md-6 d-flex align-items-center">
            <div>
                <h2>La nostra missione</h2>
                <p>CineNow &egrave; nato con l'obiettivo di rivoluzionare il modo in cui vivi il cinema. Attraverso il nostro sistema innovativo, puoi prenotare i tuoi biglietti in modo rapido, scegliere i posti che preferisci e goderti l'esperienza del grande schermo senza stress.</p>
                <p>Collaboriamo con la catena di cinema Movieplex per offrire un servizio di qualit&agrave;, mettendo a tua disposizione sale moderne, tecnologia all'avanguardia e una vasta programmazione.</p>
            </div>
        </div>
    </div>

    <div class="text-center my-5">
        <h2>Perch&eacute; scegliere CineNow?</h2>
        <div class="row text-center my-4">
            <div class="col-md-4">
                <div class="card shadow-sm p-4">
                    <h5 class="card-title">Esperienza User-Friendly</h5>
                    <p class="card-text">Prenota con facilit&agrave; dal nostro sito intuitivo e veloce.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm p-4">
                    <h5 class="card-title">Ampia Scelta di Film</h5>
                    <p class="card-text">Dai blockbuster alle pellicole d'autore, abbiamo tutto ci&ograve; che cerchi.</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-sm p-4">
                    <h5 class="card-title">Comodit&agrave; Garantita</h5>
                    <p class="card-text">Scegli i posti migliori e assicurati una serata perfetta al cinema.</p>
                </div>
            </div>
        </div>
    </div>

    <div class="text-center my-5">
        <h2>Le sedi Movieplex</h2>
        <p>Con CineNow puoi scegliere la tua sala preferita tra le diverse sedi Movieplex disponibili in tutta Italia.</p>
    </div>
</div>

<footer>
    <%@ include file="/WEB-INF/jsp/footer.jsp" %>
</footer>
</body>
</html>
