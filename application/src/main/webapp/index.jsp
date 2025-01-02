<%@ page import="it.unisa.application.model.entity.Cliente" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow - Home</title>
  <style>
    body { font-family: Arial, sans-serif; background-color: #1c1c1c; color: #ffffff; margin: 0; padding: 0; }
    header { background: #121212; color: #fff; padding: 15px; display: flex; align-items: center; justify-content: space-between; }
    header img { height: 150px; margin-right: 15px; }
    nav { display: flex; align-items: center; position: relative; }
    nav a { color: #fff; margin: 0 15px; text-decoration: none; position: relative; }
    .dropdown { position: absolute; display: none; background-color: #333333; border-radius: 5px; top: 45px; left: 0; z-index: 1000; }
    .dropdown a { color: #ffffff; padding: 10px 20px; display: block; text-decoration: none; }
    .dropdown a:hover { background-color: #444444; }
    .profilo.active .dropdown, .catalogo.active .dropdown { display: block; }
    footer { background: #121212; color: #fff; text-align: center; padding: 10px; position: fixed; bottom: 0; width: 100%; }
    .content { padding: 20px; text-align: center; }
    .movie-section { display: flex; justify-content: space-around; flex-wrap: wrap; margin-top: 20px; }
    .movie { width: 150px; margin: 15px; text-align: center; }
    .movie img { width: 100px; height: 100px; margin-bottom: 10px; border-radius: 5px; }
    .btn { padding: 10px 20px; background: #ff3b30; color: #fff; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 20px; }
  </style>
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
