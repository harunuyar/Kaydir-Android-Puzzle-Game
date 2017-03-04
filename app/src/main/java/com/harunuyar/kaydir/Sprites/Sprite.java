package com.harunuyar.kaydir.Sprites;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import com.harunuyar.kaydir.Tools.Vector;

/**
 * Created by Harun on 5.02.2017.
 */

public abstract class Sprite {

    protected Bitmap bitmap;
    protected Vector position, size;

    protected Sprite (Bitmap bitmap){
        this.bitmap = bitmap;
        this.position = new Vector(0,0);
        this.size = new Vector(bitmap.getWidth(), bitmap.getHeight());
    }

    public Sprite(Bitmap bitmap, int x, int y){
        this(bitmap);
        this.position = new Vector(x,y);
        this.size = new Vector(bitmap.getWidth(), bitmap.getHeight());
    }

    public Sprite(Bitmap bitmap, int x, int y, int width, int height){
        this(bitmap, x, y);
        this.size = new Vector(width, height);
    }

    public Sprite(Bitmap bitmap, Vector position){
        this(bitmap);
        this.position = position;
        this.size = new Vector(bitmap.getWidth(), bitmap.getHeight());
    }

    public Sprite(Bitmap bitmap, Vector position, Vector size){
        this(bitmap, position);
        this.size = size;
    }

    abstract public void update();

    abstract public void draw(Canvas canvas);

    abstract public void dispose();

    public int getX(){
        return position.getX();
    }

    public int getY(){
        return position.getY();
    }

    public int getHeight(){
        return size.getY();
    }

    public int getWidth(){
        return size.getX();
    }

    public int getRight(){
        return position.getX() + size.getX();
    }

    public int getBottom(){
        return position.getY() + size.getY();
    }

    public int getLeft(){
        return getX();
    }

    public int getTop() {
        return getY();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setHeight(int height) {
        size.setY(height);
    }

    public void setWidth(int width) {
        size.setX(width);
    }

    public Vector getPosition() {
        return position;
    }

    public Vector getSize() {
        return size;
    }

    public void setPosition(Vector position){
        this.position = position;
    }

    public void setSize(Vector size){
        this.size = size;
    }

    public void setX(int x) {
        position.setX(x);
    }

    public void setY(int y) {
        position.setY(y);
    }

    public boolean contains(int x, int y){
        Rect rect = new Rect(position.getX(), position.getY(), position.getX() + size.getX(), position.getY() + size.getY());
        return rect.contains(x,y);
    }


}
