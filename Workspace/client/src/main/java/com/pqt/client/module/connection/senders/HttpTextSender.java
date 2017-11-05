package com.pqt.client.module.connection.senders;

import com.pqt.client.module.connection.listeners.IConnectionListener;
import com.sun.javafx.binding.StringFormatter;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class HttpTextSender implements ITextSender{
    @Override
    public void send(String host, String text, IConnectionListener listener) {
        try {
            String trueURL = String.format("http://%s?%s", host, encode(text));

            //TODO remove sysout
            {
                System.out.println(" --- --- ---");
                System.out.println("Sending : ");
                System.out.println(trueURL);
            }

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

                //TODO remove sysout
                {
                    System.out.println("Received : ");
                    System.out.println(content);
                }

                listener.onMessageReceivedEvent(content.toString());
            }

            con.disconnect();

        }catch (java.net.SocketTimeoutException e){
            listener.onTimeOutEvent();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            listener.onDisconnectedEvent();
        }
    }

    // Méthode à modifier pour encoder le message suivant un algorithme spécifique
    private String encode(String toEncode){
        return toEncode;
    }
}
