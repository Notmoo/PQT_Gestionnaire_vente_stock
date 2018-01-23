package com.pqt.server.servlets;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.server.controller.IMessageHandler;
import com.pqt.server.controller.SimpleMessageHandler;
import com.pqt.server.servlets.exceptions.BadPqtServerSetupException;
import com.pqt.server.tools.io.ISerialFileManager;
import com.pqt.server.tools.io.SimpleSerialFileManagerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TODO Issue #6 : ajouter logs
@WebServlet(name = "QueryServlet", urlPatterns = "/")
public class QueryServlet extends HttpServlet {

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

    private void executeServletProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (this.getServletContext().getRealPath("/WEB-INF/classes") == null) {
                response.getWriter().write(new BadPqtServerSetupException("Real path of ressource folder is null. Current PQT server only works with web server that unpack webapps' WAR files.").toString());
            } else {
                if (messageToolFactory == null)
                    this.messageToolFactory = new GSonMessageToolFactory();

                if (msgHandler == null) {
                    /*
                     * Le chemin passé en paramètre correspond au chemin réel vers le dossier contenant tous les fichiers de ressources
                     * Cela ne fonctionne que si le webserver utilisé unpackage le war du serveur.
                     */
                    this.msgHandler = new SimpleMessageHandler(this.getServletContext().getRealPath("/WEB-INF/classes"));
                }
                if (request.getQueryString() != null && !request.getQueryString().isEmpty() && request.getParameter("message") != null) {
                    try {
                        Message resp = msgHandler.handleMessage(messageToolFactory.getObjectParser(Message.class).parse(request.getParameter("message")));

                        response.getWriter().write(messageToolFactory.getObjectFormatter(Message.class).format(resp));
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.getWriter().write(String.format("%s : %s", e.getClass().getName(), e.getMessage()));
                        response.getWriter().write("StackTrace :");
                        e.printStackTrace(response.getWriter());
                    }
                } else {
                    response.getWriter().write("Query message was not correctly made : " + request.getQueryString());
                }
            }
        }catch (Throwable e){
            e.printStackTrace();
            response.getWriter().write(e.toString());
        }
    }
}
