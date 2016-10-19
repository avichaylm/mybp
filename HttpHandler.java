package com.avichay.BPALM;


import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by avichay.mulyan on 10/10/2016.
 */
public class HttpHandler implements Runnable {


    Socket clientSocket;
    EventsStatisticData data;

    public HttpHandler(Socket socket, EventsStatisticData data) {
        this.clientSocket = socket;
        this.data = data;
    }

    @Override
    public void run() {


        try {


            BufferedReader in = new BufferedReader(
                                                          new InputStreamReader(clientSocket.getInputStream())
            );
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());


            String s;
            String firstLine = null;
            while ((s = in.readLine()) != null) {
                if (firstLine == null) {
                    firstLine = s;

                }
                if (s.isEmpty()) {
                    break;
                }
            }
            String word = null;
            String json = "";
            final String GET_STATS = "/stats/";
            if (firstLine != null) {

                String[] args = firstLine.split(" ");
                if (args.length > 1 && args[0].toLowerCase().equals("get") && args[1].toLowerCase().startsWith(GET_STATS)) {

                    word = args[1].substring(GET_STATS.length());


                    if (word != null) {
                        Pair<Long, Long> pair = data.getEventsStats(word);
                        long count = pair.getKey();
                        long words = pair.getValue();
                        json = "{\"event_type\": \"" + word + "\", \"events_count\" : " + count + ", \"words_count\" : " + words + " }";
                    } else {
                        json = "{\"event_type\": \"\", \"events_count\" : " + 0 + ", \"words_count\" : " + 0 + " }";


                    }
                    out.writeBytes("HTTP/1.1 200 OK\r\n");
                    out.writeBytes("application/json\r\n\r\n");
                    out.writeBytes(json);


                } else {
                    out.writeBytes("HTTP/1.1 400 Bad Request\r\n");

                }
            } else {
                out.writeBytes("HTTP/1.1 400 Bad Request\r\n");

            }


            out.flush();
            out.close();


        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
        }

    }
}
