<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="it.unisa.application.model.entity.Utente"%>
<html>
<head>
  <title>Errore</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/style/style.css">
  <link href="https://fonts.googleapis.com/css2?family=Baloo+Bhai+2&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
</head>
<body>
<div class="container my-5">
  <div class="card bg-light text-center">
    <div class="card-body">
      <h1 class="card-title">Si è verificato un errore</h1>
      <p class="card-text">
        <%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "Errore sconosciuto." %>
      </p>
      <%
        HttpSession sessione = request.getSession(false);
        Utente utente = (sessione != null) ? (Utente) sessione.getAttribute("gestoreSede") : null;

        if(utente != null){
      %>
        <a href="<%= request.getContextPath() %>/areaGestoreSede.jsp" class="btn btn-primary mt-3">Torna alla Home</a>
      <%
        }
        else if(((sessione != null) ? (Utente) sessione.getAttribute("gestoreCatena") : null) != null){
      %>
        <a href="<%= request.getContextPath() %>/areaGestoreCatena.jsp" class="btn btn-primary mt-3">Torna alla Home</a>
      <%
        }else{
      %>
        <a href="<%= request.getContextPath() %>/Home" class="btn btn-primary mt-3">Torna alla Home</a>
      <%}%>
    </div>
  </div>
</div>
</body>
</html>
