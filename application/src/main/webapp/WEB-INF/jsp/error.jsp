<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Errore</title>
</head>
<body>
<h1>Si è verificato un errore</h1>
<p><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "Errore sconosciuto." %></p>
<a href="<%= request.getContextPath() %>/index.jsp">Torna alla Home</a>
</body>

<footer>
  <jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</footer>
</html>
