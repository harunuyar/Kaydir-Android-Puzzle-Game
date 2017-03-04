package com.harunuyar.kaydir.States;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.harunuyar.kaydir.Constants;
import com.harunuyar.kaydir.GameActivity;
import com.harunuyar.kaydir.R;
import com.harunuyar.kaydir.Tools.Record;
import com.harunuyar.kaydir.Sprites.Block;
import com.harunuyar.kaydir.Tools.Vector;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.harunuyar.kaydir.Tools.Counter;

/**
 * Created by Harun on 5.02.2017.
 */

public class GameState extends State {

    private Block[][] blocks;
    private Block emptyBlock, çerçeveSol, çerçeveÜst, çerçeveSağ, çerçeveAlt;
    private Vector v1, v2, p1, p2, previous;
    private boolean busy, finished, started, emptySelected;
    private TextView textView;
    private int autoMixCount, k, defaultSpeed, mixSpeed, space, scaledWidth, scaledHeight, startX, startY, emptyX, emptyY, karıştırmaSayısı;
    private ImageButton buttonKarıştır, buttonSıfırla;
    private Bitmap bitmapBlock, beyazNokta, bitmapRemoved;
    private ProgressBar progressBar;
    private Counter counter;
    private Timer timer;

    public GameState(GameStateManager gsm, SurfaceView surfaceView, Bitmap bitmapBlock, int rowCount, int columnCount) {
        super(gsm, surfaceView);
        this.bitmapBlock = bitmapBlock;

        beyazNokta = BitmapFactory.decodeResource(surfaceView.getResources(), R.drawable.nokta);
        buttonKarıştır = (ImageButton) gsm.getActivity().findViewById(R.id.buttonKarıştır);
        buttonSıfırla = (ImageButton) gsm.getActivity().findViewById(R.id.buttonSıfırla);
        progressBar = (ProgressBar) gsm.getActivity().findViewById(R.id.progressBar);

        counter = new Counter(Constants.getBest(rowCount, columnCount));

        emptySelected = false;
        emptyX = -1;
        emptyY = -1;

        textView = (TextView) gsm.getActivity().findViewById(R.id.bitti);
        blocks = new Block[columnCount][rowCount];
        k = Constants.getScaledValue(25);
        space = k/(int)Math.sqrt(rowCount * columnCount) + 1;
        karıştırmaSayısı = blocks.length * blocks[0].length * 3 - 1;

        float bitmapRatio = (float)bitmapBlock.getHeight()/bitmapBlock.getWidth();
        float surfaceRatio = (float)surfaceView.getHeight()/surfaceView.getWidth();

        if(bitmapRatio < surfaceRatio) {
            scaledWidth = (surfaceView.getWidth() - (blocks.length + 1) * space) / blocks.length;
            scaledHeight = (int) ((bitmapRatio * scaledWidth * blocks.length) / (blocks[0].length));
        }
        else{
            scaledHeight = (surfaceView.getHeight() - (blocks[0].length + 1) * space) / blocks[0].length;
            scaledWidth = (int) ((scaledHeight * blocks[0].length)/(bitmapRatio * blocks.length));
        }

        startX = (surfaceView.getWidth() - (blocks.length * scaledWidth + (blocks.length - 1) * space)) / 2;
        startY = (surfaceView.getHeight() - (blocks[0].length * scaledHeight + (blocks[0].length - 1) * space)) / 2;
        defaultSpeed = 2*k;
        mixSpeed = defaultSpeed;

        Bitmap renkliBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        renkliBitmap.eraseColor(ContextCompat.getColor(gsm.getContext(), R.color.colorPrimaryLight));

        çerçeveSol = new Block(renkliBitmap, 0, 0, startX - space, surfaceView.getHeight());
        çerçeveÜst = new Block(renkliBitmap, 0, 0, surfaceView.getWidth(), startY - space);

        int sağX = startX + blocks.length * (scaledWidth + space);
        int altY = startY + blocks[0].length * (scaledHeight + space);

        çerçeveSağ = new Block(renkliBitmap, sağX, 0, surfaceView.getWidth() - sağX, surfaceView.getHeight());
        çerçeveAlt = new Block(renkliBitmap, 0, altY, surfaceView.getWidth(), surfaceView.getHeight() - altY);

        gsm.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setVisibility(View.VISIBLE);
                textView.setText("Çıkarmak için bir parça seçin." + "\nEn iyi süre: " + counter.getBestAsString());
                buttonKarıştır.setClickable(false);
                buttonSıfırla.setClickable(false);
                progressBar.setMax(karıştırmaSayısı);
                progressBar.setProgress(0);
                progressBar.setVisibility(View.VISIBLE);
            }
        });

        init();
        initListeners();
    }

    public void init(){
        busy = false;
        finished = true;
        autoMixCount = 0;
        started = false;
        previous = new Vector(-1, -1);
        Block.setSpeed(mixSpeed);

        int width = bitmapBlock.getWidth();
        int height = bitmapBlock.getHeight();
        for (int i = 0; i < blocks.length; i++){
            for (int j = 0; j < blocks[0].length; j++){
                Bitmap newBitmap;
                if (i == emptyX && j == emptyY) {
                    newBitmap = beyazNokta;
                    bitmapRemoved = Bitmap.createBitmap(bitmapBlock, i * (width / blocks.length), j * (height / blocks[0].length),
                            width / blocks.length, height / blocks[0].length);
                }
                else {
                    newBitmap = Bitmap.createBitmap(bitmapBlock, i * (width / blocks.length), j * (height / blocks[0].length),
                            width / blocks.length, height / blocks[0].length);
                }
                blocks[i][j] = new Block(newBitmap, startX + i * (scaledWidth + space), startY + j * (scaledHeight + space), scaledWidth, scaledHeight);
                blocks[i][j].setNumber(i * blocks[0].length + j);
            }
        }

        if (emptySelected) {
            emptyBlock = blocks[emptyX][emptyY];
            emptyBlock.setEmpty(true);
        }

        v1 = new Vector(-1,-1);
        v2 = new Vector(-1,-1);
        p1 = new Vector(0,0);
        p2 = new Vector(0,0);

        if (emptySelected) {
            gsm.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    buttonSıfırla.setClickable(false);
                    progressBar.setProgress(0);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Başlamak için karıştırın." + "\nEn iyi süre: " + counter.getBestAsString());
                }
            });
        }
    }

    private void makePictureWhole() {

        emptyBlock.setBitmap(bitmapRemoved);
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Block.setSpeed(1);

                int startX = GameState.this.startX + ((blocks.length - 1) * space)/2;
                int startY = GameState.this.startY + ((blocks[0].length - 1) * space)/2;

                for (int i = 0; i < blocks.length; i++){
                    for (int j = 0; j < blocks[0].length; j++){
                        int x = startX + i * scaledWidth;
                        int y = startY + j * scaledHeight;
                        blocks[i][j].setDestination(new Vector(x, y));
                    }
                }
                gsm.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonSıfırla.setClickable(true);
                    }
                });
                return null;
            }
        };
        task.execute();
    }

    public  void initListeners(){
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!busy && started) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            p1.setXY((int) event.getX(), (int) event.getY());
                            break;
                        case MotionEvent.ACTION_UP:
                            p2.setXY((int) event.getX(), (int) event.getY());
                            pointChange(p1, p2);
                            break;
                    }
                }
                if (!emptySelected){
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            for (int i = 0; i < blocks.length; i++) {
                                for (int j = 0; j < blocks[0].length; j++) {
                                    if (blocks[i][j].contains((int) event.getX(), (int) event.getY())) {
                                        emptyX = i;
                                        emptyY = j;
                                        emptySelected = true;
                                        emptyBlock = blocks[emptyX][emptyY];
                                        emptyBlock.setEmpty(true);
                                        emptyBlock.setBitmap(beyazNokta);
                                        gsm.getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                textView.setVisibility(View.VISIBLE);
                                                textView.setText("Başlamak için karıştırın." + "\nEn iyi süre: " + counter.getBestAsString());
                                                buttonKarıştır.setClickable(true);
                                                buttonSıfırla.setClickable(true);
                                            }
                                        });
                                        init();
                                        return true;
                                    }
                                }
                            }
                            break;
                    }
                }
                return true;
            }
        });

        buttonKarıştır.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptySelected) {
                    buttonSıfırla.setClickable(false);
                    if(timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    textView.setText("Karıştırılıyor...");
                    started = true;
                    finished = false;
                    autoMixCount = karıştırmaSayısı;
                    buttonKarıştır.setClickable(false);
                }
            }
        });

        buttonSıfırla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emptySelected) {
                    if(timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    finished = true;
                    progressBar.setProgress(0);
                    init();
                    buttonKarıştır.setClickable(true);
                }
                Constants.playSound(Constants.BUTTON_SOUND, gsm.getActivity());
            }
        });
    }

    public void karıştır(){
        p2.setXY(emptyBlock.getX() + 1, emptyBlock.getY() + 1);
        if(!busy && autoMixCount > 0){
            autoMixCount--;
            Random r = new Random();
            int i, j;
            Vector now;
            do {
                i = r.nextInt(3) - 1;
                if (i == 0) {
                    if (r.nextBoolean()) {
                        j = 1;
                    } else {
                        j = -1;
                    }
                } else {
                    j = 0;
                }
                p1.setXY(p2.getX() + i * (space + scaledWidth), p2.getY() + j * (space + scaledHeight));
                now = new Vector(i, j);
            }while (invalidTouch(p1) || now.equals(previous));
            previous = new Vector(now.getX() * -1, now.getY() * -1);
            pointChange(p1, p2);
            progressBar.setProgress(karıştırmaSayısı - autoMixCount);
            if (autoMixCount == 0){
                gsm.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonKarıştır.setClickable(true);
                        buttonSıfırla.setClickable(true);
                        Block.setSpeed(defaultSpeed);

                        counter.setCount(0);
                        GameState.this.gsm.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(counter.toString() + "\nEn iyi süre: " + counter.getBestAsString());
                            }
                        });

                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                counter.increase();
                                GameState.this.gsm.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        textView.setText(counter.toString() + "\nEn iyi süre: " + counter.getBestAsString());
                                    }
                                });
                            }
                        }, 1000, 1000);
                    }
                });
            }
        }
    }

    private boolean invalidTouch(Vector p1) {
        for (Block[] bs : blocks){
            for(Block b : bs){
                if (b.contains(p1.getX(), p1.getY())){
                    return false;
                }
            }
        }
        return true;
    }

    private void setNotBusyIfFinished(){
        try {
            if (busy && blocks[v1.getX()][v1.getY()].getDestination() == null && blocks[v2.getX()][v2.getY()].getDestination() == null) {
                //Block.setSpeed(defaultSpeed);
                busy = false;
                v1 = new Vector(-1, -1);
                v2 = new Vector(-1, -1);
            }
        }
        catch (Exception ex){}
    }

    private void pointChange(Vector p1, Vector p2) {
        try {
            Vector direction;
            if (Math.abs(p1.getY() - p2.getY()) > Math.abs(p1.getX() - p2.getX())) {
                if (p1.getY() - p2.getY() < 0) {
                    direction = new Vector(0, 1);
                } else {
                    direction = new Vector(0, -1);
                }
            } else {
                if (p1.getX() - p2.getX() < 0) {
                    direction = new Vector(1, 0);
                } else {
                    direction = new Vector(-1, 0);
                }
            }

            Block b1 = null, b2 = null;

            for (int i = 0; i < blocks.length; i++) {
                for (int j = 0; j < blocks[0].length; j++) {
                    if (blocks[i][j].contains(p1.getX(), p1.getY())) {
                        b1 = blocks[i][j];
                        b2 = blocks[i + direction.getX()][j + direction.getY()];
                        v1 = new Vector(i,j);
                        v2 = new Vector(i + direction.getX(), j + direction.getY());
                    }
                }
            }

            if (!b2.isEmpty())
                return;

            b1.setDestination(b2.getPosition().clone());
            b2.setDestination(b1.getPosition().clone());

            blocks[v1.getX()][v1.getY()] = b2;
            blocks[v2.getX()][v2.getY()] = b1;

            Constants.playSound(Constants.TICK_SOUND, gsm.getActivity());

            busy = true;
        }
        catch (Exception ex){}
    }

    private boolean finished() {
        if (busy || autoMixCount > 0){
            return false;
        }
        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks[0].length; j++){
                if (blocks[i][j].getNumber() != i*blocks[0].length+j){
                    return false;
                }
            }
        }
        return true;
    }

    private void performIfFinished(){
        if (finished()){
            started = false;
            if (!finished) {
                if(timer != null) {
                    timer.cancel();
                    timer = null;
                }
                finished = true;

                Constants.playSound(Constants.WIN_SOUND, gsm.getActivity());

                makePictureWhole();

                gsm.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buttonKarıştır.setClickable(false);
                        buttonSıfırla.setClickable(false);
                        textView.setVisibility(View.VISIBLE);
                        if (counter.getBest() > counter.getCount()){
                            counter.setBest(counter.getCount());
                            Constants.setBest(new Record(blocks[0].length, blocks.length, counter.getBest()), gsm.getActivity());
                            textView.setText("En iyi süre rekorunuzu kırdınız!\n" + counter.getBestAsString());
                        }
                        else {
                            textView.setText("Tebrikler! Süreniz: " + counter.toString() + "\nEn iyi süre: " + counter.getBestAsString());
                        }

                    }
                });
            }
        }
        else if (finished){
            finished = false;
            gsm.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            if (çerçeveSol.getWidth() != 0) {
                çerçeveSağ.draw(canvas);
                çerçeveSol.draw(canvas);
            }
            else {
                çerçeveÜst.draw(canvas);
                çerçeveAlt.draw(canvas);
            }
            if (emptySelected) {
                emptyBlock.draw(canvas);
            }
            for (Block[] bl : blocks) {
                for (Block b : bl) {
                    if (!b.isEmpty())
                        b.draw(canvas);
                }
            }
        }
        catch (Exception ex){}
    }

    @Override
    public void update() {
        try {
            if (started) {
                setNotBusyIfFinished();
                performIfFinished();

                karıştır();
            }

            for (Block[] bl : blocks) {
                for (Block b : bl) {
                    b.update();
                }
            }
        }
        catch (Exception ex){}
    }

    @Override
    public void dispose() {
        try {
            if(timer != null) {
                timer.cancel();
                timer = null;
            }

            çerçeveSol.dispose();
            çerçeveAlt.dispose();
            çerçeveÜst.dispose();
            çerçeveSağ.dispose();
            for (Block[] bl : blocks) {
                for (Block b : bl) {
                    b.dispose();
                }
            }
        }
        catch (Exception ex){}
    }

    @Override
    public void onResume() {
        if (timer != null){
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    counter.increase();
                    GameState.this.gsm.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(counter.toString() + "\nEn iyi süre: " + counter.getBestAsString());
                        }
                    });
                }
            }, 1000, 1000);
        }
    }

    @Override
    public void onPause() {
        if (timer != null){
            timer.cancel();
        }
    }
}
