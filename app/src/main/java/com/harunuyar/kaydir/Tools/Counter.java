package com.harunuyar.kaydir.Tools;

/**
 * Created by Harun on 8.02.2017.
 */

public class Counter {

    private int count, best;

    public Counter(int best){
        this.best = best;
        count = 0;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void increase(){
        count++;
    }

    @Override
    public String toString() {
        String s;
        if (count/3600 == 0) {
            s = String.format("%02d:%02d", count / 60, count % 60);
        }
        else {
            s = String.format("%02d:%02d:%02d", count/3600, (count % 3600) / 60, count % 60);
        }
        return s;
    }

    public String getBestAsString() {
        if (best == Integer.MAX_VALUE){
            return "--:--";
        }

        String s;
        if (best/3600 == 0) {
            s = String.format("%02d:%02d", best / 60, best % 60);
        }
        else {
            s = String.format("%02d:%02d:%02d", best/3600, (best % 3600) / 60, best % 60);
        }
        return s;
    }

    public int getBest(){
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }
}
