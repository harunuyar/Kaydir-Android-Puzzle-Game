package com.harunuyar.kaydir;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    private ImageButton buttonOyna, buttonSes;
    private boolean sesAçık;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //permission();

        sesAçık = Constants.isSesAçık(this);

        buttonOyna = (ImageButton) findViewById(R.id.buttonOyna);
        buttonSes = (ImageButton) findViewById(R.id.buttonSes);

        final Bitmap sesiAç = BitmapFactory.decodeResource(getResources(), R.drawable.sesiac);
        final Bitmap sesiKapat = BitmapFactory.decodeResource(getResources(), R.drawable.sesikapat);

        buttonSes.setImageBitmap(sesAçık ? sesiKapat : sesiAç);

        buttonOyna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.playSound(Constants.BUTTON_SOUND, MainMenuActivity.this);
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        buttonSes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sesAçık = !sesAçık;
                buttonSes.setImageBitmap(sesAçık ? sesiKapat : sesiAç);
                Constants.setSesAçık(sesAçık, MainMenuActivity.this);
                Constants.playSound(Constants.BUTTON_SOUND, MainMenuActivity.this);
            }
        });

        Constants.loadSounds(this);
    }


}
