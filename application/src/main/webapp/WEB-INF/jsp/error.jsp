<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Errore</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/style/style.css">
  <link href="https://fonts.googleapis.com/css2?family=Baloo+Bhai+2&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
</head>
<body>
<h1 style="text-align: center">Si è verificato un errore</h1>
<p><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "Errore sconosciuto." %></p>
<a href="<%= request.getContextPath() %>/index.jsp">Torna alla Home</a>
</body>

<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</html>
