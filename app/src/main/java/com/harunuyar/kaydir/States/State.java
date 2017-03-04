package com.harunuyar.kaydir.States;

import android.graphics.Canvas;
import android.view.SurfaceView;

/**
 * Created by Harun on 5.02.2017.
 */

public abstract class State {

    protected SurfaceView surfaceView;
    protected GameStateManager gsm;

    public State(GameStateManager gsm, SurfaceView surfaceView){
        this.gsm = gsm;
        this.surfaceView = surfaceView;
    }

    abstract public void update();

    abstract public void draw(Canvas canvas);

    abstract public void dispose();

    abstract public void onResume();

    abstract public void onPause();
}
