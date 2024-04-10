package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.ynov.tictactoe.Game.Game;

public class GameServerHost {
    private ServerSocket _serverSocket;
    private Socket _clienSocket;
    private PrintWriter _send;
    private BufferedReader _receive;
    private Game _game;
    private String _clientPseudonyme = "Anonymous";

    public GameServerHost(int port) throws IOException {
        this.RunServer(port);
    }

    private void RunServer(int port) throws IOException {
        this._serverSocket = new ServerSocket(port);
        System.out.println("You host the game!");
        this.WaitingAnimation.start();
        this._clienSocket = this._serverSocket.accept();
        this._send = new PrintWriter(this._clienSocket.getOutputStream(), true);
        this._receive = new BufferedReader(new InputStreamReader(this._clienSocket.getInputStream()));
    }

    private void CloseServer() throws IOException {
        this._send.close();
        this._receive.close();
        this._clienSocket.close();
        this._serverSocket.close();
    }

    public void GameManager() throws IOException {
        String pseudonyme = this.GetPlayerPseudonyme();
        this._clientPseudonyme = this._receive.readLine();
        this._send.println(pseudonyme);
        System.out.println(String.format("%s connected!", this._clientPseudonyme));
        this._game = new Game("X");
        this._game.ShowBoard();

        do {
            if (IsClientWon()) {
                System.out.println(String.format("%s won!", this._clientPseudonyme));
                this.CloseServer();
                return;
            }
            GetPlayerPosition();
            if (this._game.CheckWin()) {
                System.out.println("You win!");
                _send.println("win");
                this.CloseServer();
                return;
            }
            this._send.println("");
            System.out.printf("Waiting %s to play...\n", this._clientPseudonyme);
            UpdateClientPos();
            this._send.println("");
        } while (true);
    }

    private String GetPlayerPseudonyme() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter your pseudonyme >> ");
        String pseudonyme = input.readLine();
        if (pseudonyme.length() > 10) {
            System.out.println("Your pseudonyme need to be less than 10 caracters.");
            return GetPlayerPseudonyme();
        }
        return pseudonyme;
    }

    private void GetPlayerPosition() throws IOException {
        System.out.println("Your turn!");
        Integer pos = this._game.GetPlayerInput();
        this.SendAndReceive(pos.toString());
        this._game.ShowBoard();
    }

    private void UpdateClientPos() throws IOException {
        String data = this._receive.readLine();
        int pos = Integer.parseInt(data);
        this._game.SetPosition(pos, "O");
        this._game.ShowBoard();
        System.out.println(String.format("%s play %s cell.", this._clientPseudonyme, pos+1));
    }

    private boolean IsClientWon() throws IOException {
        String data = this._receive.readLine();
        if (data.equals("win")) return true;
        return false;
    }

    private Thread WaitingAnimation = new Thread() {
        public void run() {
            String logMsg = "Waiting for player";
            while (_clienSocket == null) {
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

    private String SendAndReceive(String msg) throws IOException {
        this._send.println(msg);
        return this._receive.readLine();
    }
}
