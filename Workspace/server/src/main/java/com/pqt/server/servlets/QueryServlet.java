package com.pqt.server.servlets;

import com.pqt.core.communication.GSonMessageToolFactory;
import com.pqt.core.communication.IMessageToolFactory;
import com.pqt.core.entities.messages.Message;
import com.pqt.server.controller.IMessageHandler;
import com.pqt.server.controller.SimpleMessageHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//TODO ajouter logs
@WebServlet(name = "QueryServlet", urlPatterns = "/")
public class QueryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        executeServletProcess(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        executeServletProcess(request, response);
    }

    private void executeServletProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IMessageToolFactory messageToolFactory = new GSonMessageToolFactory();
        IMessageHandler msgHandler = new SimpleMessageHandler();

        if (request.getQueryString() != null && !request.getQueryString().isEmpty() && request.getParameter("message")!=null) {
            try {
                Message resp = msgHandler.handleMessage(messageToolFactory.getObjectParser(Message.class).parse(request.getParameter("message")));

                response.getWriter().write(messageToolFactory.getObjectFormatter(Message.class).format(resp));
            }catch(Exception e){
                response.getWriter().write(String.format("%s : %s", e.getClass().getName(), e.getMessage()));
            }
        }else{
            response.getWriter().write("Query message was not correctly made : "+request.getQueryString());
        }
    }
}
