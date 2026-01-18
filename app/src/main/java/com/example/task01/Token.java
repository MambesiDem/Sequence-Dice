package com.example.task01;

public class Token {
    private int color;
    private int playerNumber;

    public Token(int color,int playerNumber){
        this.color = color;
        this.playerNumber = playerNumber;
    }

    public int getColor() {
        return color;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
