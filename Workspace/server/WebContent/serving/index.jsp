<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Test</title>
    <link rel="stylesheet" href="statics/css/theme.css">
    <script src="statics/jquery/jquery.min.js"></script>
    <script src="statics/js/js.cookie.js"></script>
    <script src="statics/js/communication.js"></script>
</head>

<body>
<iframe hidden width="0" height="0" border="0" name="dummyframe" id="dummyframe"></iframe>
    <span id="logged-user" hidden></span>
    <form onsubmit="login();" target="dummyframe">
        <input title="login" id="server-login" type="text">
        <input title="password" id="server-password" type="password">
        <button id="login-button" type="submit">Connexion</button>
    </form>
        <button id="logout-button" hidden onclick="logout(false);">Déconnexion</button>

    <div hidden id="error-box" class="alert">
        <span></span>
        <span class="closebtn" onclick="this.parentElement.style.display='none';"><i class="material-icons">close</i></span>
    </div>

    <div hidden id="note-board">
        <ul class="note" id="note-list">
            <%--<li class="note">--%>
                <%--<div class="note-content">--%>
                    <%--<a href=""><i class="material-icons delete">done</i></a>--%>
                    <%--<h2>Commande #1 </h2>--%>
                    <%--<ul>--%>
                        <%--<li>numéro 1</li>--%>
                        <%--<li>numéro 2</li>--%>
                        <%--<li>numéro 2</li>--%>
                        <%--<li>numéro 2tsretiaurnetaursneitrnaeuitn</li>--%>
                        <%--<li>numéro 2</li>--%>
                        <%--<li>numéro 2</li>--%>
                        <%--<li>numéro 2</li>--%>
                        <%--<li>numéro 2</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
            <%--</li>--%>
        </ul>
    </div>
</body>

<footer>
    <script>
        var cookies = Cookies.get();
        if (typeof cookies['pqtServerUsername'] !== "undefined")
            $('#server-login').val(cookies['pqtServerUsername']);
    </script>
</footer>

</html>