package com.avichay.BPALM;


import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by avichay.mulyan on 07/10/2016.
 */
public class EventsListener implements Runnable {


    private EventsStatisticData data;
    private String execPath;

    boolean canRun = true;

    public EventsListener(EventsStatisticData data, String execPath) {
        this.data = data;
        this.execPath = execPath;
    }

    @Override
    public void run() {
        try {


            Process p = Runtime.getRuntime().exec(execPath);
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            int i = 1;
            while (canRun && (line = input.readLine()) != null)

            {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    MyEvent event = mapper.readValue(line, MyEvent.class);
                    data.add(event.getEventType(), event.getData());
                } catch (JsonParseException jpe) {
                    //   System.err.println("Failed to read events from format");

                    //   throw jpe;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException ioe) {


            System.err.println("Failed to read events from program");
            throw new RuntimeException(ioe);

        }


    }

    public void close() {
        canRun = false;
    }
}
