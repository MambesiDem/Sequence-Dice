package com.example.task01;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ConstraintLayout parentLayout;
    int numberOfPlayers;
    List<String> playerNames;
    SequenceDice game;
    int roll1=0;
    int roll2 =0;
    TextView txtPlayerTurn;
    String curOperation ="";
    List<Integer> idsOfCurCells = new ArrayList<>();
    TextView txtTokenState;
    Button btnRoll;

    ImageView dice1;
    ImageView dice2;
    TextView txtP1;
    TextView txtP2;
    TextView txtP3;
    int countRolls =0;
    public static int p1Color;
    public static int p2Color;
    public static int p3Color;
    private  AnimatorSet animatorSet;
    int countRounds =0;
    int keepNumRounds = 0;
    int countTokensPlayed = 0;
    int countTokensRemoved = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        if(intent!=null){
            Bundle extras = intent.getExtras();
            if(extras!=null){
                String numPlayers = extras.getString("Number Of Players");
                assert numPlayers != null;
                numberOfPlayers = Integer.parseInt(numPlayers);
                String[] playersArray = extras.getStringArray("List of names");
                assert playersArray != null;
                playerNames = Arrays.asList(playersArray);
                p1Color = extras.getInt("Player1 Color");
                p2Color = extras.getInt("Player2 Color");
                if(numberOfPlayers==3){
                    p3Color = extras.getInt("Player3 Color");
                }
            }
        }

        dice1 = findViewById(R.id.imgDice1);
        dice2 = findViewById(R.id.imgDice2);
        txtP1 = findViewById(R.id.txtP1Roll);
        txtP2 = findViewById(R.id.txtP2Roll);
        txtP3 = findViewById(R.id.txtP3Roll);
        parentLayout = findViewById(R.id.parentLayout);
        txtPlayerTurn = findViewById(R.id.txtPlayerTurn);
        txtTokenState = findViewById(R.id.txtTokenState);
        btnRoll = findViewById(R.id.btnRoll);
        btnRoll.setText(playerNames.get(0) + " Roll");
        game = new SequenceDice(playerNames,numberOfPlayers);
        setListerners();
        animateRollButton();
    }
    @SuppressLint("SetTextI18n")
    private void setListerners() {
        game.getGameStartedProperty().addListener(((property, oldValue, newValue) -> {
            if(newValue){
                Toast.makeText(this,"The game has started.",Toast.LENGTH_LONG).show();
            }
            btnRoll.setText("Roll");
            recordRounds();
        }));
        game.getCurrentPlayerProperty().addListener(((property, oldValue, newValue) -> {
            txtPlayerTurn.setText("It's now "+newValue.getName()+"'s turn");
        }));
        game.getFirstRoll().addListener((property, oldValue, newValue) -> {
            int sum = newValue.get(0)+newValue.get(1);
            dice1.setImageResource(getResources().getIdentifier("dice_" + newValue.get(0), "drawable", getPackageName()));
            dice2.setImageResource(getResources().getIdentifier("dice_" + newValue.get(1), "drawable", getPackageName()));

            if(countRolls == 0) {
                if(numberOfPlayers==4)txtP1.setText(reduceName(playerNames.get(0))+" Rolled: "+sum);
                else txtP1.setText(playerNames.get(0)+" Rolled: "+sum);
                btnRoll.setText(playerNames.get(1)+" Roll");
                countRolls++;
            } else if (countRolls == 1) {
                if(numberOfPlayers==4)txtP2.setText(reduceName(playerNames.get(1))+" Rolled: "+sum);
                else txtP1.setText(playerNames.get(1)+" Rolled: "+sum);
                if(playerNames.size()==3){
                    btnRoll.setText(playerNames.get(2)+" Roll");
                }
                countRolls++;
            } else if (countRolls == 2) {
                txtP3.setText(playerNames.get(2)+" Rolled: "+sum);
            }
        });
        game.getDieRoll1Property().addListener(((property, oldValue, newValue) -> {
            roll1 = newValue;
            dice1.setImageResource(getResources().getIdentifier("dice_" + roll1, "drawable", getPackageName()));
        }));
        game.getDieRoll2Property().addListener(((property, oldValue, newValue) -> {
            recordRounds();
            roll2 = newValue;
            dice2.setImageResource(getResources().getIdentifier("dice_" + roll2, "drawable", getPackageName()));
            int sum = roll1+roll2;
            txtP1.setVisibility(View.INVISIBLE);
            txtP2.setVisibility(View.VISIBLE);
            String curP = game.getCurrentPlayerProperty().get().getName();
            if(numberOfPlayers==4){
                txtP2.setText(reduceName(curP)+ " Rolled: "+sum);
            }
            else{
                txtP2.setText(curP+" Rolled: "+sum);
            }

            txtP3.setVisibility(View.VISIBLE);
            txtP3.setText("Choose a cell to play");
            txtP3.setTextColor(Color.WHITE);
            txtP3.setBackgroundColor(game.getCurrentPlayerProperty().get().getToken().getColor());
        }));
        game.getAvailableCellsToPlay().addListener(((property, oldValue, newValue) -> {
            updateUIToPlace(newValue);
        }));
        game.getAvailableCellsToReplace().addListener(((property, oldValue, newValue) -> {
            updateUIToPlace(newValue);
        }));
        game.getAvailableCellsToRemove().addListener(((property, oldValue, newValue) -> {
            updateUIToRemove(newValue);
        }));
        game.getEmptyCellsToPlay().addListener(((property, oldValue, newValue) -> {
            if(newValue.isEmpty()){
                Toast.makeText(this,"No empty cell available.",Toast.LENGTH_LONG).show();
            }
            else{
                updateUIToPlace(newValue);
            }
        }));
        game.getWinnerProperty().addListener(((property, oldValue, newValue) -> {
            txtTokenState.setText(newValue.getName() +" Won!");
        }));
        game.getTokenPlaced().addListener(((property, oldValue, newValue) -> {
            txtTokenState.setText(game.getCurrentPlayerProperty().get().getName()+" placed a Token");
            removeTextViewBackground(parentLayout,idsOfCurCells);
            idsOfCurCells.clear();
            int id = getResources().getIdentifier("btnRoll","id",getPackageName());
            List<Integer> btnId = new ArrayList<>();
            btnId.add(id);
            setViewsUnclickableExcept(parentLayout,btnId);
            countTokensPlayed++;
        }));
        game.getTokenRemoved().addListener(((property, oldValue, newValue) -> {
            txtTokenState.setText(game.getCurrentPlayerProperty().get().getName()+" removed a Token");
            removeTextViewBackground(parentLayout,idsOfCurCells);
            idsOfCurCells.clear();
            int id = getResources().getIdentifier("btnRoll","id",getPackageName());
            List<Integer> btnId = new ArrayList<>();
            btnId.add(id);
            setViewsUnclickableExcept(parentLayout,btnId);
            countTokensRemoved++;
        }));
        game.getNoAvailableCell().addListener(((property, oldValue, newValue) -> {
            txtTokenState.setText("No available cell to play.");
            txtP3.setText("No available cell to play.");
        }));
        game.getGameOver().addListener(((property, oldValue, newValue) -> {
            gameOver(newValue.get(0),newValue.get(1),newValue.get(2));
        }));

    }
    public String reduceName(String name){
        String players = name.substring(name.indexOf('(') + 1, name.indexOf(')'));
        String[] playerN = players.split("&");
        char s = playerN[0].charAt(0);
        char n = playerN[1].charAt(1);
        StringBuilder t = new StringBuilder();
        for(int i=0;i<6;i++){
            t.append(name.charAt(i));
        }
        return t+" ("+s +" & "+n+")";
    }

    private void gameOver(int p1LongestSequence,int p2LongestSequence,int p3LongestSequence) {
        SharedPreferences sharedPreferences = getSharedPreferences("LeaderboardPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int currentWins = sharedPreferences.getInt(game.getCurrentPlayerProperty().get().getName(), 0);
        int updatedWins = currentWins + 1;

        editor.putInt(game.getCurrentPlayerProperty().get().getName(), updatedWins);
        editor.apply();

        Intent intent = new Intent(this,GameReportActivity.class);
        intent.putExtra("Winner Name",game.getCurrentPlayerProperty().get().getName());
        intent.putExtra("Rounds Played",keepNumRounds);
        intent.putExtra("Tokens Placed",countTokensPlayed);
        intent.putExtra("Tokens Removed",countTokensRemoved);
        intent.putExtra("Number of players",numberOfPlayers);
        intent.putExtra("P1 Longest Sequence",p1LongestSequence);
        intent.putExtra("P2 Longest Sequence",p2LongestSequence);
        intent.putExtra("P3 Longest Sequence",p3LongestSequence);
        startActivity(intent);
    }

    private void recordRounds() {
        countRounds++;
        if(numberOfPlayers == 3){
            if(countRounds == 3){
                keepNumRounds++;
                countRounds=0;
            }
        }
        else{
            if(countRounds == 2){
                keepNumRounds++;
                countRounds=0;
            }
        }
    }

    private void rollDice(ImageView dice1, ImageView dice2) {
        AnimationDrawable rollingAnimation = new AnimationDrawable();

        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_1), 100);
        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_2), 100);
        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_3), 100);
        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_4), 100);
        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_5), 100);
        rollingAnimation.addFrame(ContextCompat.getDrawable(this, R.drawable.dice_6), 100);

        rollingAnimation.setOneShot(false);
        dice2.setImageDrawable(rollingAnimation);
        dice1.setImageDrawable(rollingAnimation);
        rollingAnimation.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rollingAnimation.stop();

                game.rollDice();

            }
        }, 1000);
    }
    public void updateUIToPlace(List<String> newValue){
        List<Integer> ids = new ArrayList<>();
        for(String cell: newValue){
            int row = Character.getNumericValue(cell.charAt(0));
            int col = Character.getNumericValue(cell.charAt(1));
            int id = getResources().getIdentifier("txt"+ row + col,"id",getPackageName());
            ids.add(id);
            TextView txt = findViewById(id);
            txt.setBackgroundColor(Color.LTGRAY);
            idsOfCurCells.add(id);
        }
        curOperation = "Place Token";
        setViewsUnclickableExcept(parentLayout,ids);

    }
    public void updateUIToRemove(List<String> newValue){
        if(newValue.isEmpty()){
            Toast.makeText(this,"There's nothing to remove.",Toast.LENGTH_LONG).show();
            game.removeToken(-1,-1,-1);
        }
        else{
            List<Integer> ids = new ArrayList<>();
            for(String cell: newValue){
                int row = Character.getNumericValue(cell.charAt(0));
                int col = Character.getNumericValue(cell.charAt(1));
                int id = getResources().getIdentifier("txt"+row+col,"id",getPackageName());
                ids.add(id);
                TextView txt = findViewById(id);
                txt.setBackgroundColor(Color.LTGRAY);
                idsOfCurCells.add(id);
            }
            curOperation = "Remove Token";
            setViewsUnclickableExcept(parentLayout,ids);
        }
    }
    public void putTokenImage(int curColor,TextView curView){
        if(curColor == Color.GREEN){
            Drawable image = getResources().getDrawable(R.drawable.green);
            curView.setForeground(image);
        } else if (curColor == Color.YELLOW) {
            Drawable image = getResources().getDrawable(R.drawable.yellow);
            curView.setForeground(image);
        }
        else if(curColor==Color.RED){
            Drawable image = getResources().getDrawable(R.drawable.red);
            curView.setForeground(image);
        }
        else{
            Drawable image = getResources().getDrawable(R.drawable.blue);
            curView.setForeground(image);
        }
    }
    public void onCellClicked(View view) {

        if (curOperation == "Place Token") {
            Player cur = game.getCurrentPlayerProperty().get();
            String id = getResources().getResourceEntryName(view.getId());
            int sum = game.getDieRoll1Property().get()+game.getDieRoll2Property().get();
            TextView curView = (TextView) view;
            putTokenImage(cur.getToken().getColor(),curView);
            game.placeTokens(Character.getNumericValue(id.charAt(3)),
                    Character.getNumericValue(id.charAt(4)),sum);
        } else if (curOperation == "Remove Token") {
            String id = getResources().getResourceEntryName(view.getId());
            int sum = game.getDieRoll1Property().get()+game.getDieRoll2Property().get();
            TextView curView = (TextView) view;
            curView.setForeground(null);
            game.removeToken(Character.getNumericValue(id.charAt(3)),
                    Character.getNumericValue(id.charAt(4)),sum);
        }
        txtP3.setVisibility(View.INVISIBLE);
        txtP2.setVisibility(View.INVISIBLE);
        animateRollButton();
    }

    public void onBtnRollClicked(View view) {
        animatorSet.pause();
        rollDice(dice1,dice2);
    }
    private void setViewsUnclickableExcept(ViewGroup root, List<Integer> clickableIds) {

        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                setViewsUnclickableExcept((ViewGroup) child, clickableIds);
            } else {
                boolean isClickable = false;
                for (int id : clickableIds) {
                    if (child.getId() == id) {
                        isClickable = true;
                        break;
                    }
                }
                if (isClickable) {
                    Log.i("Clickable Views", "View ID: " + getResources().getResourceEntryName(child.getId()));
                } else {
                    Log.i("Unclickable Views", "View ID: " + getResources().getResourceEntryName(child.getId()));
                }
                child.setClickable(isClickable);

            }
        }
    }
    private void removeTextViewBackground(ViewGroup root, List<Integer> textViewIds) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof ViewGroup) {
                // Recursively process child ViewGroup
                removeTextViewBackground((ViewGroup) child, textViewIds);
            } else if (child instanceof TextView) {
                if (textViewIds.contains(child.getId())) {
                    // Remove the background
                    child.setBackground(null);
                    child.setBackgroundResource(R.drawable.txt_bordered);
                }
            }
        }
    }
    private void animateRollButton() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(btnRoll, "scaleX", 1.0f, 1.5f, 1.0f);
        scaleX.setDuration(500);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(btnRoll, "scaleY", 1.0f, 1.5f, 1.0f);
        scaleY.setDuration(500);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.start();
    }

}