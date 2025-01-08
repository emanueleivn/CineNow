<%@ page import="it.unisa.application.model.entity.GestoreSede" %>
<%@ page import="it.unisa.application.model.entity.Sede" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <title>Area Gestore Sede</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/style/style.css">
  <link href="https://fonts.googleapis.com/css2?family=Baloo+Bhai+2&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
</head>
<body>
<%
  HttpSession session1 = request.getSession(false);
  GestoreSede gestore = (session1 != null) ? (GestoreSede) session1.getAttribute("gestoreSede") : null;
%>

<jsp:include page="/WEB-INF/jsp/headerSede.jsp"/>

<div class="content">
  <h1>Area Gestore Sede</h1>
  <%
    if (gestore != null) {
  %>
  <p>Benvenuto, <%= gestore.getEmail() %>!</p>
  <p>Ruolo: <%= gestore.getRuolo() %></p>
  <%
    if (gestore.getSede() != null) {
  %>
  <p>Sede associata: <%= gestore.getSede().getNome() %></p>
  <%
  } else {
  %>
  <p>Errore: nessuna sede associata.</p>
  <%
    }
  } else {
  %>
  <p>Errore: non risulti autenticato come Gestore di sede.</p>
  <%
    }
  %>
</div>
<div class="container my-5">
  <div class="text-center my-5">
    <img src="static/images/logo.png" alt="CineNow Logo" style="height: 300px;">
  </div>
</div>
<footer><jsp:include page="/WEB-INF/jsp/footer.jsp"/></footer>
</body>
</html>