package com.ynov.tictactoe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ynov.tictactoe.multiplayer.ClientHost;
import com.ynov.tictactoe.multiplayer.GameClient;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        PrintMainMenu();
    }

    private static void PrintMainMenu() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Choose an option:");
        System.out.println("1. Host game");
        System.out.println("2. Join");
        System.out.print("Which option ? (1/2) >> ");
        String option = reader.readLine();
        switch (option) {
            case "1":
                ClientHost server = new ClientHost();
                server.start(8080);
                break;
            case "2":
                PrintJoinMenu();
                break;
            default:
                PrintMainMenu();
                break;
        }
    }

    private static void PrintJoinMenu() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("IP Adress >> ");
        String ip = reader.readLine();
        GameClient client = new GameClient(ip, 8080);
        client.GameManager();
    }
}