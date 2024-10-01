/**
 * Paddle Class
 * Drawn by changing the position, then calling its draw method from the parent's onDraw(Canvas
 */

package com.example.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class Paddle {
    private float xPos, yPos;
    private Paint color;
    private Rect paddle;
    //private int length, width;
    public Paddle(){

    }

    public Paddle(float xPos, float yPos, float width, float height, int color) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = new Paint();
        this.color.setColor(color);
        this.color.setStyle(Paint.Style.FILL);
        paddle = new Rect((int)xPos, (int)yPos, (int)(xPos+width), (int)(yPos+height));
    }

    public void setX(float xPos){
        int tempPaddleWidth = paddle.width();
        paddle.left = (int)xPos;
        paddle.right = (int) (xPos + tempPaddleWidth);
    }
    public void draw(Canvas canvas){
        canvas.drawRect(paddle, color);
    }

    public float getLeft() {
        return paddle.left;
    }

    public float getRight() {
        return paddle.right;
    }
    public float getTop(){
        return paddle.top;
    }
    public float getBottom(){
        return paddle.bottom;
    }
}
