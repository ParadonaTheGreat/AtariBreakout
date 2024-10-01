/**
 * Breakout game
 *
 * Atari game where the objective is to bounce the ball off the paddle on the bottom and break the blocks on the ceiling.
 * Win when all the blocks are broken. Lose a life if the ball misses the paddle. Score is how many blocks you break. Block
 * health differs by color.
 *
 * Ball can speed up as you hit it more times.
 * Curved paddle with different angles of reflection when the ball hits.
 * Different difficulty levels
 *
 * Within the game:
 * How many lives the user has left
 *
 * Persistent Data:
 * Name of Player
 * High Scores
 * Date and Time
 * Keep only top 5 to 15 scores, more if multiplayer
 *
 * Three custom views:
 * Paddle
 * Ball
 * Blocks
 * Game play
 *
 * Activities:
 * Starting Activity: Start Button, Game instructions, Difficulty Setting
 * Game Activity
 * Game Over and leaderboard:
 * Show leaderboard
 */


package com.example.breakout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {

    RadioGroup rg;
    EditText nameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        rg = findViewById(R.id.rg);
        nameField = findViewById(R.id.nameField);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) findViewById(checkedId);
                String difficulty = rb.getText().toString();
                System.out.println(difficulty);
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("difficulty", difficulty);
                if (nameField.getText().toString().equals(""))
                    intent.putExtra("name", "Anonymous");
                else
                    intent.putExtra("name", nameField.getText().toString().trim());
                startActivity(intent);
            }
        });

    }


}