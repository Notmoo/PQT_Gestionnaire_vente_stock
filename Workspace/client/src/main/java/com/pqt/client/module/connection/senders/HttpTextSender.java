package com.pqt.client.module.connection.senders;

import com.pqt.client.module.connection.listeners.IConnectionListener;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpTextSender implements ITextSender{
    @Override
    public void send(String host, String text, IConnectionListener listener) {
        try {
            String trueURL = String.format("http://%s?%s", host, format(text));

            HttpURLConnection con = (HttpURLConnection) new URL(trueURL).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(true);

            con.setDoOutput(true);
            con.connect();
            listener.onConnectedEvent();

            try(BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                StringBuilder content = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }

                listener.onMessageReceivedEvent(content.toString());
            }

            con.disconnect();
        }catch (Exception e) {
            //TODO Issue #6 : ajouter un log ici
            e.printStackTrace();
            listener.onConnexionError(e);
        }finally {
            listener.onDisconnectedEvent();
        }
    }

    // Méthode à modifier pour encoder le message suivant un algorithme spécifique
    private String format(String toFormat){
        try {
            String encodeTo = "UTF-8";
            return String.format("format=%s&message=%s", encodeTo, URLEncoder.encode(toFormat, encodeTo));
        }catch(UnsupportedEncodingException e){
            //TODO Issue #6 : ajouter un log ici
            e.printStackTrace();
        }
        return String.format("message=%s", toFormat);
    }
}
