package com.zaga.papadot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameOverActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private int score;
    private int oldScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_over);
        // get score and display
        score = Integer.parseInt(getIntent().getExtras().get("score").toString());
        ((TextView) findViewById(R.id.txt_score)).setText("Score:  " + score);
        // Database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        if (MainActivity.topRanker != null) oldScore = MainActivity.topRanker.score;
        if (oldScore > score) findViewById(R.id.ed_player_name).setVisibility(View.GONE);

        findViewById(R.id.btn_restart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldScore <= score) saveScoreToDatabase(score);
                startActivity(new Intent(GameOverActivity.this, GameActivity.class));
            }
        });

        findViewById(R.id.btn_backToMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldScore <= score) saveScoreToDatabase(score);
                startActivity(new Intent(GameOverActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.ed_player_name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((EditText) findViewById(R.id.ed_player_name)).setText("");
            }
        });
    }

    private void saveScoreToDatabase(int score) {
        // get player's name
        String playerName = ((EditText) findViewById(R.id.ed_player_name)).getText().toString();
        if (playerName.isEmpty()) playerName = "unknow";
        // new Ranker
        Ranker ranker = new Ranker(playerName, score);
        MainActivity.topRanker = ranker;
        myRef.child("Ranker").setValue(ranker);
    }
}
