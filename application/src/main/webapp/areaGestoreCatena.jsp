<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="it">
<head>
  <title>Area Gestore Catena</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/style/style.css">
  <link href="https://fonts.googleapis.com/css2?family=Baloo+Bhai+2&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/headerCatena.jsp"/>

<style>
  h1{
    text-align: center;
    margin: 70px 0px 70px 0px;
  }
</style>

<h1>Benvenuto, Gestore della Catena</h1>
<div class="container my-5">
  <div class="text-center my-5">
    <img src="static/images/logo.png" alt="CineNow Logo" style="height: 300px;">
  </div>
</div>
<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</body>

</html>
