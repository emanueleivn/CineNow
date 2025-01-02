<%@ page import="it.unisa.application.model.entity.Cliente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow - Home</title>
  <!-- Collegamento al file CSS esterno -->
  <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/static/style/style.css">
  <script>
    document.addEventListener("DOMContentLoaded", function() {
      const catalogoLink = document.querySelector(".catalogo > a");
      const catalogoDropdown = document.querySelector(".catalogo");

      catalogoLink.addEventListener("click", function(event) {
        event.preventDefault();
        catalogoDropdown.classList.toggle("active");
      });

      const profiloLink = document.querySelector(".profilo > a");
      const profiloDropdown = document.querySelector(".profilo");

      profiloLink.addEventListener("click", function(event) {
        event.preventDefault();
        profiloDropdown.classList.toggle("active");
      });

      document.addEventListener("click", function(event) {
        if (!catalogoDropdown.contains(event.target) && catalogoDropdown.classList.contains("active")) {
          catalogoDropdown.classList.remove("active");
        }
        if (!profiloDropdown.contains(event.target) && profiloDropdown.classList.contains("active")) {
          profiloDropdown.classList.remove("active");
        }
      });
    });
  </script>
</head>
<body>
<%
  HttpSession session1 = request.getSession(false);
  Cliente clienteLoggato = (session1 != null) ? (Cliente) session1.getAttribute("cliente") : null;
%>

<header>
  <img src="static/images/logo.png" alt="CineNow Logo">
  <nav>
    <a href="<%= request.getContextPath() %>/Home">Home</a>
    <div class="catalogo">
      <a href="#">Catalogo</a>
      <div class="dropdown">
        <a href="<%= request.getContextPath() %>/catalogo/mercogliano">Moviplex Mercogliano</a>
        <a href="<%= request.getContextPath() %>/catalogo/aquila">Moviplex L'Aquila</a>
      </div>
    </div>
    <div class="profilo">
      <a href="#">Profilo</a>
      <div class="dropdown">
        <% if (clienteLoggato == null) { %>
        <a href="<%= request.getContextPath() %>/login">Login</a>
        <% } else { %>
        <a href="<%= request.getContextPath() %>/storicoOrdini">Storico Ordini</a>
        <a href="<%= request.getContextPath() %>/logout">Logout</a>
        <% } %>
      </div>
    </div>
    <a href="<%= request.getContextPath() %>/about">About</a>
  </nav>
</header>

<div class="content">
  <h1>Benvenuto su CineNow</h1>
  <div class="movie-section">
    <div class="movie">
      <img src="" alt="Locandina Film">
      <p>Titolo Film 1</p>
    </div>
    <div class="movie">
      <img src="logo.jpg" alt="Locandina Film">
      <p>Titolo Film 2</p>
    </div>
    <div class="movie">
      <img src="logo.jpg" alt="Locandina Film">
      <p>Titolo Film 3</p>
    </div>
    <div class="movie">
      <img src="logo.jpg" alt="Locandina Film">
      <p>Titolo Film 4</p>
    </div>
  </div>
  <a href="<%= request.getContextPath() %>/catalogo" class="btn">Sfoglia Catalogo</a>
</div>

<footer>&copy; 2023 CineNow - Tutti i diritti riservati.</footer>

</body>
</html>
