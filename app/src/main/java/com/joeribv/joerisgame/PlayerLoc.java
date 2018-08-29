package com.joeribv.joerisgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import static java.lang.Math.abs;

public class PlayerLoc implements GameObject {
    private int color;
    private float xPos;
    private float yPos;

    private float radius;
    public PlayerLoc(int xPos,int yPos, float radius, int color){
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
        this.radius = radius;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(xPos,yPos,radius,paint);

    }
    @Override
    public void update(){
        update();
    }
    public void update(Point point) {
        xPos = point.x;
        yPos = point.y;
    }
    public float getxPos(){
        return xPos;
    }
    public float getyPos(){
        return yPos;
    }
    public float getRadius(){
        return radius;
    }
    public boolean playerFinished(PlayerLoc playerloc, PlayerLoc finishPoint){
        float dist_x, dist_y;
        double r;
        dist_x = abs(playerloc.xPos-finishPoint.xPos);
        dist_y = abs(playerloc.yPos-finishPoint.yPos);
        r = Math.sqrt(Math.pow(dist_x,2)+Math.pow(dist_y,2));
        // if they are closer to each other than both radiusses added they must have collided.
        if(r<=(playerloc.radius+finishPoint.radius)){
            return true;
        }else{
            return false;
        }
    }
}
