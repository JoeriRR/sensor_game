package com.joeribv.joerisgame;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Iterator;

public class ObstacleMover {
    private ArrayList<Obstacles> obstacles;
    private long startTime;
    private long initTime;
    private int obstaceWidth;
    private int obstacleHeigth;
    private int score;
    private int index;
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
            obstacles.add(new Obstacles(obstaceWidth,obstacleHeigth, Color.BLACK,Math.round((float)((Math.random()*3)+1)))); // Spawn at random side
        }
    }
    public boolean playerCollide(PlayerLoc player){
        index = 0;
        for(Obstacles ob :obstacles){
            if(ob.playerCollide(player)) {
                return true;
            }
            index++;
        }
        return false;
    }
    public void remove(){
        obstacles.remove(index);
    }
    public void update(){
        if(startTime<Constants.INIT_TIME){
            startTime = Constants.INIT_TIME;
        }
        int elapsedTime = (int)(System.currentTimeMillis()-startTime);
        startTime = System.currentTimeMillis();
        float speed = (float)(Math.sqrt(1+(startTime-initTime)/2000)*Constants.SCREEN_HEIGTH/10000.0f); // add difficulty scaling.
        // add new obstacle each 5 points gained (see variable spawn_update) score here is (1+game_score/spawn_update) --> spawn_update = 5
        if(obstacles.size()<score){
            obstacles.add(new Obstacles(obstaceWidth,obstacleHeigth, Color.BLACK,Math.round((float)((Math.random()*3)+1))));
        }
        // remove objects if they are offscreen
        for(Iterator<Obstacles> it = obstacles.iterator(); it.hasNext();){
            Obstacles ob = it.next();
            int mode = ob.getMode();
            switch(mode) {
                case 1:
                    if (ob.getRectangle().top >= Constants.SCREEN_HEIGTH) {
                        it.remove();
                    }
                    ob.moveRect(speed * elapsedTime, mode);
                    break;
                case 2:
                    if (ob.getRectangle().bottom <= 0) {
                        it.remove();
                    }
                    ob.moveRect(speed * elapsedTime, mode);
                    break;
                case 3:
                    if (ob.getRectangle().left >= Constants.SCREEN_WIDTH) {
                        it.remove();
                    }
                    ob.moveRect(speed * elapsedTime, mode);
                    break;
                case 4:
                    if (ob.getRectangle().right <= 0) {
                        it.remove();
                    }
                    ob.moveRect(speed * elapsedTime, mode);
                    break;
            }
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
