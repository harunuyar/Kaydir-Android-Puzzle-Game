package com.harunuyar.kaydir.Tools;

/**
 * Created by Harun on 8.02.2017.
 */

public class Record implements Comparable{
    private int row, column, score;

    public Record(int row, int column, int score){
        this.column = column;
        this.row = row;
        this.score = score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public int getScore(){
        return score;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Record){
            Record r = (Record) obj;
            if ((r.getRow() == getRow() && r.getColumn() == getColumn() || (r.getRow() == getColumn() && r.getColumn() == getRow()))){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Record){
            Record r = (Record) o;
            return getScore() - r.getScore();
        }
        return 0;
    }
}
