package com.example.task01;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameReportActivity extends AppCompatActivity {
    TextView txtWinningTeam;
    TextView roundsPlayed;
    TextView txtTokensPlaced;
    TextView txtTokensRemoved;
    TextView txtLongestSequence1;
    TextView txtLongestSequence2;
    TextView txtLongestSequence3;
    int numPlayers;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_report);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(extras!=null){
            String winningTeam = extras.getString("Winner Name");
            String NumRoundsPlayed = Integer.toString(extras.getInt("Rounds Played"));
            String tokensPlaced = Integer.toString(extras.getInt("Tokens Placed"));
            String tokensRemoved = Integer.toString(extras.getInt("Tokens Removed"));
            numPlayers = extras.getInt("Number of players");
            String p1longest = Integer.toString(extras.getInt("P1 Longest Sequence"));
            String p2longest = Integer.toString(extras.getInt("P2 Longest Sequence"));
            String p3Longest = Integer.toString(extras.getInt("P3 Longest Sequence"));
            txtWinningTeam = findViewById(R.id.txtWinningTeam);

            roundsPlayed = findViewById(R.id.txtRoundsPlayed);
            roundsPlayed.setText("Rounds Played: "+NumRoundsPlayed);
            txtTokensPlaced = findViewById(R.id.txtTokensPlaced);
            txtTokensPlaced.setText("Tokens Place: "+tokensPlaced);
            txtTokensRemoved = findViewById(R.id.txtTokensRemoved);
            txtTokensRemoved.setText("Tokens Removed: "+tokensRemoved);
            txtLongestSequence1 = findViewById(R.id.txtLongestSequenceA);
            txtLongestSequence1.setText("Longest Sequence (Player 1): "+p1longest);
            txtLongestSequence2 = findViewById(R.id.txtLongestSequenceB);
            txtLongestSequence2.setText("Longest Sequence (Player 2): "+p2longest);
            txtLongestSequence3 = findViewById(R.id.txtLongestSequenceC);
            if(numPlayers!=3)txtLongestSequence3.setVisibility(View.INVISIBLE);
            else txtLongestSequence3.setText("Longest Sequence (Player 3): "+p3Longest);
            if(numPlayers == 4){
                txtWinningTeam.setText(String.format("%s%s", getString(R.string.winning_team), winningTeam));
                txtLongestSequence1 = findViewById(R.id.txtLongestSequenceA);
                txtLongestSequence1.setText("Longest Sequence (Team 1): "+p1longest);
                txtLongestSequence2 = findViewById(R.id.txtLongestSequenceB);
                txtLongestSequence2.setText("Longest Sequence (Team 2): "+p2longest);
            }
            else {
                txtWinningTeam.setText(String.format("%s%s", "Winning Player: ", winningTeam));
            }
        }
    }

    public void onBtnRestartGameClicked(View view) {
        Intent intent = new Intent(this,GameReportActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBtnExitClicked(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}