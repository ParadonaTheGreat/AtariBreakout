package com.example.breakout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GameActivity extends AppCompatActivity {

    GameView gameView;

    private TextView lives;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        lives = findViewById(R.id.livesText);
        GameView gameView = findViewById(R.id.gameView);

        Handler h = new Handler(new Handler.Callback(){
            @Override
            public boolean handleMessage(Message msg){
                if (msg.what == 0){
                    if (lives.getText().toString().contains("3")){
                        lives.setText("Lives: 2");
                    }
                    else if (lives.getText().toString().contains("2")){
                        lives.setText("Lives: 1");
                    }
                    else if (lives.getText().toString().contains("1")){
                        lives.setText("Lives: 0");
                        try {
                            gameView.leaderBoardHandling();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                        intent.putExtra("result", 0);
                        intent.putExtra("score", msg.arg1);
                        intent.putExtra("fileName", gameView.getFilesDirectory());
                        startActivity(intent);
                    }
                }
                if (msg.what == 1){
                    Intent intent = new Intent(GameActivity.this, GameOverActivity.class);
                    intent.putExtra("result", 1);
                    intent.putExtra("score", msg.arg1);
                    intent.putExtra("fileName", gameView.getFilesDirectory());
                    startActivity(intent);
                }
                return true;
            }
        });

        gameView.setHandler(h);

        Intent intent = getIntent();
        if (intent != null){
            gameView.getDifficulty(intent.getStringExtra("difficulty"));
            gameView.userName = intent.getStringExtra("name");
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
}