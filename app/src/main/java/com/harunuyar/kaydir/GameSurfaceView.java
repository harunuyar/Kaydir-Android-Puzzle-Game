package com.harunuyar.kaydir;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.widget.TextView;
import com.harunuyar.kaydir.States.GameState;
import com.harunuyar.kaydir.States.GameStateManager;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harun on 5.02.2017.
 */

public class GameSurfaceView extends SurfaceView implements Runnable {

    private boolean debug = false;

    public GameSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private boolean running;
    private Thread thread;
    private GameStateManager gsm;
    private boolean stateSet;
    private static int satır, sütun;
    private static Bitmap bitmap;

    private int fps;

    private void init(Context context) {
        running = false;
        thread = null;
        gsm = new GameStateManager(context);
        stateSet = false;
        bitmap = Constants.getBitmap();
        satır = Constants.getSatır();
        sütun = Constants.getSütun();
    }

    @Override
    public void run() {
        while (running){
            if (getHolder().getSurface().isValid()) {
                if (!stateSet) {
                    Constants.setWidth(getWidth());
                    gsm.pushState(new GameState(gsm, this, bitmap, satır, sütun));
                    stateSet = true;

                    if(debug) {
                        final TextView textViewFps = (TextView) gsm.getActivity().findViewById(R.id.textViewFps);
                        gsm.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textViewFps.setVisibility(VISIBLE);
                                fps = 0;
                            }
                        });
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                gsm.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textViewFps.setText(fps + " fps");
                                        fps = 0;
                                    }
                                });
                            }
                        }, 0, 1000);
                    }
                }
                Canvas canvas = getHolder().lockCanvas();
                draw(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    public void draw(Canvas canvas){
        super.draw(canvas);
        if (debug)
            fps++;
        canvas.drawColor(ContextCompat.getColor(gsm.getContext(), R.color.colorSecondary));
        gsm.draw(canvas);
    }

    public void onResume(final Activity activity){
        running = true;
        gsm.setActivity(activity);
        gsm.onResume();
        thread = new Thread(this);
        thread.start();
    }

    public void onPause(){
        running = false;
        gsm.onPause();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        while (!gsm.isEmpty()){
            gsm.popState();
        }
    }
}
