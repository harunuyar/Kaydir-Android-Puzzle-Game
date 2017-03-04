package com.harunuyar.kaydir.Tools;

/**
 * Created by Harun on 6.02.2017.
 */

public class Vector {
    int x;
    int y;

    public Vector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Vector(Vector vector){
        this(vector.getX(), vector.getY());
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void addX(int x){
        this.x += x;
    }

    public void addY(int y){
        this.y += y;
    }

    public void setXY(int x, int y){
        setX(x);
        setY(y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector){
            Vector vector = (Vector) obj;
            if (vector.getY() == getY() && vector.getX() == getX())
                return true;
        }
        return false;
    }

    public double distanceTo(Vector vector){
        return Math.sqrt(
                ( this.getX() - vector.getX() ) * ( this.getX() - vector.getX() ) +
                ( this.getY() - vector.getY() ) * ( this.getY() - vector.getY() )
                );
    }

    public Vector clone(){
        return new Vector(this);
    }
}
