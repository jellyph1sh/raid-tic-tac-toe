package com.ynov.tictactoe.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Game {
    
    private List<String> _board = new ArrayList<String>();
    private String _tag;

    public Game(String tag) {
        this.InitBoard();
        this._tag = tag;
    }

    private void InitBoard() {
        for (int i=0; i<9; i++) {
            this._board.add(" ");
        }
    }

    public void ShowBoard() {
        System.out.println("+---+---+---+");
        int index = 0;
        for (String value : this._board) {
            System.out.print("| ");
            System.out.print(value+" ");
            index++;
            if (index%3==0) {
                System.out.println("|");
                System.out.println("+---+---+---+");
            }
        }
    }

    public Integer PlayerInput() throws IOException {
        System.out.print("Select cell between 1 and 9 >> ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            Integer pos = Integer.parseInt(br.readLine());
            pos -= 1;
            if (pos < 0 || pos > 8) {
                System.out.println("Not a valid position");
                return PlayerInput();
            }

            return pos;
        } catch (Exception e) {
            System.out.println("Your input is not a number.");
            return PlayerInput();
        }
    }

    public boolean SetPosition(Integer position, String tag) {
        if (" ".equals(this._board.get(position))) {    
            this._board.set(position, tag);
            return true;
        }
        return false;
    } 

    public Integer GetPlayerInput() throws IOException {
        Integer position = PlayerInput();
        if (!SetPosition(position, this._tag)) {
            System.out.println("Invalid position!");
            return GetPlayerInput();
        }
        return position;
    }

    public boolean CheckWin() {
        // Horizontal
        for (int i=0; i<3; i++) {
            if (CheckLine(i, 3) || CheckLine(i+(i*2), 1)) return true;
        }
        return CheckLine(0, 4) || CheckLine(2, 2);
    }

    private boolean CheckLine(int pos, int gap) {
        return this._board.get(pos+gap).equals(this._board.get(pos)) && this._board.get(pos+gap).equals(this._board.get(pos+(gap*2))) && this._board.get(pos) != " ";
    }
}
