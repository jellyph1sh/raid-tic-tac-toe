package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.ynov.tictactoe.Game.Game;

public class GameClient {
    private Socket _socket;
    private PrintWriter _send;
    private BufferedReader _receive;
    private Game _game;
    private String _hostPseudonyme = "Anonymous";
    
    public GameClient(String ip, int port) throws UnknownHostException, IOException {
        this.Connect(ip, port);
    }

    private void Connect(String ip, int port) throws UnknownHostException, IOException {
        this._socket = new Socket(ip, port);
        this._send = new PrintWriter(this._socket.getOutputStream(), true);
        this._receive = new BufferedReader(new InputStreamReader(this._socket.getInputStream()));
        System.out.println("Connected to your host!");
    }

    private void CloseConnection() throws IOException {
        this._send.close();
        this._receive.close();
        this._socket.close();
    }

    public void GameManager() throws IOException {
        String pseudonyme = this.GetPlayerPseudonyme();
        this._hostPseudonyme = this.SendAndReceive(pseudonyme);
        this._game = new Game("O");
        System.out.println(String.format("You joined %s's game", this._hostPseudonyme));
        
        this._game.ShowBoard();
        this._send.println("");
        
        do {
            System.out.println(String.format("Waiting %s to play...", this._hostPseudonyme));
            this.UpdateHostPos();
            if (IsHostWon()) {
                System.out.println(String.format("%s won!", this._hostPseudonyme));
                this.CloseConnection();
                return;
            }
            this.GetPlayerPosition();
            if (this._game.CheckWin()) {
                System.out.println("You win!");
                _send.println("win");
                this.CloseConnection();
                return;
            }
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

    private void UpdateHostPos() throws IOException {
        String data = this._receive.readLine();
        int pos = Integer.parseInt(data);
        this._game.SetPosition(pos, "X");
        this._game.ShowBoard();
        System.out.println(String.format("%s play %s cell.", this._hostPseudonyme, pos+1));
    }

    private boolean IsHostWon() throws IOException {
        String data = this.SendAndReceive("");
        if (data.equals("win")) return true;
        return false;
    }

    private String SendAndReceive(String msg) throws IOException {
        this._send.println(msg);
        return this._receive.readLine();
    }
}
