package com.example.task01;

public class DieRoll {
    int die1Roll;
    int die2Roll;
    int sum;
    Player player;

    public DieRoll(int die1Roll, int die2Roll, int sum, Player player) {
        this.die1Roll = die1Roll;
        this.die2Roll = die2Roll;
        this.sum = sum;
        this.player = player;
    }

    public int getDie1Roll() {
        return die1Roll;
    }

    public int getDie2Roll() {
        return die2Roll;
    }

    public int getSum() {
        return sum;
    }

    public Player getPlayer() {
        return player;
    }
}
