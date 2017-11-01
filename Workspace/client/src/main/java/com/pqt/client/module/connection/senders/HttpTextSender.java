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
    public void send(String url, String text, IConnectionListener listener) {
        try {
            String trueURL = String.format("http://%s?message=%s", url, text);


            HttpURLConnection con = (HttpURLConnection) new URL(trueURL).openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setInstanceFollowRedirects(true);

            String params = URLEncoder.encode("message="+text, "UTF-8");
            con.setDoOutput(true);
            try(DataOutputStream out = new DataOutputStream(con.getOutputStream())) {
                out.writeBytes(params);
                out.flush();
            }
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

        }catch (java.net.SocketTimeoutException e){
            listener.onTimeOutEvent();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            listener.onDisconnectedEvent();
        }
    }
}
