package com.harunuyar.kaydir.Sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.harunuyar.kaydir.Constants;
import com.harunuyar.kaydir.Tools.Vector;

/**
 * Created by Harun on 5.02.2017.
 */

public class Block extends Sprite {

    private Vector destination = null;
    private Vector direction = null;
    private static int speed = Constants.getScaledValue(25);
    private boolean empty = false;
    private int number;

    public Block(Bitmap bitmap) {
        super(bitmap);
    }

    public Block(Bitmap bitmap, int x, int y){
        super(bitmap, x, y);
    }

    public Block(Bitmap bitmap, int x, int y, int width, int height){
        super(bitmap, x, y, width, height);
    }

    public Block(Bitmap bitmap, Vector position){
        super(bitmap, position);
    }

    public Block(Bitmap bitmap, Vector position, Vector size){
        super(bitmap, position, size);
    }

    @Override
    public void update() {
        if(destination!=null){

            if (position.getY() < destination.getY()){
                position.addY(speed);
            }
            else if (position.getY() > destination.getY()){
                position.addY(-speed);
            }
            if (position.getX() < destination.getX()){
                position.addX(speed);
            }
            else if (position.getX() > destination.getX()){
                position.addX(-speed);
            }

            boolean finished = (direction.getX() == 1 && position.getX() >= destination.getX()) ||
                    (direction.getX() == -1 && position.getX() <= destination.getX()) ||
                    (direction.getY() == 1 && position.getY() >= destination.getY()) ||
                    (direction.getY() == -1 && position.getY() <= destination.getY());
            if (finished){
                position = destination.clone();
                destination = null;
                direction = null;
                return;
            }
        }
    }

    public void setDestination(Vector destination){
        if (destination.equals(position)){
            return;
        }
        this.destination = destination;
        if(position.getY() < destination.getY()){
            direction = new Vector(0,1);
        }
        else if(position.getY() > destination.getY()){
            direction = new Vector(0,-1);
        }
        else if(position.getX() < destination.getX()){
            direction = new Vector(1,0);
        }
        else if(position.getX() > destination.getX()){
            direction = new Vector(-1,0);
        }
    }

    public Vector getDestination(){
        return destination;
    }

    @Override
    public void draw(Canvas canvas) {
        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Rect dst = new Rect(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY());
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }

    public boolean isEmpty(){
        return empty;
    }

    public void setEmpty(boolean empty){
        this.empty = empty;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        Block.speed = speed;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public int getNumber(){
        return this.number;
    }
}
