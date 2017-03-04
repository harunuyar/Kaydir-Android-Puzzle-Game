package com.harunuyar.kaydir;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.harunuyar.kaydir.States.GameState;
import com.harunuyar.kaydir.States.GameStateManager;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Harun on 5.02.2017.
 */

public class GameSurfaceView extends SurfaceView implements Runnable {

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
                }
                Canvas canvas = getHolder().lockCanvas();
                draw(canvas);
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    public void draw(Canvas canvas){
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
        while(true) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            break;
        }
    }

    public void onDestroy(){
        while (!gsm.isEmpty()){
            gsm.popState();
        }
    }
}
