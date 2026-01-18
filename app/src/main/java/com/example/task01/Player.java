package com.example.task01;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private int numberOfTokens = 10;;
    private List<Token> tokens;
    private int playerNumber;

    public Player(String name,int playerNumber) {
        this.name = name;
        tokens = new ArrayList<>();
        this.playerNumber = playerNumber;
        initializeTokens();
    }

    private void initializeTokens() {
        for(int i =0;i<numberOfTokens;i++){
            if(playerNumber == 1){
                tokens.add(new Token(GameActivity.p1Color, playerNumber));
            }
            else if(playerNumber==2){
                tokens.add(new Token(GameActivity.p2Color,playerNumber));
            }
            else if(playerNumber==3){
                tokens.add(new Token(GameActivity.p3Color, playerNumber));
            }
        }

    }
    public Token getToken(){
        return tokens.get(tokens.size()-1);
    }


    public String getName() {
        return name;
    }
    public int getPlayerNumber(){
        return playerNumber;
    }
    public int getNumberOfTokens(){
        return numberOfTokens;
    }
}
