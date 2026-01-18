package com.example.task01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LeaderBoardActivity extends AppCompatActivity {

    LinearLayout rowsContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        rowsContainer = findViewById(R.id.playerRowsContainer);
        loadData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("LeaderboardPrefs",MODE_PRIVATE);
        Map<String ,?> allEntries = sharedPreferences.getAll();
        rowsContainer.removeAllViews();
        List<Map.Entry<String, ?>> sortedEntries = new ArrayList<>(allEntries.entrySet());
        sortedEntries.sort((entry1, entry2) -> ((Integer) entry2.getValue()).compareTo((Integer) entry1.getValue()));

        int rank = 1;
        for (Map.Entry<String, ?> entry : sortedEntries) {
            if (rank > 10) break;
            String playerName = entry.getKey();
            int wins = (Integer) entry.getValue();

            addPlayerToLeaderboard(rank, playerName, wins);
            rank++;
        }
    }

    public void addPlayerToLeaderboard(int rank, String playerName, int wins) {

        LinearLayout playerRow = new LinearLayout(this);
        playerRow.setOrientation(LinearLayout.HORIZONTAL);
        playerRow.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        playerRow.setPadding(10, 10, 10, 10);
        playerRow.setBackgroundColor(Color.parseColor("#D8BFD8"));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 5);
        playerRow.setLayoutParams(params);

        TextView playerRank = new TextView(this);
        LinearLayout.LayoutParams rankParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        playerRank.setLayoutParams(rankParams);
        playerRank.setGravity(Gravity.CENTER);
        playerRank.setTextColor(Color.parseColor("#4B0082"));
        playerRank.setText(String.valueOf(rank));

        TextView playerNameView = new TextView(this);
        LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        playerNameView.setLayoutParams(nameParams);
        playerNameView.setGravity(Gravity.CENTER);
        playerNameView.setTextColor(Color.parseColor("#4B0082"));
        playerNameView.setText(playerName);

        TextView playerWins = new TextView(this);
        LinearLayout.LayoutParams winsParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        playerWins.setLayoutParams(winsParams);
        playerWins.setGravity(Gravity.CENTER);
        playerWins.setTextColor(Color.parseColor("#4B0082"));
        playerWins.setText(String.valueOf(wins));

        playerRow.addView(playerRank);
        playerRow.addView(playerNameView);
        playerRow.addView(playerWins);

        rowsContainer.addView(playerRow);
    }
}