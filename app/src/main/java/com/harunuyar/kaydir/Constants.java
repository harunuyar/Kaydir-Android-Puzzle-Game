package com.harunuyar.kaydir;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Environment;

import com.harunuyar.kaydir.Csv.CsvReader;
import com.harunuyar.kaydir.Csv.CsvWriter;
import com.harunuyar.kaydir.Tools.Record;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Harun on 5.02.2017.
 */

public abstract class Constants {

    private static int goalWidth = 1080;
    private static String folder = Environment.getExternalStorageDirectory().getAbsolutePath() +"/Pictures/Kaydır";
    public static int CAMERA_REQUEST = 1;
    public static int GALLERY_REQUEST = 2;
    private static String RECORDS_FILE = "records.csv";
    private static String SES_AYARLARI_FILE = "sound.csv";
    private static String SES_AÇIK = "acik";
    private static String SES_KAPALI = "kapali";

    private static int width = 1080;
    private static ArrayList<Record> records;

    public static final int TICK_SOUND = 1;
    public static final int WIN_SOUND = 2;
    public static final int BUTTON_SOUND = 3;
    public static final int PLAY_SOUND = 4;
    private static SoundPool tickSoundPool, winSoundPool, buttonSoundPool, playSoundPool;
    private static int tickSoundID, winSoundID, buttonSoundID, playSoundID;
    private static boolean tickLoaded = false, winLoaded = false, buttonLoaded = false, playLoaded = false;

    private static int satır, sütun;
    private static Bitmap bitmap;

    private static boolean sesAçık, sesLoaded;

    private Constants(){ }

    public static int getScaledValue(int value){
        return (value * width) / goalWidth;
    }

    public static void setWidth(int width){
        Constants.width = width;
    }

    public static String getFolder() {
        return folder;
    }

    public static void setFolder(String folder) {
        Constants.folder = folder;
    }

    public static void readBest(Context context){
        records = new ArrayList<>();
        try {
            CsvReader csvReader = new CsvReader(RECORDS_FILE, context);
            String[] s;
            while ((s=csvReader.readNext()) != null){
                Record r = new Record(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]));
                records.add(r);
            }
        } catch (Exception e) {}
    }

    public static int getBest(int rowCount, int columnCount) {
        if (records != null){
            for (Record r : records){
                if ((r.getRow() == rowCount && r.getColumn() == columnCount) || (r.getRow() == columnCount && r.getColumn() == rowCount)){
                    return r.getScore();
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    public static void setBest(Record record, Context context){
        if (records != null){
            boolean buldu = false, değişti = false;
            for (Record r : records){
                if (record.equals(r)){
                    buldu = true;
                    if (record.compareTo(r) < 0){
                        değişti = true;
                        r.setScore(record.getScore());
                        break;
                    }
                }
            }
            if (!buldu){
                records.add(record);
                değişti = true;
            }
            if (değişti){
                try {
                    CsvWriter csvWriter = new CsvWriter(RECORDS_FILE, context, "row", "column", "score");
                    for (Record r : records){
                        csvWriter.write(r.getRow() + "", r.getColumn() + "", r.getScore() + "");
                    }
                    csvWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void loadSounds(Activity activity) {
        tickLoaded = false;
        winLoaded = false;
        buttonLoaded = false;
        playLoaded = false;
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tickSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
            winSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
            buttonSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
            playSoundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            tickSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            winSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            buttonSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            playSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        tickSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                tickLoaded = true;
            }
        });
        winSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                winLoaded = true;
            }
        });
        buttonSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                buttonLoaded = true;
            }
        });
        playSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                playLoaded = true;
            }
        });

        tickSoundID = tickSoundPool.load(activity, R.raw.tick, 1);
        winSoundID = winSoundPool.load(activity, R.raw.win, 1);
        buttonSoundID = buttonSoundPool.load(activity, R.raw.button, 1);
        playSoundID = playSoundPool.load(activity, R.raw.button2, 1);
    }

    public static void playSound(int SOUND_CODE, Activity activity){
        if (!sesAçık)
            return;
        AudioManager audioManager = (AudioManager) activity.getSystemService(activity.AUDIO_SERVICE);
        float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volume = actualVolume / maxVolume;
        switch (SOUND_CODE) {
            case TICK_SOUND:
                if (tickLoaded) {
                    tickSoundPool.play(tickSoundID, volume, volume, 1, 0, 1f);
                }
                break;
            case WIN_SOUND:
                if (winLoaded) {
                    winSoundPool.play(winSoundID, volume, volume, 1, 0, 1f);
                }
                break;
            case BUTTON_SOUND:
                if (buttonLoaded) {
                    buttonSoundPool.play(buttonSoundID, volume, volume, 1, 0, 1f);
                }
                break;
            case PLAY_SOUND:
                if (playLoaded){
                    playSoundPool.play(playSoundID, volume, volume, 1, 0, 1f);
                }
                break;
        }
    }

    public static void setGameSettings(int satır, int sütun, Bitmap bitmap){
        Constants.bitmap = bitmap;
        Constants.satır = satır;
        Constants.sütun = sütun;
    }

    public static Bitmap getBitmap(){
        return bitmap;
    }

    public static int getSatır(){
        return satır;
    }

    public static int getSütun(){
        return sütun;
    }

    public static boolean isSesAçık(Context context) {
        if (!sesLoaded) {
            try {
                sesLoaded = true;
                sesAçık = true;
                CsvReader csvReader = new CsvReader(SES_AYARLARI_FILE, context);
                String[] s;
                if ((s=csvReader.readNext()) != null){
                    sesAçık = s[0].equals(SES_AÇIK);
                }
            } catch (Exception e) {}
        }
        return sesAçık;
    }

    public static void setSesAçık(boolean sesAçık, Context context){
        Constants.sesAçık = sesAçık;
        try {
            CsvWriter csvWriter = new CsvWriter(SES_AYARLARI_FILE, context, "ses");
            csvWriter.write(sesAçık ? SES_AÇIK : SES_KAPALI);
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
