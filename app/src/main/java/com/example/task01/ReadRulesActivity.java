package com.example.task01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ReadRulesActivity extends AppCompatActivity {
    TextView txtRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_rules);

        txtRules = findViewById(R.id.txtGameRulesContent);
        txtRules.setText("The game is played on a 6x6 board with cells numbered 2 to 12. \n" +
                "Players use two six-sided dice and colored tokens. \n" +
                "In a four-player game, two teams of two players each compete. \n" +
                "\n" +
                "Objective:\n" +
                "-Be the first to form a sequence of five (or six in a 3 or 4-player game) tokens in a row, column, or diagonal. \\n\\n\n" +
                "\n" +
                "How to Play:\n" +
                "-To start, each player rolls the dice. The highest total goes first. \n" +
                "-Turns proceed clockwise. On each turn, a player rolls the dice and follows the rules based on the sum: \\n\n" +
                "-Sum matches an empty cell: Place a token on a matching numbered cell. \n" +
                "-Sum matches an occupied cell: Replace an opponent's token with your own. \n" +
                "-Sum is 2 or 12: After placing or replacing a token, you get an extra turn. \n" +
                "-Sum is 10 (Defensive Roll): Remove an opponent's token, except from grey cells (2s and 12s). \n" +
                "-Sum is 11 (Wild Roll): Place a token in any empty cell. \n" +
                "-Play continues until a player or team achieves the objective. \\n");

    }
    public void onBtnExtClicked(View view){
        finish();
    }

    public void openHowToPlayVideo(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=3o3EWAIfU10"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");

        // Check if the YouTube app is installed
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            // If YouTube app is not installed, open in browser
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=3o3EWAIfU10"));
            startActivity(webIntent);
        }
    }
}