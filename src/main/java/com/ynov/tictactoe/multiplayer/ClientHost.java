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

    public void start(int port) throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Pseudo >> ");
        String pseudo = reader.readLine();
        serverSocket = new ServerSocket(port);
        System.out.println("You host the game!");
        Thread waitingThread = new Thread() {
            public void run() {
                String logMsg = "Waiting for player";
                while (clienSocket == null) {
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    logMsg += ".";
                    System.out.println(logMsg);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        waitingThread.start();
        clienSocket = serverSocket.accept();
        out = new PrintWriter(clienSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clienSocket.getInputStream()));
        System.out.println(String.format("%s connected!", in.readLine()));
        out.println(pseudo);

        /*String inputLine;
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
        }*/
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clienSocket.close();
        serverSocket.close();
    }
}
