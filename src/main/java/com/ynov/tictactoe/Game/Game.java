package com.ynov.tictactoe.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
    
    private List<String> board = new ArrayList<String>();
    private String playerTurn;

    public Game(String playerTurn) {
        this.InitBoard();
        this.playerTurn = playerTurn;
    }

    private void InitBoard() {
        for (int i=0; i<9; i++) {
            board.add(" ");
        }
    }

    public void ShowBoard() {
        System.out.println("+---+---+---+");
        int index = 0;
        for (String value : board) {
            System.out.print("| ");
            System.out.print(value+" ");
            index++;
            if (index%3==0) {
                System.out.println("|");
                System.out.println("+---+---+---+");
            }
        }
    }

    public boolean Play() throws IOException {
        if (CheckWin()) return true;
        System.out.println("Select position between 1 and 9");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Integer position = Integer.parseInt(br.readLine())-1;
        if (position < 0 || position > 8) {
            System.out.println("Not a valid position");
            return Play();
        }
        if (" ".equals(board.get(position))) {    
            board.set(position, playerTurn);
            switch (playerTurn) {
                case "X":
                    playerTurn = "O";
                    break;
                case "O":
                    playerTurn = "X";
                    break;
                default:
                    break;
            }
            ShowBoard();
            return Play();
        }
        System.out.println("Wrong position");
        return Play();
    }

    private boolean CheckWin() {
        // Horizontal
        for (int i=0; i<3; i++) {
            if (CheckLine(i, 3) || CheckLine(i, 1)) return true;
        }
        return CheckLine(0, 4) || CheckLine(2, 2);
    }

    private boolean CheckLine(int pos, int gap) {
        return board.get(pos+gap).equals(board.get(pos)) && board.get(pos+gap).equals(board.get(pos+(gap*2))) && board.get(pos) != " ";
    }
}
