package com.harunuyar.kaydir;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton buttonPickImage, buttonTakePhoto, buttonVarsayilan, buttonSatırSol,
            buttonSatırSağ, buttonSütunSol, buttonSütunSağ, imageButtonBack;
    private FloatingActionButton floatingActionButton;
    private TextView textViewSatır, textViewSütun;
    private ImageView imageViewSelected;
    private String fileName;
    private int satır, sütun;
    private Bitmap defaultBitmap, usedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wallpaper);
        usedBitmap = defaultBitmap;

        buttonPickImage = (ImageButton) findViewById(R.id.buttonPickImage);
        buttonTakePhoto = (ImageButton) findViewById(R.id.buttonTakePhoto);
        buttonSatırSol = (ImageButton) findViewById(R.id.buttonSatırSol);
        buttonSatırSağ = (ImageButton) findViewById(R.id.buttonSatırSağ);
        buttonSütunSol = (ImageButton) findViewById(R.id.buttonSütunSol);
        buttonSütunSağ = (ImageButton) findViewById(R.id.buttonSütunSağ);
        imageButtonBack = (ImageButton) findViewById(R.id.imageButtonBackSettings);
        textViewSatır = (TextView) findViewById(R.id.textViewSatır);
        textViewSütun = (TextView) findViewById(R.id.textViewSütun);
        buttonVarsayilan = (ImageButton) findViewById(R.id.buttonVarsayilan);
        imageViewSelected = (ImageView) findViewById(R.id.imageViewSelected);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        satır = 4;
        sütun = 4;
        textViewSütun.setText("" + sütun);
        textViewSatır.setText("" + satır);

        imageViewSelected.setImageBitmap(defaultBitmap);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usedBitmap != defaultBitmap){
                    usedBitmap.recycle();
                    usedBitmap = defaultBitmap;
                }
                finish();
            }
        });

        buttonSatırSağ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satır < 9) {
                    satır++;
                    textViewSatır.setText("" + satır);
                }
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);
            }
        });

        buttonSatırSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (satır > 2) {
                    satır--;
                    textViewSatır.setText("" + satır);
                }
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);
            }
        });

        buttonSütunSağ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sütun < 9) {
                    sütun++;
                    textViewSütun.setText("" + sütun);
                }
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);
            }
        });

        buttonSütunSol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sütun > 2) {
                    sütun--;
                    textViewSütun.setText("" + sütun);
                }
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);
            }
        });

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);

                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.GALLERY_REQUEST);
            }
        });

        buttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);

                Uri outputFileUri = null;
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                if (android.os.Build.VERSION.SDK_INT >= 24){
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String imageFileName = "IMG_" + timeStamp;
                    File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File image = null;
                    try {
                        image = File.createTempFile(
                                imageFileName,
                                ".jpg",
                                storageDir
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fileName = image.getAbsolutePath();

                    if (image != null){
                        outputFileUri = FileProvider.getUriForFile(SettingsActivity.this,
                                "com.example.android.fileprovider",
                                image);
                    }
                }
                else {
                    File folder = new File(Constants.getFolder());
                    if (!folder.exists()) {
                        folder.mkdir();
                    }

                    fileName = Constants.getFolder() + "/IMG_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";

                    File file = new File(fileName);
                    outputFileUri = Uri.fromFile(file);
                }

                if (intent.resolveActivity(getPackageManager()) != null) {
                    if (outputFileUri != null) {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                        startActivityForResult(intent, Constants.CAMERA_REQUEST);
                    }
                }

            }
        });

        buttonVarsayilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.playSound(Constants.BUTTON_SOUND, SettingsActivity.this);
                if (usedBitmap != defaultBitmap) {
                    imageViewSelected.setImageBitmap(defaultBitmap);
                    usedBitmap.recycle();
                    usedBitmap = defaultBitmap;
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.playSound(Constants.PLAY_SOUND, SettingsActivity.this);
                Constants.setGameSettings(satır, sütun, usedBitmap);
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        permission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            if (bitmap != null) {
                imageViewSelected.setImageBitmap(bitmap);
                if (usedBitmap != defaultBitmap)
                    usedBitmap.recycle();
                usedBitmap = bitmap;
            }
        }
        else if (requestCode == Constants.CAMERA_REQUEST && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
            if (bitmap != null) {
                imageViewSelected.setImageBitmap(bitmap);
                if (usedBitmap != defaultBitmap)
                    usedBitmap.recycle();
                usedBitmap = bitmap;
            }
        }
    }

    private void permission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (usedBitmap != defaultBitmap)
            defaultBitmap.recycle();
        usedBitmap.recycle();
    }
}
