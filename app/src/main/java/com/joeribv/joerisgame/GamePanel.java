package com.joeribv.joerisgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback{
    /* sound variables */
    MediaPlayer myMusic;
    MediaPlayer myFail;

    /* thread variables*/
    private MainThread thread;

    /* player variables */
    private int color_p = Color.RED;
    private int color_f = Color.BLUE;
    private int color_shield = Color.YELLOW;
    private PlayerLoc player;
    private Point playerPoint;
    private PlayerLoc finish_p;
    private Point finishPoint;
    private PictureLoc shield;
    private Point shield_p;
    private float radius = (int)(Constants.SCREEN_WIDTH/108.0);
    private float radius_f = (int)(Constants.SCREEN_WIDTH/21.6);
    private float radius_c = (int)(Constants.SCREEN_WIDTH/21.6);
    private float shield_r = (int)(Constants.SCREEN_WIDTH/59);;
    private BitmapFactory bf;
    /* sensor variables */
    private OrientationData orientationData;

    /* score variables */
    private long frameTime;
    private boolean gameFinished = false; // check collision
    private long gameFinishTime;
    private int shield_timer;
    private int score = 1;
    private String difficulty;

    /* animation variables */
    /* get difficulty from mainactivity*/
    private ObstacleMover obstacleMover;
    private int obstacleWidth = (int)(Constants.SCREEN_WIDTH/21.6); // scale with screenwidth
    private int obstacleHeigth = (int)(Constants.SCREEN_HEIGTH/9.6); // scale with screenwidth
    private int spawn_update = 5;
    private int[] x = new int[4];
    private float speed_scaling = 1;
    DataBaseManager dataBaseManager;

    public GamePanel(Context context){
        super(context);
        Bundle level = ((Activity) context).getIntent().getExtras();
        difficulty = level.getString("difficulty");
        SetLevelVar(difficulty);
        Constants.CURRENT_CONTEXT = context;
        x[0] = obstacleWidth; x[1]= Constants.SCREEN_WIDTH-x[0]; x[2] = obstacleWidth; x[3]=Constants.SCREEN_HEIGTH-x[2];
        /* initiate sensors */
        orientationData = new OrientationData();
        orientationData.register();
        /* generate player */
        bf = new BitmapFactory();
        Bitmap shield_pic = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(),R.drawable.picture_shield);
        player = new PlayerLoc(Constants.SCREEN_WIDTH/2,Constants.SCREEN_HEIGTH/2,radius,color_p);
        playerPoint = new Point(Constants.SCREEN_WIDTH/2,3*Constants.SCREEN_HEIGTH/4);
        /* generate finish point */
        finish_p = new PlayerLoc(Constants.SCREEN_WIDTH/2,Constants.SCREEN_HEIGTH/4,radius_f,color_f);
        finishPoint = new Point((int)(x[0]+radius_f+(Math.random()*(((x[1]-radius_f)-(x[0]+radius_f))+1))),(int)(x[2]+radius_f+(Math.random()*((x[3]-radius_f)-(x[2]+radius_f))+1)));
        /* generate shield point*/
        shield = new PictureLoc((int)(x[0]+radius_f+(Math.random()*(((x[1]-radius_f)-(x[0]+radius_f))+1))),(int)(x[2]+radius_f+(Math.random()*((x[3]-radius_f)-(x[2]+radius_f))+1)),radius_c,color_shield,shield_pic);
        shield_p = new Point((int)(x[0]+radius_f+(Math.random()*(((x[1]-radius_f)-(x[0]+radius_f))+1))),(int)(x[2]+radius_f+(Math.random()*((x[3]-radius_f)-(x[2]+radius_f))+1)));

        shield_timer = (int)(Math.random()*4+8);
        /* start game thread */
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(),this);
        frameTime = System.currentTimeMillis();
        setFocusable(true);
        dataBaseManager = new DataBaseManager();


        /* obstacle generator */ // scale obstacleheigth percentage.
        obstacleMover = new ObstacleMover(obstacleWidth,obstacleHeigth,score,speed_scaling);
        myMusic = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.impossible);
        myFail = MediaPlayer.create(Constants.CURRENT_CONTEXT,R.raw.failsound);
        myMusic.start();
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
                float xSpeed = roll * Constants.SCREEN_WIDTH / 1000f*(Constants.X_SENS+10)/50;
                float ySpeed = pitch * Constants.SCREEN_HEIGTH / 1000f*(Constants.Y_SENS+10)/50;
                playerPoint.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime : 0;
            }
            if (playerPoint.x < (radius+x[0])) {
                playerPoint.x = (int) (radius+x[0]);
            } else if (playerPoint.x > (x[1]-radius)) {
                playerPoint.x = (int)(x[1]-radius);
            }
            if (playerPoint.y < (radius+x[2])) {
                playerPoint.y = (int) (radius+x[2]);
            } else if (playerPoint.y > (x[3]-radius)) {
                playerPoint.y = (int) (x[3]-radius);
            }
            obstacleMover.update();
            player.update(playerPoint);
            if(obstacleMover.playerCollide(player)){
                if(player.getRadius()==(radius+shield_r)){
                    player.setRadius(radius);
                    player.setColor(Color.RED);
                    obstacleMover.remove();
                }else {
                    gameFinished = true;
                    gameFinishTime = System.currentTimeMillis();
                    dataBaseManager.updateDatabase(score-1,difficulty); // score starts on 1
                    myMusic.stop();
                    myFail.start();
                }
            }
            if (player.playerFinished(player, finish_p)) {
                score ++;
                obstacleMover.setScore(score/spawn_update);
                finishPoint = new Point((int)(x[0]+radius_f+(Math.random()*(((x[1]-radius_f)-(x[0]+radius_f))+1))),(int)(x[2]+radius_f+(Math.random()*((x[3]-radius_f)-(x[2]+radius_f))+1)));
                finish_p.update(finishPoint);
            }
            if(score>=shield_timer) {
                if (shield.playerFinished(player, shield)) {
                    player.setRadius(radius + shield_r);
                    player.setColor(Color.YELLOW);
                    shield.update(shield_p);
                    shield_timer = shield_timer + (int)(Math.random()*4+8);
                }
            }
        }
    }
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        player.draw(canvas);
        finish_p.draw(canvas);
        drawField(canvas,x);
        Paint text_paint = new Paint();
        text_paint.setTextSize(40);
        text_paint.setColor(Color.BLUE);
        canvas.drawText("Score= " +(score-1),40,40,text_paint);
        if(score>=shield_timer) {
            shield.draw(canvas);
        }
        obstacleMover.draw(canvas);
        if(gameFinished){
            if((System.currentTimeMillis()-gameFinishTime)>=4000){
                myFail.stop();
                ((Activity) Constants.CURRENT_CONTEXT).finish();
            }
        }
    }
    public void reset(){
        myMusic.stop();
        myFail.stop();
    }
    private void drawField(Canvas canvas,int[] x){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        Path path = new Path();
        path.moveTo(x[0],x[2]);
        path.lineTo(x[0],x[3]);
        path.lineTo(x[1],x[3]);
        path.lineTo(x[1],x[2]);
        path.lineTo(x[0],x[2]);
        // create dotted line 50/50 dotted
        float[] intervals = new float[]{50.0f, 50.0f};
        float phase = 0;
        DashPathEffect dashPathEffect = new DashPathEffect(intervals, phase);
        paint.setPathEffect(dashPathEffect);
        canvas.drawPath(path,paint);
    }

    private void SetLevelVar(String difficulty){
        switch(difficulty) {
            case "hard":
                speed_scaling = 1;
                spawn_update = 5;
                obstacleWidth = obstacleWidth*1;
                obstacleHeigth = obstacleHeigth*1;
                break;
            case "extreme":
                speed_scaling = (float)1.1;
                spawn_update = 3;
                obstacleWidth = (int)(obstacleWidth*1.3);
                obstacleHeigth = (int)(obstacleHeigth*1.3);
                break;
            case "god":
                speed_scaling = (float)1.2;
                spawn_update = 1;
                obstacleWidth = (int)(obstacleWidth*1.6);
                obstacleHeigth = (int)(obstacleHeigth*1.6);
                break;
        }
    }

}
