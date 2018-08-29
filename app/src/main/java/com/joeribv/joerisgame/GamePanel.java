package com.joeribv.joerisgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    private MainThread thread;
    private int score = 1;
    private int color_p = Color.RED;
    private int color_f = Color.BLUE;
    private PlayerLoc player;
    private Point playerPoint;
    private PlayerLoc finish_p;
    private Point finishPoint;
    private float radius = 10;
    private float radius_f = 50;

    private Obstacles obstacle;

    private OrientationData orientationData;
    private long frameTime;
    private boolean gameFinished = false; // check collision
    private long gameFinishTime;

    private ObstacleMover obstacleMover;
    public GamePanel(Context context){
        super(context);
        Constants.CURRENT_CONTEXT = context;
        /* initiate sensors */
        orientationData = new OrientationData();
        orientationData.register();
        /* generate player */
        player = new PlayerLoc(100,200,radius,color_p);
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGTH/4);
        /* generate finish point */
        finish_p = new PlayerLoc(100,200,radius_f,color_f);
        finishPoint = new Point(Constants.SCREEN_WIDTH/2,Constants.SCREEN_HEIGTH/4);
        /* start game thread */
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);
        frameTime = System.currentTimeMillis();
        setFocusable(true);
        /* generate test obstace */
        //obstacle = new Obstacles(100,300,Color.BLACK,300 ,600);
        obstacleMover = new ObstacleMover(50,200,score);
    }
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int heigth){

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        thread = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        thread.setRunning(true);
        thread.start();
    }
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch(Exception e) {e.printStackTrace();}
            retry = false;
        }
    }

    public void update() {
        if(!gameFinished) {
            if (frameTime < Constants.INIT_TIME) {
                frameTime = Constants.INIT_TIME;
            }
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];
                float xSpeed = roll * Constants.SCREEN_WIDTH / 1000f; // add scaling
                float ySpeed = pitch * Constants.SCREEN_HEIGTH / 1000f; // add scaling
                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }
            if (playerPoint.x < radius) {
                playerPoint.x = (int) radius;
            } else if (playerPoint.x > Constants.SCREEN_WIDTH) {
                playerPoint.x = Constants.SCREEN_WIDTH - (int) radius;
            }
            if (playerPoint.y < radius) {
                playerPoint.y = (int) radius;
            } else if (playerPoint.y > Constants.SCREEN_HEIGTH) {
                playerPoint.y = Constants.SCREEN_HEIGTH - (int) radius;
            }
            player.update(playerPoint);
            finish_p.update(finishPoint);
            obstacleMover.update();
            if (player.playerFinished(player, finish_p)) {
                score ++;
                obstacleMover.setScore((int)(1+score/5));
                finishPoint = new Point((int)(Math.random()*Constants.SCREEN_WIDTH-radius),(int)(Math.random()*Constants.SCREEN_HEIGTH-radius));
                //gameFinishTime = System.currentTimeMillis();
                //gameFinished = true;
            }
        }
    }
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        finish_p.draw(canvas);
        obstacleMover.draw(canvas);
        if(gameFinished){
            // print some text (time/level/etc)
            if((System.currentTimeMillis()-gameFinishTime)>=2000){
                gameFinished = false;
                Intent intent = new Intent(Constants.CURRENT_CONTEXT,MainActivity.class);
                Constants.CURRENT_CONTEXT.startActivity(intent);
                // finish activity when finished reached (change to when object collided or time up)
                ((Activity) Constants.CURRENT_CONTEXT).finish();
            }
        }
    }
    public void reset(){
        // playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGTH/4);
        // player.update(playerPoint);
    }
}
