package com.joeribv.joerisgame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Obstacles implements GameObject {
    private Rect rectangle;
    private int color;
    public void moveRect(float y){
        rectangle.top += y;
        rectangle.bottom += y;
    }
    public Rect getRectangle(){
        return rectangle;
    }

    public Obstacles(int rectHeigth,int rectLength,int color,int startX,int startY){
        this.color = color;
        rectangle = new Rect(startX,startY,startX+rectLength,startY+rectHeigth);
    }
    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle,paint);
    }
    @Override
    public void update(){

    }
    public boolean playerCollide(PlayerLoc player){
        float xPos = player.getxPos();
        float yPos = player.getyPos();
        float radius = player.getRadius();
        return false;
    }
}
