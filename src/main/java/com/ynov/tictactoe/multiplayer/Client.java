package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ynov.tictactoe.Game.Game;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Game game;
    private String hostPseudo;
    
    public void startConnection(String ip, int port) throws UnknownHostException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Pseudo >> ");
        String pseudo = reader.readLine();
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        hostPseudo = sendMessage(pseudo);
        System.out.println(String.format("You joined %s's game!", hostPseudo));
        game = new Game("O");
        game.ShowBoard();
        GameManager();
    }

    public void GameManager() throws IOException {
        out.println("");
        do {
            System.out.printf("Waiting %s to play...\n", hostPseudo);
            String pos = in.readLine();
            game.SetPosition(Integer.parseInt(pos), "X");
            game.ShowBoard();
            System.out.printf("%s play %s\n", hostPseudo, Integer.parseInt(pos) + 1);
            out.println("");
            if (in.readLine().equals("win")) {
                System.out.printf("%s win!\n", hostPseudo);
                stopConnection();
                break;
            }
            System.out.println("Your turn!\n");
            Integer position = game.Play();
            out.println(position.toString());
            in.readLine();
            game.ShowBoard();
            if (game.CheckWin()) {
                System.out.println("You win!\n");
                out.println("win");
                stopConnection();
                break;
            }
            out.println("");
        } while (true);
    }

    public void startChat() throws IOException {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String resp = sendMessage("pos(1,2)");
            System.out.println(resp);
        }
    }

    public String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
