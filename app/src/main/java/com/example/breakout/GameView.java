/**
 * This is where the game is played. The onDraw method is called to draw the bricks, the ball,
 * the paddle, and other information. The Activity that contains this view can control it.
 */

package com.example.breakout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GameView extends View implements View.OnTouchListener {

    public final int FRAMERATE = 5;
    private ArrayList<Block> blocks;
    private int rowsOfBlocks = 7;

    private Ball ball;
    private float ballRadius = 40f;
    private float ballX = 400;
    private float ballY = 1000;
    private int ballColor = Color.BLACK;

    private Paddle paddle;
    private float paddleX = 100f;
    private float paddleY = 400f;
    private float paddleWidth = 100f;
    private float paddleHeight = 20f;
    private int paddleColor = Color.BLACK;

    int incrX = 5;
    int incrY = 10;
    Handler h;
    boolean running = false;
    Handler viewCommunicator;

    String difficulty;
    int numBlocksTotal;
    Context main;
    FileOutputStream fos;
    PrintWriter output;
    int userScore;
    FileOutputStream tempFos;
    public String userName;
    int numHits;

    public GameView(Context context) {
        super(context);
        main = context;
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        main = context;
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        main = context;
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        main = context;
        init();
    }

    //Common initialization called from all constructors;
    public void init(){
        ball = new Ball(ballX, ballY, ballRadius, ballColor);
        setOnTouchListener(this);
        h = new Handler();
    }

    public void getDifficulty(String difficulty){
        this.difficulty = difficulty;
    }

    /**
     * Draw the game screen. If the blocks haven't been initialized then create the list and fill it
     * with new blocks
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if (paddle == null){
            paddleY = canvas.getHeight()-paddleHeight*5;
            paddleWidth = (int) (1.3*canvas.getWidth()/5);
            paddle = new Paddle(paddleX, paddleY, paddleWidth, paddleHeight, paddleColor);

        }

        int blockWidth = getWidth()/7;

        if (difficulty.equalsIgnoreCase("Easy")){
            rowsOfBlocks = 3;
        }
        else if (difficulty.equalsIgnoreCase("Medium")){
            rowsOfBlocks = 5;
        }
        else{
            rowsOfBlocks = 7;
        }

        if (blocks == null){
            blocks = new ArrayList<Block>();
            for (int j = 0; j<rowsOfBlocks; j++) {
                for (int i = 0; i < 7; i++) {
                    int strength = (int) (Math.random() * 3) + 1;
                    int color = Color.GREEN;
                    if (strength == 1)
                        color = Color.RED;
                    if (strength == 2)
                        color = Color.BLUE;
                    if (strength == 3)
                        color = Color.BLACK;
                    Block block = new Block(j*60+150, j*60+200, i * blockWidth + 10, i * blockWidth+blockWidth-10, color, strength);
                    blocks.add(block);
                    numBlocksTotal ++;
                    block.draw(canvas);
                }
            }
        }



        int width = canvas.getWidth();
        paddle.draw(canvas);
        ball.draw(canvas);
        for (Block block:blocks){
            block.draw(canvas);
        }
        h.postDelayed(r, FRAMERATE);
        h.postDelayed(s,FRAMERATE);
    }


    public void setHandler(Handler handler){
        viewCommunicator = handler;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        running = true;
        paddle.setX(event.getX());
        if (paddle.getLeft()<0)
            paddle.setX(0);
        if (paddle.getRight()>getWidth())
            paddle.setX(getWidth()-paddleWidth);
        //ball.setCenterX(event.getX());
        //ball.setCenterY(event.getY());
        invalidate();
        return true;
    }

    Runnable r = new Runnable(){
        @Override
        public void run() {
            if (running) {
                if (ball.getCenterX() + ballRadius >= getWidth() || ball.getCenterX() - ballRadius <= 0) {
                    incrX *= -1;
                }
                if (ball.getCenterY() - ballRadius <= 0) {
                    incrY *= -1;
                }


                if (ball.getCenterY() + ballRadius >= paddleY
                        && ball.getCenterX()<=paddle.getRight()+ballRadius && ball.getCenterX()>=paddle.getLeft()-ballRadius+2*paddleWidth/3) {
                    incrY *= -1;
                    incrX = Math.abs(incrX);
                    System.out.println("Right Third");
                }
                else if (ball.getCenterY() + ballRadius >= paddleY
                        && ball.getCenterX()<=paddle.getRight()+ballRadius-1*paddleWidth/3 && ball.getCenterX()>=paddle.getLeft()+ballRadius+1*paddleWidth/3) {
                    incrY *= -1;
                    System.out.println("Middle Third");
                }
                else if (ball.getCenterY() + ballRadius >= paddleY
                        && ball.getCenterX()<=paddle.getRight()+ballRadius-2*paddleWidth/3 && ball.getCenterX()>=paddle.getLeft()-ballRadius+0*paddleWidth/3) {
                    incrY *= -1;
                    incrX = Math.abs(incrX);
                    incrX *= -1;
                    System.out.println("Left Third");
                }


                if (ball.getCenterY()+ballRadius>= paddleY+paddleHeight+10){
                    running = false;
                    Message message = new Message();
                    message.what = 0;
                    message.arg1 = numBlocksTotal - blocks.size();
                    userScore = numBlocksTotal - blocks.size();
                    viewCommunicator.sendMessage(message);
                    ball.setCenterX(getWidth()/2);
                    ball.setCenterY(getHeight()/2);
                }

                ball.setCenterX(ball.getCenterX() + incrX);
                ball.setCenterY(ball.getCenterY()+incrY);
                postInvalidate();
            }
        }
    };

    Runnable s = new Runnable() {
        @Override
        public void run() {
            for (int i = blocks.size() - 1; i >= 0; i--) {
                Block block = blocks.get(i);
                boolean collision = false;

                if (ball.getCenterX() + ballRadius >= block.left && ball.getCenterX() - ballRadius <= block.right &&
                        ball.getCenterY() + ballRadius >= block.bottom && ball.getCenterY() - ballRadius <= block.top) {
                    numHits++;
                    if (numHits %7 == 0 && numHits !=0){
                        if (incrX <0){
                            incrX -=2;
                        }
                        if (incrX>0){
                            incrX += 2;
                        }
                        if(incrY < 0){
                            incrY -=2;
                        }
                        if (incrY >0){
                            incrY += 2;
                        }
                    }
                    float ballPrevX = ball.getCenterX() - incrX;
                    float ballPrevY = ball.getCenterY() - incrY;

                    if (ballPrevX + ballRadius < block.left && ball.getCenterX() + ballRadius >= block.left) {
                        incrX *= -1;
                        collision = true;
                    }

                    else if (ballPrevX - ballRadius > block.right && ball.getCenterX() - ballRadius <= block.right) {
                        incrX *= -1;
                        collision = true;
                    }

                    else if (ballPrevY + ballRadius < block.bottom && ball.getCenterY() + ballRadius >= block.bottom) {
                        incrY *= -1;
                        collision = true;
                    }

                    else if (ballPrevY - ballRadius > block.top && ball.getCenterY() - ballRadius <= block.top) {
                        incrY *= -1;
                        collision = true;
                    }

                    if (collision) {
                        block.strength -= 1;
                        if (block.strength == 2) {
                            block.setColor(Color.BLUE);
                        }
                        if (block.strength == 1) {
                            block.setColor(Color.RED);
                        }
                        if (block.strength == 0) {
                            blocks.remove(i);
                        }
                    }
                }

                if (blocks.size() == 0) {
                    Message msg1 = new Message();
                    msg1.what = 1;
                    msg1.arg1 = numBlocksTotal;
                    userScore = numBlocksTotal;
                    try {
                        leaderBoardHandling();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    viewCommunicator.sendMessage(msg1);
                }
            }
        }
    };


    public void leaderBoardHandling() throws IOException {
        File myFile = new File(main.getFilesDir() + "breakoutLeaderboard.txt");
        if (myFile.exists()){
            try {
                tempFos = new FileOutputStream(main.getFilesDir() + "tempBreakoutLeaderboard.txt", false);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            PrintWriter tempOutput = new PrintWriter(tempFos);

            FileInputStream fis = new FileInputStream(main.getFilesDir() + "breakoutLeaderboard.txt");
            FileInputStream tempFis = new FileInputStream(main.getFilesDir() + "tempBreakoutLeaderboard.txt");
            FileInputStream tempTempFis = new FileInputStream(main.getFilesDir() + "breakoutLeaderboard.txt");
            FileInputStream nameFis = new FileInputStream(main.getFilesDir() + "breakoutLeaderboard.txt");

            Scanner scan = new Scanner(fis);
            Scanner tempScan = new Scanner(tempFis);
            Scanner tempTempScan = new Scanner(tempTempFis);
            Scanner nameScan = new Scanner(nameFis);

            boolean changed = false;
            String nextLine = tempTempScan.nextLine();
            int nextScore = Integer.parseInt(nextLine.substring(nextLine.indexOf(": ") +2));
            while (scan.hasNext()){
                String tempLine = scan.nextLine();
                int score = Integer.parseInt(tempLine.substring(tempLine.indexOf(": ")+2));
                String line = nameScan.nextLine();
                try {
                    String tempNextLine = tempTempScan.nextLine();
                    nextScore =  Integer.parseInt(tempNextLine.substring(tempNextLine.indexOf(": ")+2));
                } catch (Exception e){
                    if (userScore<score){
                        tempOutput.println(line);
                        if (!changed)
                            tempOutput.println(java.time.LocalDate.now() + "   " + userName + ": " + userScore);
                    }

                    else{
                        if (!changed)
                            tempOutput.println(java.time.LocalDate.now() + "   " + userName + ": " + userScore);
                        tempOutput.println(line);
                    }
                    changed = true;
                    break;
                }
                if (userScore<=score) {
                    tempOutput.println(line);
                    if (userScore >= nextScore && !changed) {
                        tempOutput.println(java.time.LocalDate.now() + "   " + userName + ": " + userScore);
                        changed = true;
                    }
                }
                else{
                    if (userScore >= nextScore && !changed) {
                        tempOutput.println(java.time.LocalDate.now() + "   " + userName + ": " + userScore);
                        changed = true;
                    }
                    tempOutput.println(line);
                }
            }
            tempOutput.close();
            fos = new FileOutputStream(main.getFilesDir() + "breakoutLeaderboard.txt", false);
            output = new PrintWriter(fos);
            while (tempScan.hasNext()){
                output.println(tempScan.nextLine());
            }
            output.close();
            try {
                fis.close();
                tempFis.close();
                tempTempFis.close();
                scan.close();
                tempScan.close();
                tempTempScan.close();
                fos.close();
                tempFos.close();
                output.close();
                tempOutput.close();
                nameFis.close();
                nameScan.close();
            }
            catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        else{
            try {
                fos = new FileOutputStream(main.getFilesDir() + "breakoutLeaderboard.txt", false);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            output = new PrintWriter(fos);
            output.println(java.time.LocalDate.now() + "   " + userName + ": " + userScore);
            output.close();
            fos.close();
        }
    }

    public String getFilesDirectory(){
        return (main.getFilesDir() + "breakoutLeaderboard.txt");
    }
}