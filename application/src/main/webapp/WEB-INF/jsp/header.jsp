<%@ page import="it.unisa.application.model.entity.Cliente" %>
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

    <nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="background-color: #000000 !important;
    border: none;
}">
        <div class="container-fluid">
            <a href="<%= request.getContextPath() %>/Home" class="nav-link navbar-brand text-danger fw-bold ms-3"><img src="static/images/scritta%20sito.png"
                                                                     alt="CineNow" class="ms-3"
                                                                     style="height: 70px;"></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav"
                    aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse justify-content-center" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/Home">Home</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="catalogoDropdown" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            Catalogo
                        </a>
                        <ul class="dropdown-menu bg-dark text-light" aria-labelledby="catalogoDropdown">
                            <li><a class="dropdown-item text-light" href="<%= request.getContextPath() %>/Catalogo?sede=Mercogliano">Sede Mercogliano</a>
                            </li>
                            <li><a class="dropdown-item text-light" href="<%= request.getContextPath() %>/Catalogo?sede=Laquila">Sede L'Aquila</a></li>
                        </ul>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="profiloDropdown" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            Profilo
                        </a>
                        <ul class="dropdown-menu bg-dark text-light" aria-labelledby="profiloDropdown">
                            <%
                                HttpSession session1 = request.getSession(false);
                                Cliente clienteLoggato = (session1 != null) ? (Cliente) session1.getAttribute("cliente") : null;
                                if (clienteLoggato != null) {
                            %>
                            <li><a class="dropdown-item text-light"
                                   href="<%= request.getContextPath() %>/storicoOrdini">Storico</a></li>
                            <li><a class="dropdown-item text-light"
                                   href="<%= request.getContextPath() %>/logout">Logout</a></li>
                            <%
                            } else {
                            %>
                            <li><a class="dropdown-item text-light"
                                   href="<%= request.getContextPath() %>/login">Login</a></li>
                            <li><a class="dropdown-item text-light"
                                   href="<%= request.getContextPath() %>/registrazione">Registrati ora</a></li>
                            <%
                                }
                            %>
                        </ul>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/About">About</a>
                    </li>
                </ul>
                <span class="navbar-brand text-danger fw-bold ms-auto" style="font-family:'Baloo Bhai 2',cursive;">Catena-Movieplex</span>
            </div>
        </div>
    </nav>
</header>
</body>
</html>
