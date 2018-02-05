package com.pqt.server.servlets;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.server.controller.IMessageHandler;
import com.pqt.server.controller.SimpleMessageHandler;
import com.pqt.server.servlets.exceptions.BadPqtServerSetupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

@WebServlet(name = "QueryServlet", urlPatterns = "/")
public class QueryServlet extends HttpServlet {

    private static Logger LOGGER = LogManager.getLogger(QueryServlet.class);

    private IMessageToolFactory messageToolFactory;
    private IMessageHandler msgHandler;

    public QueryServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        executeServletProcess(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            executeServletProcess(request, response);
    }

    private void executeServletProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (this.getServletContext().getRealPath("/WEB-INF/classes") == null) {
                response.getWriter().write(new BadPqtServerSetupException("Real path of ressource folder is null. Current PQT server only works with web server that unpack webapps' WAR files.").toString());
            } else {
                if (messageToolFactory == null){
                    LOGGER.debug("Initialisation de la fabrique de messages");
                    this.messageToolFactory = new GSonMessageToolFactory();
                }

                if (msgHandler == null) {
                    /*
                     * Le chemin passé en paramètre correspond au chemin réel vers le dossier contenant tous les fichiers de ressources
                     * Cela ne fonctionne que si le webserver utilisé unpackage le war du serveur.
                     */
                    this.msgHandler = new SimpleMessageHandler(this.getServletContext().getRealPath("/WEB-INF/classes"));
                }
                if (request.getQueryString() != null && !request.getQueryString().isEmpty() && request.getParameter("message") != null) {
                    try {
                        LOGGER.debug("Réception d'un message");
                        String messageToHandle;
                        if(request.getParameter("encode")!=null){
                            LOGGER.debug("Tentative de décodage du message ({})", request.getParameter("encode"));
                            messageToHandle = URLDecoder.decode(request.getParameter("message"), request.getParameter("encode"));
                        }else
                            messageToHandle = request.getParameter("message");

                        Message msg = messageToolFactory.getObjectParser(Message.class).parse(messageToHandle);

                        if(msg.getUser()!=null)
                            LOGGER.debug("Traitement du message (type : '{}', auteur : '{}')", msg.getType(), msg.getUser().getUsername());
                        else
                            LOGGER.debug("Traitement du message (type : '{}', auteur : 'null')", msg.getType());

                        Message resp = msgHandler.handleMessage(msg);

                        LOGGER.debug("Envoi de la réponse (type : '{}')", resp.getType());

                        response.getWriter().write(messageToolFactory.getObjectFormatter(Message.class).format(resp));
                    } catch (Exception e) {
                        LOGGER.error("Exception durant le traitement du message : {}", e);
                        e.printStackTrace();
                        response.getWriter().write(String.format("%s : %s", e.getClass().getName(), e.getMessage()));
                        response.getWriter().write("StackTrace :");
                        e.printStackTrace(response.getWriter());
                    }
                } else {
                    LOGGER.error("Message reçu mais incorrectement construit");
                    response.getWriter().write("Query message was not correctly made : " + request.getQueryString());
                }
            }
        }catch (Throwable e){
            LOGGER.error("Exception ou erreur durant l'exécution du processus du QueryServlet : {}", e);
            e.printStackTrace();
            response.getWriter().write(e.toString());
        }
    }
}
