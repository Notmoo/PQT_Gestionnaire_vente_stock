package com.pqt.client.module.connection.senders;

import com.pqt.client.module.connection.listeners.IConnectionListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpTextSender implements ITextSender{
    private static Logger LOGGER = LogManager.getLogger(HttpTextSender.class);

    @Override
    public void send(String host, String text, IConnectionListener listener) {
        try {
            String trueURL = String.format("http://%s?%s", host, format(text));
            LOGGER.trace("Envoi de la requête HTTP : {}", trueURL);

            HttpURLConnection con = (HttpURLConnection) new URL(trueURL).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(true);

            con.setDoOutput(true);
            con.connect();
            LOGGER.trace("Connexion du HTTPTextSender");
            listener.onConnectedEvent();

            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                LOGGER.trace("réception de la réponse : {}", content.toString());
                listener.onMessageReceivedEvent(content.toString());
            }

            con.disconnect();
        }catch (Exception e) {
            LOGGER.error("Erreur durant l'envoi d'un message via HTTP vers le serveur : {}", e);
            listener.onConnexionError(e);
        }finally {
            LOGGER.trace("Déconnexion du HTTPTextSender");
            listener.onDisconnectedEvent();
        }
    }

    // Méthode à modifier pour encoder le message suivant un algorithme spécifique
    private String format(String toFormat){
        String encodeTo = "UTF-8";
        try {
            return String.format("format=%s&message=%s", encodeTo, URLEncoder.encode(toFormat, encodeTo));
        }catch(UnsupportedEncodingException e){
            LOGGER.error("Erreur d'encodage du texte à envoyer vers format {} : {}", encodeTo, e);
            LOGGER.error("Utilisation de la chaine non-formatée");
        }
        return String.format("message=%s", toFormat);
    }
}
