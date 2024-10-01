/**
 * Block class
 * Blocks are built at the top of GameView when the game
 * initializes. These have various properties, shown below.
 */

package com.example.breakout;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Block {
    private Paint paint;
    private Rect block;
    public int strength;
    private int hitsTaken = 0;
    int top, bottom, left, right;

    public Block(int top, int bottom, int left, int right, int color, int strength){
        this.strength = strength;
        this.hitsTaken = 0;
        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        block = new Rect(left, top, right, bottom);
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
    }

    public void draw(Canvas canvas){
        canvas.drawRect(block, paint);
    }
    public void setColor(int color){
        paint.setColor(color);
    }
}
