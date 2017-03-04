package com.harunuyar.kaydir;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameActivity extends AppCompatActivity {

    private GameSurfaceView gameSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameSurfaceView = (GameSurfaceView) findViewById(R.id.menusv);
        Constants.readBest(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameSurfaceView.onDestroy();
    }
}
