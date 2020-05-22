package com.zaga.papadot;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Ranker {

    public String playerName;
    public int score;

    public Ranker() {
        // Default constructor required for calls to DataSnapshot.getValue(Ranker.class)
    }

    public Ranker(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%s = %d", playerName, score);
    }

}
// go to GameOver.class