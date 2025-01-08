<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="it.unisa.application.model.entity.Proiezione" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="it">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Proiezioni - <%= request.getAttribute("filmNome") %> - <%= request.getAttribute("sedeNome") %></title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style/style.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>

<div class="container mt-5">
  <h1 class="text-center mb-4">Proiezioni - <%= request.getAttribute("filmNome") %> - <%= request.getAttribute("sedeNome") %></h1>
  <div class="table-responsive">
    <%
      List<Proiezione> proiezioni = (List<Proiezione>) request.getAttribute("programmazioneFilm");
      if (proiezioni != null && !proiezioni.isEmpty()) {
    %>
    <table class="table table-striped table-bordered">
      <thead class="thead-dark">
      <tr>
        <th scope="col">Data</th>
        <th scope="col">Orario</th>
        <th scope="col">Sala</th>
        <th scope="col">Azioni</th>
      </tr>
      </thead>
      <tbody>
      <%
        for (Proiezione proiezione : proiezioni) {
          String data = proiezione.getDataProiezione().toString();
          String orario = proiezione.getOrarioProiezione().getOraInizio().toString();
          int sala = proiezione.getSalaProiezione().getNumeroSala();
      %>
      <tr>
        <td><%= data %></td>
        <td><%= orario %></td>
        <td>Sala <%= sala %></td>
        <td>
          <form action="${pageContext.request.contextPath}/SceltaPosto" method="post" class="d-inline">
            <input type="hidden" name="proiezioneId" value="<%= proiezione.getId() %>">
            <button type="submit" class="btn btn-primary">Seleziona Posto</button>
          </form>
        </td>
      </tr>
      <%
        }
      %>
      </tbody>
    </table>
    <%
    } else {
    %>
    <div class="alert alert-warning text-center" role="alert">
      Nessuna proiezione disponibile per questa settimana, torna presto...
    </div>
    <%
      }
    %>
  </div>
</div>

<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.5.4/dist/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</body>
</html>
