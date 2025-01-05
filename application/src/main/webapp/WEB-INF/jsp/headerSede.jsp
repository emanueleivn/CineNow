<%@ page import="it.unisa.application.model.entity.Cliente" %>
<%@ page import="it.unisa.application.model.entity.GestoreSede" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>CineNow</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="static/style/style.css">
  <link href="https://fonts.googleapis.com/css2?family=Baloo+Bhai+2&display=swap" rel="stylesheet">
  <link href="https://fonts.googleapis.com/css2?family=Luckiest+Guy&display=swap" rel="stylesheet">
</head>
<body>
<header>
  <div class="top-bar d-flex align-items-center justify-content-start bg-light py-2 px-3">
  </div>
  <%
    HttpSession session1 = request.getSession(false);
    GestoreSede gestore = (session1 != null) ? (GestoreSede) session1.getAttribute("gestoreSede") : null;
  %>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="background-color: #000000 !important;
    border: none;
}">
    <div class="container-fluid">
            <a href="<%= request.getContextPath() %>/areaGestoreSede.jsp" class="nav-link navbar-brand text-danger fw-bold ms-3"><img src="static/images/scritta%20sito.png"
                                                                     alt="CineNow" class="ms-3"
                                                                     style="height: 70px;"></a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
              aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
        <ul class="navbar-nav ms-auto">
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/areaGestoreSede.jsp">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link"
               href="<%= request.getContextPath() %>/logout">Logout</a>
          </li>
          <li class="nav-item">
            <%
              if (gestore != null && gestore.getSede() != null) {
            %>
            <a class= "nav-link" href="<%= request.getContextPath() %>/gestioneProgrammazione?sedeId=<%= gestore.getSede().getId() %>">Gestisci Programmazione</a>
            <%
              }
            %></li>
        </ul>
        <span class="navbar-brand text-danger fw-bold ms-auto" style="font-family:'Baloo Bhai 2',cursive;">Catena-Movieplex</span>
      </div>
    </div>
  </nav>
</header>
</body>
</html>
