package com.example.task01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button play;
    Button viewBoard;
    Button readRules;
    Button startGame;
    EditText entP1Name;
    EditText entP2Name;
    EditText entP3Name;
    EditText entP4Name;
    EditText entNumPlayers;
    String numPlayers;

    Spinner spinnerOptions;
    List<String> playerNames;
    TextView colorForP1;
    TextView colorForP2;
    TextView colorForP3;
    TextView txtColor1;
    TextView txtColor2;
    TextView txtColor3;
    int p1Color=Color.GREEN;
    int p2Color=Color.BLUE;
    int p3Color=Color.RED;
    int[] colors = {Color.RED,Color.BLUE,Color.GREEN,Color.YELLOW};
    int currentColorIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        List<String> options = Arrays.asList("Select the number of players","2","3","4");
        playerNames = new ArrayList<>();

        spinnerOptions = findViewById(R.id.spinnerOptions);
        play = findViewById(R.id.btnPlay);
        viewBoard = findViewById(R.id.btnView);
        readRules = findViewById(R.id.btnRead);
        startGame = findViewById(R.id.btnStart);
        entP1Name = findViewById(R.id.edtPlayer1Name);
        entP2Name = findViewById(R.id.edtPlayer2Name);
        entP3Name = findViewById(R.id.edtPlayer3Name);
        entP4Name = findViewById(R.id.edtPlayer4Name);
        colorForP1 = findViewById(R.id.chooseP1Color);
        colorForP2 = findViewById(R.id.chooseP2Color);
        colorForP3 = findViewById(R.id.chooseP3Color);
        colorForP1.setBackgroundColor(Color.GREEN);
        colorForP2.setBackgroundColor(Color.BLUE);
        colorForP3.setBackgroundColor(Color.RED);
        txtColor1 = findViewById(R.id.txtColor1);
        txtColor2 = findViewById(R.id.txtColor2);
        txtColor3 = findViewById(R.id.txtColor3);
        //setColorsInvisible();

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                android.R.id.text1,
                options);

        spinnerOptions.setAdapter(adapter);
        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                numPlayers = adapterView.getItemAtPosition(position).toString();

                proceed();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(adapterView.getContext(),"Select the number of players.",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void proceed() {

        switch (numPlayers) {
            case "2":
                twoPlayers();
                break;
            case "3":
                twoPlayers();
                entP3Name.setVisibility(View.VISIBLE);
                colorForP3.setVisibility(View.VISIBLE);
                txtColor3.setVisibility(View.VISIBLE);
                txtColor3.setText(R.string.player_3);
                break;
            case "4":
                twoPlayers();
                entP3Name.setVisibility(View.VISIBLE);
                entP4Name.setVisibility(View.VISIBLE);
                txtColor1.setText(R.string.team_1);
                txtColor2.setText(R.string.t2);
                break;
            default: setInvisible();
        }
    }

    private void setInvisible() {
        entP1Name.setVisibility(View.INVISIBLE);
        entP2Name.setVisibility(View.INVISIBLE);
        entP3Name.setVisibility(View.INVISIBLE);
        entP4Name.setVisibility(View.INVISIBLE);
        setColorsInvisible();
        play.setVisibility(View.INVISIBLE);
    }
    private void setColorsInvisible(){
        colorForP1.setVisibility(View.INVISIBLE);
        colorForP2.setVisibility(View.INVISIBLE);
        colorForP3.setVisibility(View.INVISIBLE);
        txtColor1.setVisibility(View.INVISIBLE);
        txtColor2.setVisibility(View.INVISIBLE);
        txtColor3.setVisibility(View.INVISIBLE);
    }

    public void onBtnPlayClicked(View view) {
        switch (numPlayers){
            case "2":
                playerNames.add(entP1Name.getText().toString());
                playerNames.add(entP2Name.getText().toString());
                break;
            case "3":
                playerNames.add(entP1Name.getText().toString());
                playerNames.add(entP2Name.getText().toString());
                playerNames.add(entP3Name.getText().toString());
                break;
            case "4":
                playerNames.add("Team 1 ("+entP1Name.getText().toString()+" & "
                +entP2Name.getText().toString()+")");
                playerNames.add("Team 2 ("+entP3Name.getText().toString()+" & "
                        +entP4Name.getText().toString()+")");
                break;
        }
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("Number Of Players",numPlayers);
        String[] playerNamesArray = playerNames.toArray(new String[0]);
        intent.putExtra("List of names",playerNamesArray);
        if(numPlayers=="3"){
            intent.putExtra("Player1 Color",p1Color);
            intent.putExtra("Player2 Color",p2Color);
            intent.putExtra("Player3 Color",p3Color);
        }
        else{
            intent.putExtra("Player1 Color",p1Color);
            intent.putExtra("Player2 Color",p2Color);
        }
        startActivity(intent);
    }
    public void twoPlayers(){
        entP1Name.setVisibility(View.VISIBLE);
        entP2Name.setVisibility(View.VISIBLE);
        colorForP1.setVisibility(View.VISIBLE);
        txtColor1.setVisibility(View.VISIBLE);
        colorForP2.setVisibility(View.VISIBLE);
        txtColor2.setVisibility(View.VISIBLE);
        txtColor1.setText(R.string.player_1);
        txtColor2.setText(R.string.player_2);
        colorForP3.setVisibility(View.INVISIBLE);
        txtColor3.setVisibility(View.INVISIBLE);
        entP3Name.setVisibility(View.INVISIBLE);
        entP4Name.setVisibility(View.INVISIBLE);
        play.setVisibility(View.VISIBLE);
    }

    public void onBtnReadRulesClicked(View view) {
        Intent intent = new Intent(this,ReadRulesActivity.class);
        startActivity(intent);
    }

    public void onBtnViewBoardClicked(View view) {
        Intent intent = new Intent(this, LeaderBoardActivity.class);
        startActivity(intent);
    }
    public void onBtnStartClicked(View view) {
        viewBoard.setVisibility(View.INVISIBLE);
        readRules.setVisibility(View.INVISIBLE);
        startGame.setVisibility(View.INVISIBLE);
        spinnerOptions.setVisibility(View.VISIBLE);
    }

    public void onP2ChangeColor(View view) {
        currentColorIndex = (currentColorIndex+1)%colors.length;
        TextView txtColor = (TextView) view;
        txtColor.setBackgroundColor(colors[currentColorIndex]);
        String id = getResources().getResourceEntryName(view.getId());
        if(id.charAt(7)=='1'){
            if(numPlayers == "3"){
                if(colors[currentColorIndex]!=p2Color && colors[currentColorIndex]!=p3Color){
                    p1Color = colors[currentColorIndex];
                }
                else{
                    onP2ChangeColor(view);
                }
            }
            else{
                if(colors[currentColorIndex]!=p2Color){
                    p1Color = colors[currentColorIndex];
                }
                else{
                    onP2ChangeColor(view);
                }
            }
        }
        else if (id.charAt(7)=='2'){
            if(numPlayers == "3"){
                if(colors[currentColorIndex]!=p1Color && colors[currentColorIndex]!=p3Color){
                    p2Color = colors[currentColorIndex];
                }
                else{
                    onP2ChangeColor(view);
                }
            }
            else{
                if(colors[currentColorIndex]!=p1Color){
                    p2Color = colors[currentColorIndex];
                }
                else{
                    onP2ChangeColor(view);
                }
            }
        }
        else{
            if(colors[currentColorIndex]!=p2Color && colors[currentColorIndex]!=p1Color){
                p3Color = colors[currentColorIndex];
            }
            else{
                onP2ChangeColor(view);
            }
        }
    }
}