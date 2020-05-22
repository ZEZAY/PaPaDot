package com.zaga.papadot;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {

    private int id;
    private static int nextId = 0;
    public int posX, posY;
    public boolean goX, goY;
    private Bitmap player;


    Player(Resources res) {
        player = BitmapFactory.decodeResource(res, R.drawable.player1);
        posX = posY = 0;
        goX = goY = true;
        id = nextId;
        nextId++;
    }

    public Bitmap getPlayer() {
        return player;
    }

    public Rect getCollisionShap() {
        return new Rect(posX+15, posY+15, posX + player.getWidth()-15, posY + player.getHeight()-15);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        Player anotherPlayer = (Player) obj;
        return (this.id == anotherPlayer.getId());
    }
}
