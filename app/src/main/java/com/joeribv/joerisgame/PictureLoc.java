package com.joeribv.joerisgame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;

import static java.lang.Math.abs;

public class PictureLoc implements GameObject {
    private int color;
    private float xPos;
    private float yPos;
    private float radius;
    private Bitmap picture;
    public PictureLoc(int xPos,int yPos, float radius, int color,Bitmap picture){
        this.xPos = xPos;
        this.yPos = yPos;
        this.color = color;
        this.radius = radius;
        this.picture = picture;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        Rect destination = new Rect((int)xPos-picture.getWidth(),(int)yPos-picture.getHeight(),(int)xPos+picture.getWidth(),(int)yPos+picture.getHeight());
        canvas.drawCircle(xPos,yPos,radius,paint);
        Paint pictPaint = new Paint();
        canvas.drawBitmap(picture,null,destination,pictPaint);
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
    public void setRadius(float radius_in){
        radius = radius_in;
    }
    public void setColor(int color_in){
        color = color_in;
    }
    public boolean playerFinished(PlayerLoc playerloc, PictureLoc finishPoint){
        float dist_x, dist_y;
        double r;
        dist_x = abs(playerloc.getxPos()-finishPoint.xPos);
        dist_y = abs(playerloc.getyPos()-finishPoint.yPos);
        r = Math.sqrt(Math.pow(dist_x,2)+Math.pow(dist_y,2));
        // if they are closer to each other than both radiusses added they must have collided.
        if(r<=(playerloc.getRadius()+finishPoint.radius)){
            return true;
        }else{
            return false;
        }
    }
}
