package com.example.breakout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class GameOverActivity extends AppCompatActivity {

    TextView winOrLose;
    TextView leaderboard;
    TextView score;
    int userScore;
    String fileName;
    FileInputStream fis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over);
        winOrLose = findViewById(R.id.winOrLose);
        leaderboard = findViewById(R.id.leaderBoard);
        score = findViewById(R.id.score);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        if (intent != null){
            int result = intent.getIntExtra("result",0);
            userScore = intent.getIntExtra("score", 0);
            fileName = intent.getStringExtra("fileName");
            score.setText(score.getText().toString()+userScore);
            if (result == 0){
                winOrLose.setText("You Lost!");
            }
            else{
                winOrLose.setText("You Won!");
            }
        }

        try {
            fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Scanner scan = new Scanner(fis);
        int numTimes = 0;
        while(scan.hasNext() && numTimes<5){
            leaderboard.setText(leaderboard.getText() + "\n" + scan.nextLine());
            numTimes++;
        }
        try {
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scan.close();


    }

    public void restartPressed(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}