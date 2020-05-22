package com.zaga.papadot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class GameView extends View {

    private boolean isGameOver;
    // background setting
    private Bitmap background;
    // game's item
    private List<Player> players;
    private int playerSpeed;
    private int score;
    private Paint scorePaint = new Paint();
    // tool
    private Timer timer;
    private Random random;
    private int maxWidth, maxHeight;
    private int minWidth = 0, minHeight = 0;

    public GameView(Context context) {
        super(context);
        isGameOver = false;
        // background
        background = BitmapFactory.decodeResource(getResources(), R.drawable.bg_white);
        background = Bitmap.createScaledBitmap(background, maxWidth, maxHeight, false);
        // players
        players = new ArrayList<>();
        playerSpeed = 10;
        // score
        scorePaint.setColor(Color.BLACK);
        scorePaint.setTextSize(90);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);
        // tools
        timer = new Timer();
        random = new Random();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        maxWidth = displayMetrics.widthPixels;
        maxHeight = displayMetrics.heightPixels;
        addPlayer(maxWidth/2, maxHeight/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isGameOver) {
            super.onDraw(canvas);
            int canvasWidth = canvas.getWidth();
            int canvasHeight = canvas.getHeight();
            // background
            canvas.drawBitmap(background, 0, 0, null);

            Bitmap exPlayer = players.get(0).getPlayer();
            minWidth = 0;
            minHeight = 0;
            maxWidth = canvasWidth - exPlayer.getWidth();
            maxHeight = canvasHeight - exPlayer.getHeight();

            // score
            score = players.size() - 1;
            canvas.drawText("" + score, canvasWidth / 2, canvasHeight / 2, scorePaint);

            // player
            for (Player player : players) {
                // check pos
                if (player.posX >= maxWidth / 2) {
                    player.goY = true;
                    if (player.posY >= maxHeight / 2)
                        player.goX = false;
                    else player.goX = true;
                } else {
                    player.goY = false;
                    if (player.posY >= maxHeight / 2)
                        player.goX = false;
                    else player.goX = true;
                }
                // to go x
                if (player.goX) player.posX += playerSpeed;
                else player.posX -= playerSpeed;
                if (player.posX > maxWidth) player.posX = maxWidth;
                if (player.posX < minWidth) player.posX = minWidth;
                // to go y
                if (player.goY) player.posY += playerSpeed;
                else player.posY -= playerSpeed;
                if (player.posY > maxHeight) player.posY = maxHeight;
                if (player.posY < minHeight) player.posY = minHeight;
                // check GameOver
                for (Player player2 : players){
                    if (!player.equals(player2) && Rect.intersects(player.getCollisionShap(), player2.getCollisionShap())){
                        Toast.makeText(getContext(), "Game Over", Toast.LENGTH_SHORT).show();
                        callGameOver();
                        isGameOver = true;
                    }
                }
                // draw
                canvas.drawBitmap(player.getPlayer(), player.posX, player.posY, null);
            }
        }
    }

    private void addPlayer(float x, float y) {
        Player player = new Player(getResources());
//        player.posX = random.nextInt(maxWidth-minWidth) + minWidth;
//        player.posY = random.nextInt(maxHeight-minHeight) + minHeight;
        player.posX = (int) x;
        player.posY = (int) y;
        players.add(player);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            addPlayer(event.getX(), event.getY());
        return true;
    }

    private void callGameOver() {
        Intent gameOverIntent = new Intent(getContext(), GameOverActivity.class);
        gameOverIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        gameOverIntent.putExtra("score", score);
        getContext().startActivity(gameOverIntent);
    }

}
