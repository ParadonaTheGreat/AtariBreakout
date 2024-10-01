/**
 * Ball class. This bounces around the screen.
 */

package com.example.breakout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {
    private float centerX, centerY;
    private float radius;
    private Paint color;

    public Ball(float centerX, float centerY, float radius, int color){
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.color = new Paint();
        this.color.setColor(color);
        this.color.setAntiAlias(true);
        this.color.setStyle(Paint.Style.FILL);
    }

    public void draw(Canvas canvas){
        canvas.drawCircle(centerX, centerY, radius, color);
    }

    public void setPos(float xPos, float yPos){
        this.centerX = xPos;
        this.centerY = yPos;
    }
    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public void setColor(int color){
        this.color.setColor(color);
    }
}
