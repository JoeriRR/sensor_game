package com.joeribv.joerisgame;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

public class ObstacleMover {
    private ArrayList<Obstacles> obstacles;
    private long startTime;
    private long initTime;
    private int obstaceWidth;
    private int obstacleHeigth;
    private int score;
    public ObstacleMover(int obstacleWidth, int obstacleHeigth,int score){
        this.score = score;
        this.obstaceWidth = obstacleWidth;
        this.obstacleHeigth = obstacleHeigth;
        startTime = initTime = System.currentTimeMillis();
        obstacles = new ArrayList<>();
        populate();
    }
    public void populate(){
        while(obstacles.size()<score){
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-obstaceWidth/2));
            obstacles.add(new Obstacles(obstacleHeigth,obstaceWidth, Color.BLACK,xStart,0));
        }
    }
    public boolean playerCollide(PlayerLoc player){
        for(Obstacles ob :obstacles){
            if(ob.playerCollide(player)) {
                return true;
            }
        }
        return false;
    }

    public void update(){
        if(startTime<Constants.INIT_TIME){
            startTime = Constants.INIT_TIME;
        }
        int elapsedTime = (int)(System.currentTimeMillis()-startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1+(startTime-initTime)/2000)*Constants.SCREEN_HEIGTH/10000.0f);
        int index = 0;
        //System.out.println(score);
        for(Obstacles ob: obstacles) {
            ob.moveRect((speed * elapsedTime));
            if(ob.getRectangle().top>=Constants.SCREEN_HEIGTH){
                obstacles.remove(index);
                int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-obstaceWidth/2));
                obstacles.add(new Obstacles(obstacleHeigth,obstaceWidth, Color.BLACK,xStart,0));
                index++;
            }
        }
        if(obstacles.size()<score){
            int xStart = (int)(Math.random()*(Constants.SCREEN_WIDTH-obstaceWidth/2));
            obstacles.add(new Obstacles(obstacleHeigth,obstaceWidth, Color.BLACK,xStart,0));
        }
    }
    public void draw(Canvas canvas){
        for(Obstacles ob: obstacles) {
            ob.draw(canvas);
        }
    }
    public void setScore(int Curscore){
        score = Curscore;
    }
}
