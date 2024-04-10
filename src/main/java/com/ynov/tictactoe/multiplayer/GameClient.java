package com.ynov.tictactoe.multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class GameClient {
    private Socket _clientSocket;
    private PrintWriter _out;
    private BufferedReader _in;
    
    public void Setup(String ip, int port) throws UnknownHostException, IOException {
        this._clientSocket = new Socket(ip, port);
    }
}
