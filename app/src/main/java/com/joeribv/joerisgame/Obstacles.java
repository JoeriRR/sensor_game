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
    private int mode;
    public void moveRect(float y,int mode){
        switch(mode) {
            case 1:
                rectangle.top += y;
                rectangle.bottom += y;
                break;
            case 2:
                rectangle.top -= y;
                rectangle.bottom -= y;
                break;
            case 3:
                rectangle.left += y;
                rectangle.right += y;
                break;
            case 4:
                rectangle.left -= y;
                rectangle.right -= y;
                break;
        }
    }
    public Rect getRectangle(){
        return rectangle;
    }
    public int getMode(){
        return mode;
    }
    public Obstacles(int rectHeigth,int rectLength,int color, int mode){
        // Heigth = 50 (width), length = 200 (length)
        this.color = color;
        this.mode = mode;
        int startX;
        int startY;
        switch(mode) {
            case 1:
                startX = (int)(Math.random()*(Constants.SCREEN_WIDTH-rectLength));
                rectangle = new Rect(startX, 0, startX+rectLength, rectHeigth);
                break;
            case 2:
                startX = (int)(Math.random()*(Constants.SCREEN_WIDTH-rectLength));
                rectangle = new Rect(startX, Constants.SCREEN_HEIGTH-rectHeigth, startX + rectLength, Constants.SCREEN_HEIGTH);
                break;
            case 3:
                startY = (int)(Math.random()*(Constants.SCREEN_HEIGTH-rectLength));
                rectangle = new Rect(0, startY, rectHeigth, startY + rectLength);
                break;
            case 4:
                startY = (int)(Math.random()*(Constants.SCREEN_HEIGTH-rectLength));
                rectangle = new Rect(Constants.SCREEN_WIDTH-rectHeigth, startY, Constants.SCREEN_WIDTH, startY + rectLength);
                break;
        }
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
        // get absolute distance center rect to center circle
        float dist_x,dist_y;
        dist_x = Math.abs(player.getxPos()-rectangle.centerX());
        dist_y = Math.abs(player.getyPos()-rectangle.centerY());
        // check whether the circle lies within the rectangle
        if(dist_x>(rectangle.width()/2+player.getRadius())){
            return false;
        }
        if(dist_y>(rectangle.height()/2+player.getRadius())){
            return false;
        }
        if(dist_x<=(rectangle.width()/2)){
            return true;
        }
        if(dist_y<=(rectangle.height()/2)){
            return true;
        }
        // detect whether circle touches the edge of the rectangle
        double cornerDist = Math.pow((dist_x-rectangle.width()/2),2)+Math.pow((dist_y-rectangle.height()/2),2);
        return(cornerDist <= Math.pow(player.getRadius(),2));

    }
}
