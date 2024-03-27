package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHost {
    private ServerSocket serverSocket;
    private Socket clienSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server initialized!");
        clienSocket = serverSocket.accept();
        out = new PrintWriter(clienSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (".".equals(inputLine)) {
                out.println("good bye");
                break;
            }
            System.out.println(inputLine);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            out.println("pos(2,2)");
        }

    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clienSocket.close();
        serverSocket.close();
    }
}
