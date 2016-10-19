package com.avichay.BPALM;


import java.io.File;
import java.util.concurrent.*;

/**
 * Created by avichay.mulyan on 17/10/2016.
 */
public class Controller {

    public static void main(String[] args) {

        validateArgs(args);

        EventsStatisticData data = new EventsData();

        final String exePath = args[0];
        final int port = Integer.parseInt(args[1]);
        final int connectionPoolSize = 5;
        ExecutorService threadPool = null;

        try {
            threadPool = Executors.newFixedThreadPool(2);

            EventsListener eventsListener = new EventsListener(data, exePath);
            Future<?> eventsFuture = threadPool.submit(eventsListener);

            HttpListener httpListener=  new HttpListener(data, port,connectionPoolSize );
            Future<?> httpFuture = threadPool.submit(httpListener);


            while (!eventsFuture.isDone() && !httpFuture.isDone()) {

                  TimeUnit.SECONDS.sleep(10);
            }

            if (!httpFuture.isDone()) {
                httpListener.close();
            }
            eventsListener.close();




            System.out.println("********  SHUTTING DOWN... *************");

            threadPool.shutdownNow();
            System.out.println("********  after SHUTTING DOWN... *************");

        } catch (Exception e) {
            e.printStackTrace();
            threadPool.shutdownNow();
        }
        try {
            System.out.println("********  await... *************");
            threadPool.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println("********  after await... *************");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void validateArgs(String[] args) {
        if (args.length < 2) {
            System.err.println("Program cannot start; Missing arguments. Usage:\n");
            System.err.println("\t<events exe path> <http port>");
            System.exit(1);
        }

        final String exePath = args[0];
        int port = -1;
        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            System.err.println("Program cannot start; Invalid port " + args[1]);
            System.exit(1);
        }


        File f = new File(exePath);
        if (!f.exists()) {
            System.err.println("Program cannot start; invalid events generator exec path " + args[0]);
            System.exit(1);
        }

    }

}
