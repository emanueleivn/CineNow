<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow - Aggiungi Nuovo Film</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/static/style/style.css">
</head>
<body>

<header>
  <jsp:include page="/WEB-INF/jsp/headerCatena.jsp"/>
</header>

<div class="content">
  <h2>Aggiungi Nuovo Film</h2>
  <form action="<%= request.getContextPath() %>/addFilm" method="POST" enctype="multipart/form-data">
    <input type="text" id="titolo" name="titolo" placeholder="Titolo" required>
    <input type="number" id="durata" name="durata" placeholder="Durata (min)" required>
    <textarea id="descrizione" name="descrizione" placeholder="Descrizione" required></textarea>
    <input type="file" id="locandina" name="locandina" accept="image/*" required>
    <select id="genere" name="genere" required>
      <option value="" disabled selected>Seleziona un genere</option>
      <option value="Animazione">Animazione</option>
      <option value="Avventura">Avventura</option>
      <option value="Azione">Azione</option>
      <option value="Biografico">Biografico</option>
      <option value="Commedia">Commedia</option>
      <option value="Documentario">Documentario</option>
      <option value="Drammatico">Drammatico</option>
      <option value="Erotico">Erotico</option>
      <option value="Fantascienza">Fantascienza</option>
      <option value="Fantasy/Fantastico">Fantasy/Fantastico</option>
      <option value="Guerra">Guerra</option>
      <option value="Horror">Horror</option>
      <option value="Musical">Musical</option>
      <option value="Storico">Storico</option>
      <option value="Thriller">Thriller</option>
      <option value="Western">Western</option>
    </select>
    <select id="classificazione" name="classificazione" required>
      <option value="" disabled selected>Seleziona una classificazione</option>
      <option value="T">T</option>
      <option value="6+">6+</option>
      <option value="14+">14+</option>
      <option value="18+">18+</option>
    </select>
    <button type="submit" class="btn">Salva Film</button>
  </form>
</div>

<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>

</body>
</html>
