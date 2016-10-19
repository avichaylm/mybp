package com.avichay.BPALM;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by avichay.mulyan on 07/10/2016.
 */
public class HttpListener implements Runnable {

    private EventsStatisticData data;
    private int port;
    private int threadPoolSize;
    private ServerSocket serverSocket = null;

    public HttpListener(EventsStatisticData data, int port, int threadPoolSize) {
        this.data = data;
        this.port = port;
        this.threadPoolSize = threadPoolSize;
    }

    @Override
    public void run() {

        ExecutorService pool = Executors.newFixedThreadPool(threadPoolSize);

        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException ioe) {

            System.err.println("Failed to listen to port: " + ioe.getMessage());
            throw new RuntimeException("Failed to listen to port " + port, ioe);

        }
        try {


            while (true) {

                Socket clientSocket = serverSocket.accept();
                pool.execute(new HttpHandler(clientSocket, data));

            }


        } catch (IOException ioe) {
            System.err.println("An error in listening to port: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        pool.shutdown();

    }

    public void close() {

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
