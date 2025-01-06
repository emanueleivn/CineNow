<%@ include file="WEB-INF/jsp/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>HomePage - CineNow</title>
  <link rel="stylesheet" href="static/css/styles.css">
</head>
<body>
<div class="container my-5">
  <div class="text-center my-5">
    <img src="static/images/logo.png" alt="CineNow Logo" style="height: 300px;">
  </div>
  <div id="carouselExampleRide" class="carousel slide my-5" data-bs-ride="carousel">
    <div class="carousel-inner">
      <div class="carousel-item active">
        <img src="static/images/locandine/nosferatu.jpg" class="d-block w-100" alt="locandina1">
      </div>
      <div class="carousel-item">
        <img src="static/images/locandine/mufasa.jpg" class="d-block w-100" alt="locandina2">
      </div>
      <div class="carousel-item">
        <img src="static/images/locandine/doveosano.jpg" class="d-block w-100" alt="locandina3">
      </div>
    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleRide" data-bs-slide="prev">
      <span class="carousel-control-prev-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Previous</span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleRide" data-bs-slide="next">
      <span class="carousel-control-next-icon" aria-hidden="true"></span>
      <span class="visually-hidden">Next</span>
    </button>
  </div>

  <div class="row text-center my-5" id="info-section">
    <div class="col-md-4">
      <div class="card shadow-sm p-4">
        <h5 class="card-title">Prenota i tuoi biglietti</h5>
        <p class="card-text">Con CineNow puoi prenotare i tuoi posti in pochi click e senza stress, direttamente online.</p>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card shadow-sm p-4">
        <h5 class="card-title">Esclusive Movieplex</h5>
        <p class="card-text">Accedi a contenuti e promozioni riservate per vivere il cinema al massimo, solo nelle sale Movieplex.</p>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card shadow-sm p-4">
        <h5 class="card-title">Programmazione aggiornata</h5>
        <p class="card-text">Scopri tutte le novità in programmazione nelle tue sale preferite e non perdere i film più attesi.</p>
      </div>
    </div>
  </div>
</div>

<footer>
  <%@ include file="WEB-INF/jsp/footer.jsp" %>
</footer>
</body>
</html>
