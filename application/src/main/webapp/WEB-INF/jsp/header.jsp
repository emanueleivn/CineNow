<%@ page import="it.unisa.application.model.entity.Cliente" %>
<header>
  <img src="<%= request.getContextPath() %>/static/images/logo.png" alt="CineNow Logo">
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
