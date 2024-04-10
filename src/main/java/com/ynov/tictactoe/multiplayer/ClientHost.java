package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.ynov.tictactoe.Game.Game;

public class ClientHost {
    private ServerSocket serverSocket;
    private Socket clienSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Game game;
    private String clientPseudo;

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
                    System.out.print("\033[H\033[2J"); // Clear console
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
        clientPseudo = in.readLine();
        System.out.println(String.format("%s connected!", clientPseudo));
        out.println(pseudo);

        game = new Game("X");
        game.ShowBoard();
        GameManager();
        stop();
    }

    public void GameManager() throws IOException {
        do {
            if (in.readLine().equals("win")) {
                System.out.printf("%s win!\n", clientPseudo);
                break;
            }
            System.out.println("Your turn!\n");
            Integer position = game.GetPlayerInput();
            out.println(position.toString());
            game.ShowBoard();
            in.readLine();
            if (game.CheckWin()) {
                System.out.println("You win!\n");
                out.println("win");
                break;
            }
            out.println("");
            System.out.printf("Waiting %s to play...\n", clientPseudo);
            String pos = in.readLine();
            game.SetPosition(Integer.parseInt(pos), "O");
            game.ShowBoard();
            System.out.printf("%s play %s\n", clientPseudo, Integer.parseInt(pos) + 1);
            out.println("");
        } while (true);
    }

    public void stop() throws IOException {
        in.close();
        out.close();
        clienSocket.close();
        serverSocket.close();
    }
}
