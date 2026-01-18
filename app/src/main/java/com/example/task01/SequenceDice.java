package com.example.task01;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SequenceDice{

    private final int rows = 6;
    private final int cols = 6;
    private final String[][] board;
    private  Token[][] tokenBoard;
    private final List<Player> players;
    private int currentPlayerIndex;
    private final Property<Boolean> gameStarted;
    private final Property<List<Integer>> gameOver;
    private final Property<Player> currentPlayer;
    private final Property<Integer> dieRoll1;
    private final Property<Integer> dieRoll2;
    private final Property<Player> winner;
    private final Property<List<String>> availableCellsToPlay;
    private final Property<List<String>> availableCellsToRemove;
    private Property<List<String>> emptyCellsToPlay;
    private Property<List<String>> availableCellsToReplace;

    private Property<List<Integer>> firstRoll;
    private Property<Boolean> tokenPlaced;
    private Property<Boolean> tokenRemoved;
    private Property<Boolean> noAvailableCell;
    private Property<List<Integer>> p1FirstRoll;
    private Property<List<Integer>> p2FirstRoll;
    private Property<List<Integer>> p3FirstRoll;

    int numberOfPlayers;
    int countFirstRolls = 0;
    Random rand = new Random();
    int maxFirstRoll =0;
    int keepPos = 0;
    int p1LongestSequence =0;
    int p2LongestSequence =0;
    int p3LongestSequence =0;

    public void calcLongestSequence(int combinations){
        if(currentPlayer.get().getToken().getColor()==GameActivity.p1Color){
            if(p1LongestSequence<combinations){
                p1LongestSequence=combinations;
            }
        }
        else if(currentPlayer.get().getToken().getColor()==GameActivity.p2Color){
            if(p2LongestSequence<combinations)p2LongestSequence=combinations;
        }
        else{
            if(p3LongestSequence<combinations)p3LongestSequence=combinations;
        }
    }
    public SequenceDice(List<String> playerNames,int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.board = initializeBoard();
        this.tokenBoard = initializeTokenBoard();
        this.players = initializePlayers(playerNames);
        this.currentPlayerIndex = 0;
        this.gameStarted = new Property<>(this, false);
        this.currentPlayer = new Property<>(this, null);
        this.dieRoll1 = new Property<>(this, 0);
        this.dieRoll2 = new Property<>(this, 0);
        this.winner = new Property<>(this, null);
        this.availableCellsToPlay = new Property<>(this,null);
        this.availableCellsToRemove = new Property<>(this,null);
        this.emptyCellsToPlay = new Property<>(this,null);
        this.availableCellsToReplace = new Property<>(this,null);
        this.firstRoll = new Property<>(this,null);
        this.tokenPlaced = new Property<>(this, false);
        this.tokenRemoved = new Property<>(this,false);
        this.gameOver = new Property<>(this, null);
        this.noAvailableCell = new Property<>(this, false);
        this.p1FirstRoll = new Property<>(this,null);
        this.p2FirstRoll = new Property<>(this,null);
        this.p3FirstRoll = new Property<>(this,null);
    }

    private String[][] initializeBoard() {
        return new String[][]{
                {"2", "3", "4", "5", "6", "2"},
                {"6", "7", "8", "9", "7", "3"},
                {"5", "9", "12", "12", "8", "4"},
                {"4", "8", "12", "12", "9", "5"},
                {"3", "7", "9", "8", "7", "6"},
                {"2", "6", "5", "4", "3", "2"}
        };
    }

    private Token[][] initializeTokenBoard(){
        return new Token[][]{
                {null,null , null, null, null, null},
                {null,null , null, null, null, null},
                {null,null , null, null, null, null},
                {null,null , null, null, null, null},
                {null,null , null, null, null, null},
                {null,null , null, null, null, null}
        };
    }

    private List<Player> initializePlayers(List<String> playerNames) {
        List<Player> players = new ArrayList<>();
        int count = 1;
        for (String name : playerNames) {
            players.add(new Player(name,count));
            count++;
        }
        return players;
    }

    public void rollDice() {
        if(gameStarted.get()){

            int roll1 = rand.nextInt(6) + 1;
            int roll2 = rand.nextInt(6) + 1;
            dieRoll1.set(roll1);
            dieRoll2.set(roll2);
            processRoll(roll1+roll2);
        }
        else{
            if(countFirstRolls != players.size()){
                List<Integer> fstRoll = new ArrayList<>();
                int roll1 = rand.nextInt(6) + 1;
                int roll2 = rand.nextInt(6) + 1;
                int sum = roll1+roll2;
                if(sum> maxFirstRoll) {
                    maxFirstRoll = sum;
                    keepPos = countFirstRolls;
                }
                fstRoll.add(roll1);
                fstRoll.add(roll2);
                countFirstRolls++;
                firstRoll.set(fstRoll);
                if(countFirstRolls ==players.size()){
                    gameStarted.set(true);
                    currentPlayerIndex = keepPos;
                    currentPlayer.set(players.get(currentPlayerIndex));
                    processRoll(maxFirstRoll);
                }
            }
        }
    }

    private void processRoll(int sum) {

        Player player = currentPlayer.get();

        if(sum == 10){
            checkCellsToRemoveFrom(sum,player);
        }
        else if(sum == 11){
            checkEmptyCells(sum);
        }
        else{
            checkAvailableCells(sum);
        }

    }
    public void removeToken(int row,int col,int sum){
        if (row != -1 || col != -1) {
            tokenBoard[row][col] = null;
            tokenRemoved.set(true);
        }
        checkWinner();
        updateTurn(sum);

    }

    private void checkCellsToRemoveFrom(int sum, Player player) {
        List<String> cellsToRemove = new ArrayList<>();
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if(tokenBoard[i][j]!=null
                        && tokenBoard[i][j].getPlayerNumber()!= player.getPlayerNumber()
                        && (!Objects.equals(board[i][j], "2") && !Objects.equals(board[i][j], "12"))){
                    String cell = i +Integer.toString(j);
                    cellsToRemove.add(cell);
                }
            }
        }
        availableCellsToRemove.set(cellsToRemove);
    }

    private void checkEmptyCells(int sum){
        List<String> emptyCells = new ArrayList<>();
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if(tokenBoard[i][j]==null){
                    String cell = i +Integer.toString(j);
                    emptyCells.add(cell);
                }
            }
        }
        emptyCellsToPlay.set(emptyCells);
    }

    public void placeTokens(int row,int col,int sum) {
        tokenBoard[row][col] = getCurrentPlayerProperty().get().getToken();
        tokenPlaced.set(true);
        checkWinner();
        updateTurn(sum);
    }
    private void checkAvailableCells(int sum) {
        List<String> availableCells = new ArrayList<>();
        List<String> cellsToReplace = new ArrayList<>();
        for(int i=0;i<board.length;i++){
            for(int j=0;j< board.length;j++){
                if(Integer.parseInt(board[i][j]) == sum && tokenBoard[i][j]==null){
                    String cell = i +Integer.toString(j);
                    availableCells.add(cell);
                }
                else if (Integer.parseInt(board[i][j]) == sum && tokenBoard[i][j]!=null &&
                    !Objects.equals(tokenBoard[i][j].getColor(), getCurrentPlayerProperty().get().getToken().getColor())){
                    String cell = i +Integer.toString(j);
                    cellsToReplace.add(cell);
                }
            }
        }
        if(availableCells.isEmpty() && cellsToReplace.isEmpty()){
            noAvailableCell.set(true);
            updateTurn(sum);
        }
        else if(availableCells.isEmpty())availableCellsToReplace.set(cellsToReplace);
        else availableCellsToPlay.set(availableCells);
    }

    private void checkWinner() {
        if(numberOfPlayers==2){
            if(checkHorizontalFor5() || checkVerticalFor5() ||
                    checkDiagonal1For5() || checkDiagonal2For5() ||
                    checkOtherDiagonal1() || checkOtherDiagonal2() ||
                    checkOtherDiagonal3() || checkOtherDiagonal4()){
                winner.set(currentPlayer.get());
                gameOver.set(Arrays.asList(p1LongestSequence,p2LongestSequence,p3LongestSequence));
            }
        }
        else{
            if(checkHorizontalFor6() || checkVerticalFor6() ||
                    checkDiagonal1For6() || checkDiagonal2For6()){
                winner.set(currentPlayer.get());
                gameOver.set(Arrays.asList(p1LongestSequence,p2LongestSequence,p3LongestSequence));
            }
        }
    }
    private void updateTurn(int sum) {
        if(sum!=2 && sum!=12){
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer.set(players.get(currentPlayerIndex));
        }
        else{
            currentPlayer.set(currentPlayer.get());
        }
    }
    private boolean checkHorizontalFor5(){

        for(int i=0;i<tokenBoard.length;i++){
            for(int j=0;j<2;j++){
                if(tokenBoard[i][j]==null)continue;
                int sumCombinations =1 ;
                int curColor = tokenBoard[i][j].getColor();
                for(int z=j+1;z<5+j;z++){
                    if(tokenBoard[i][z]==null)continue;
                    if(tokenBoard[i][z].getColor()==curColor)sumCombinations++;
                }
                calcLongestSequence(sumCombinations);
                if (sumCombinations==5){
                    Log.i("Horizontal combinations"," returned true");
                    return true;
                }
            }
        }
        Log.i("Horizontal combinations"," returned false");
        return false;
    }
    private boolean checkHorizontalFor6(){

        for(int i=0;i<tokenBoard.length;i++){
            int sumCombinations =1 ;
            for(int j=0;j<tokenBoard.length-1;j++){
                if(tokenBoard[i][j]==null)continue;
                int curColor = tokenBoard[i][j].getColor();
                if(tokenBoard[i][j+1]==null)continue;
                if(tokenBoard[i][j+1].getColor()==curColor)sumCombinations++;
            }
            calcLongestSequence(sumCombinations);
            if (sumCombinations==6)return true;
        }
        return false;
    }
    private boolean checkVerticalFor5(){
        for(int j=0;j<tokenBoard.length;j++){
            for(int i=0;i<2;i++){
                if(tokenBoard[i][j]==null)continue;
                int sumCombinations =1 ;
                int curColor = tokenBoard[i][j].getColor();
                for(int z=i+1;z<5+i;z++){
                    if(tokenBoard[z][j]==null)continue;
                    if(tokenBoard[z][j].getColor()==curColor)sumCombinations++;
                }
                calcLongestSequence(sumCombinations);
                if (sumCombinations==5){
                    Log.i("Vertical Combinations"," returned true");
                    return true;
                }
            }
        }
        Log.i("Vertical combinations"," returned false");
        return false;
    }
    private boolean checkVerticalFor6(){
        for(int j=0;j<tokenBoard.length;j++){
            int sumCombinations =1;
            for(int i=0;i<tokenBoard.length-1;i++){
                if(tokenBoard[i][j]==null)continue;
                int curColor = tokenBoard[i][j].getColor();
                if(tokenBoard[i+1][j]==null)continue;
                if(tokenBoard[i+1][j].getColor()==curColor)sumCombinations++;

            }
            calcLongestSequence(sumCombinations);
            if (sumCombinations==6)return true;
        }
        return false;
    }
    private boolean checkDiagonal1For5(){
        for(int j=0;j<2;j++){
            if(tokenBoard[j][j]==null)continue;
            int sumCombinations =1 ;
            int curColor = tokenBoard[j][j].getColor();
            for(int z=j+1;z<5+j;z++){
                if(tokenBoard[z][z]==null)continue;
                if(tokenBoard[z][z].getColor()==curColor)sumCombinations++;
            }
            calcLongestSequence(sumCombinations);
            if (sumCombinations==5){
                Log.i("Diagonal combination 1"," returned true");
                return true;
            }
        }
        Log.i("Diagonal combination 1"," returned false");
        return false;
    }
    private boolean checkDiagonal1For6(){
        int sumCombinations =1 ;
        for(int j=0;j<tokenBoard.length-1;j++){
            if(tokenBoard[j][j]==null)continue;
            int curColor = tokenBoard[j][j].getColor();
            if(tokenBoard[j+1][j+1]==null)continue;
            if(tokenBoard[j+1][j+1].getColor()==curColor)sumCombinations++;
        }
        calcLongestSequence(sumCombinations);
        if (sumCombinations==6)return true;
        else return false;
    }
    private boolean checkDiagonal2For5(){
        for(int j=5;j>3;j--){
            if(tokenBoard[j][5-j]==null)continue;
            int sumCombinations =1 ;
            int curColor = tokenBoard[j][5-j].getColor();
            for(int z=j;z>j-5;z--){
                if(tokenBoard[z][5-z]==null)continue;
                if(tokenBoard[z][5-z].getColor()==curColor)sumCombinations++;
            }
            calcLongestSequence(sumCombinations);
            if (sumCombinations==5){
                Log.i("Diagonal combination 2"," returned true");
                return true;
            }
        }
        Log.i("Diagonal combination 2"," returned false");
        return false;
    }
    private boolean checkDiagonal2For6(){
        int sumCombinations =1 ;
        for(int j=5;j>0;j--){
            if(tokenBoard[j][5-j]==null)continue;
            int curColor = tokenBoard[j][5-j].getColor();
            if(tokenBoard[j-1][(5-j)+1]==null)continue;
            if(tokenBoard[j-1][(5-j)+1].getColor()==curColor)sumCombinations++;
            calcLongestSequence(sumCombinations);
            if (sumCombinations==6)return true;
        }
        return false;
    }
    private boolean checkOtherDiagonal1(){
        int sumCombinations =1 ;
        for(int i=4;i>0;i--){
            if(tokenBoard[i][4-i]==null)return false;
            if(tokenBoard[i-1][(4-i)+1] == null)return false;
            if(tokenBoard[i][4-i].getColor() == tokenBoard[i-1][(4-i)+1].getColor()){
                sumCombinations++;
                calcLongestSequence(sumCombinations);
            }
            else return false;
        }
        if (sumCombinations==5){
            Log.i("Other diagonal 1"," returned true");
            return true;
        }
        else{
            Log.i("Other diagonal 1"," returned false");
            return false;
        }
    }
    private boolean checkOtherDiagonal2(){
        int sumCombinations =1 ;
        for(int i=5;i>1;i--){
            if(tokenBoard[i][5-(i-1)]==null)return false;
            if(tokenBoard[i-1][(5-(i-1))+1] == null)return false;
            if(tokenBoard[i][5-(i-1)].getColor() == tokenBoard[i-1][(5-(i-1))+1].getColor()){
                sumCombinations++;
                calcLongestSequence(sumCombinations);
            }
            else return false;
        }
        if (sumCombinations==5){
            Log.i("Other diagonal 2"," returned false");
            return true;
        }
        else {
            Log.i("Other diagonal 2"," returned false");
            return false;
        }
    }
    private boolean checkOtherDiagonal3(){
        int sumCombinations =1 ;
        for(int i=1;i<5;i++){
            if(tokenBoard[i][i-1]==null)return false;
            if(tokenBoard[i+1][(i-1)+1] == null)return false;
            if(tokenBoard[i][i-1].getColor() == tokenBoard[i+1][(i-1)+1].getColor()){
                sumCombinations++;
                calcLongestSequence(sumCombinations);
            }
            else return false;
        }
        if (sumCombinations==5){
            Log.i("Other diagonal 3"," returned true");
            return true;
        }
        else {
            Log.i("Other diagonal 3"," returned false");
            return false;
        }
    }
    private boolean checkOtherDiagonal4(){
        int sumCombinations =1 ;
        for(int i=0;i<4;i++){
            if(tokenBoard[i][i+1]==null)return false;
            if(tokenBoard[i+1][(i+1)+1] == null)return false;
            if(tokenBoard[i][i+1].getColor() == tokenBoard[i+1][(i+1)+1].getColor()){
                sumCombinations++;
                calcLongestSequence(sumCombinations);
            }
            else return false;
        }
        if (sumCombinations==5){
            Log.i("Other diagonal 4"," returned true");
            return true;
        }
        else {
            Log.i("Other diagonal 4"," returned false");
            return false;
        }
    }

    public Property<Boolean> getGameStartedProperty() {
        return gameStarted;
    }

    public Property<Player> getCurrentPlayerProperty() {
        return currentPlayer;
    }

    public Property<Integer> getDieRoll1Property() {
        return dieRoll1;
    }

    public Property<Integer> getDieRoll2Property() {
        return dieRoll2;
    }

    public Property<Player> getWinnerProperty() {
        return winner;
    }
    public Property<List<String>> getAvailableCellsToPlay(){
        return availableCellsToPlay;
    }
    public Property<List<String>> getAvailableCellsToRemove(){
        return availableCellsToRemove;
    }
    public Property<List<String>> getEmptyCellsToPlay(){return emptyCellsToPlay;}
    public Property<List<String>> getAvailableCellsToReplace(){return availableCellsToReplace;}
    public Property<List<Integer>> getFirstRoll(){
        return firstRoll;
    }
    public Property<Boolean> getTokenPlaced(){return tokenPlaced;}
    public Property<Boolean> getTokenRemoved(){return tokenRemoved;}
    public Property<List<Integer>> getGameOver() {
        return gameOver;
    }
    public Property<Boolean> getNoAvailableCell(){return noAvailableCell;}

}
