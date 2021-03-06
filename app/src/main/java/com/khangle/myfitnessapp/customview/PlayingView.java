/*
 * MIT License
 *
 * Copyright (c) 2017 Rishi Raj
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */


package com.khangle.myfitnessapp.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.khangle.myfitnessapp.R;
import com.khangle.myfitnessapp.model.Excercise;
import com.khangle.myfitnessapp.model.user.PlanDay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/**
 * @author Rishi Raj
 */

public class PlayingView extends View implements TextToSpeech.OnInitListener, ExerciseModificationListener {

    private final static String TAG = "PLAYVIEW";
    private final float RATIO = 0.2f;
    public String selectLevel;
    private TextToSpeech textToSpeech;
    //Current state variables:
    private ArrayList<PlanDay> exercises;
    private volatile int index;
    private volatile float timePassed;
    private volatile int turnsPassed;
    private float countdownVolume = 0.8f;
    private volatile long time;
    private boolean soundMute;
    private volatile boolean paused = true;
    private float incidentTappedX = -1, incidentTappedY = -1, currentTappedX = -1, currentTappedY = -1;
    private float deltaX = 0, deltaY = 0;
    private long tappedTime = -1;
    private SoundPool soundPool;
    private int sounds[], nextSound;
    private int prevSec = -1;
    private int timeLeftAfterCurrentExercise; //I know that LOOONG name hurts ;)
    private LinkedList<IntegerChangeListener> integerChangeListeners;
    private FinishSetListener finishSetListener;
    private TurnFinishListener turnFinishListener;
    private float gapBetweenExercises;
    private int playCircleColor = Color.RED;
    private int playRingColor = Color.WHITE;
    private int playBGcolor = Color.BLACK;
    private int playTxtColor = Color.WHITE;
    private int playButtonColor = Color.WHITE;
    private float playSwipeThreshold = 120;
    //Value determined at run time
    private float cx, cy, turnsTextYpos, outerRadius, innerRadius, circleLength, volumeControlX, volumeControlY;
    private Paint paint, paintExName, paintTurns, paintRest, paintButton, paintButtonHover, paintTimer;
    private LightingColorFilter buttonColorFilter;
    private Bitmap pause, next, back, not_mute, mute;
    private float timerX, timerY;
    private boolean ttsEnabled = true;
    private int timeForExerciseAt[];


    public PlayingView(Context context) {
        super(context);
        init();
    }

    public PlayingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /*        Initialisation code begins        */
    private void init() {
        initSounds();
        initPaints();
        initBitmaps();
        setWillNotDraw(false);
        time = System.nanoTime();
    }

    private void initBitmaps() {
        back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
        pause = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
        next = BitmapFactory.decodeResource(getResources(), R.drawable.next);
        mute = BitmapFactory.decodeResource(getResources(), R.drawable.mute);
        not_mute = BitmapFactory.decodeResource(getResources(), R.drawable.unmute);
    }

    private void initPaints() {
        paint = new Paint();
        paintTimer = new Paint();
        paintButton = new Paint();
        paintButtonHover = new Paint();
        paintTurns = new Paint();
        paintExName = new Paint();
        paintRest = new Paint();


        paintExName.setColor(playTxtColor);
        paintTurns.setColor(playTxtColor);
        paintTimer.setColor(playTxtColor);
        paintRest.setColor(playTxtColor);

        buttonColorFilter = new LightingColorFilter(playButtonColor, 0);
        paintButton.setColorFilter(buttonColorFilter);
        paintButtonHover.setColorFilter(buttonColorFilter);

        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintTimer.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintButton.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintButtonHover.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintExName.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintTurns.setFlags(Paint.ANTI_ALIAS_FLAG);
        paintRest.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    private void initSounds() {
        soundMute = false;
        ttsEnabled = true;
        countdownVolume =  0.8f;
        if (ttsEnabled) {
            textToSpeech = new TextToSpeech(getContext(), this);
            textToSpeech.setLanguage(Locale.ENGLISH);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder().setAudioAttributes
                    (new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                            .build()).build();
        } else soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 100);
        nextSound = soundPool.load(getContext(), R.raw.track_next, 2);
        sounds = new int[11];
        sounds[0] = soundPool.load(getContext(), R.raw.track_0, 2);
        sounds[1] = soundPool.load(getContext(), R.raw.track_1, 2);
        sounds[2] = soundPool.load(getContext(), R.raw.track_2, 2);
        sounds[3] = soundPool.load(getContext(), R.raw.track_3, 2);
        sounds[4] = soundPool.load(getContext(), R.raw.track_4, 2);
        sounds[5] = soundPool.load(getContext(), R.raw.track_5, 2);
        sounds[6] = soundPool.load(getContext(), R.raw.track_6, 2);
        sounds[7] = soundPool.load(getContext(), R.raw.track_7, 2);
        sounds[8] = soundPool.load(getContext(), R.raw.track_8, 2);
        sounds[9] = soundPool.load(getContext(), R.raw.track_9, 2);
        sounds[10] = soundPool.load(getContext(), R.raw.track_10, 2);
    }
    /*        Initialisation code ends        */


    /*        Drawing code begins        */
    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawRing(canvas);
        if (exercises != null) {
            if (index < exercises.size() && index >= 0) {
                drawVolumeButton(canvas);
                drawProgressCircle(canvas);
                drawRingText(canvas);
                drawControls(canvas);
                drawEstimatedTime(canvas);
                drawRestTime(canvas);
                update();
            } else drawSuccess(canvas);
        } else Log.d(TAG, "Exercises are still null");
        postInvalidateDelayed(22);
    }

    private void drawBackground(Canvas canvas) {
        paint.setColor(playBGcolor);
        canvas.drawRect(getLeft(), getTop(), getRight(), getBottom(), paint);
    }

    private void drawRing(Canvas canvas) {
        paint.setColor(playRingColor);
        canvas.drawCircle(cx, cy, outerRadius, paint);

        paint.setColor(playBGcolor);
        canvas.drawCircle(cx, cy, innerRadius, paint);
    }

    private void drawVolumeButton(Canvas canvas) {
        paintButton.setColor(playButtonColor);
        if (!(currentTappedX < volumeControlX + mute.getWidth() && currentTappedY > volumeControlY)) {
            int alpha = 180;
            int r = Color.red(playButtonColor);
            int g = Color.green(playButtonColor);
            int b = Color.blue(playButtonColor);

            paintButton.setColor(Color.argb(alpha, r, g, b));
        }

        if (soundMute)
            canvas.drawBitmap(mute, volumeControlX, volumeControlY, paintButton);
        else
            canvas.drawBitmap(not_mute, volumeControlX, volumeControlY, paintButton);
    }

    private void drawProgressCircle(Canvas canvas) {
        double timeRatio = 0;
        Excercise exc = exercises.get(index).getExc();
        if (timePassed > 0)  {
            int sec = exc.getLevelJSON().get(selectLevel)[1];
            timeRatio = timePassed / sec * 2 * Math.PI;
        }


        if (paused) {
            long timeElap = System.nanoTime() - tappedTime;
            float size = (float) (timeElap / Math.pow(10, 8.6f));
            paint.setColor(playButtonColor);
            if (tappedTime == -1 || size > 0.5f)
                size = 0.5f;
            canvas.drawCircle(cx + (float) Math.sin(timeRatio) * circleLength,
                    cy - (float) Math.cos(timeRatio) * circleLength,
                    (outerRadius - innerRadius) * (1.5f + size), paint);
        }

        paint.setColor(playCircleColor);
        canvas.drawCircle(cx + (float) Math.sin(timeRatio) * circleLength,
                cy - (float) Math.cos(timeRatio) * circleLength,
                (outerRadius - innerRadius) * 1.5f, paint);

    }

    private void drawRingText(Canvas canvas) {
        String text;
        if(true) {
            Excercise exc = exercises.get(index).getExc();
            int turn = exc.getLevelJSON().get(selectLevel)[0];
            text = Integer.toString(turn - turnsPassed);
        } else {
            text = Integer.toString(turnsPassed);
        }
        canvas.drawText(text, (cx - paintTurns.measureText(text) / 2), turnsTextYpos, paintTurns);

        String exName = exercises.get(index).getExc().getName();
        if (paintExName.getTextSize() == 0 && exName.length() != 0)
            setRingFontFor(exName);

        canvas.drawText(exName, cx - paintExName.measureText(exName) / 2,
                cy + (paintExName.descent() + paintExName.ascent()) / 2 + outerRadius / 2, paintExName);
    }

    private void drawControls(Canvas canvas) {
        if (tappedTime != -1) {
            long timeElap = System.nanoTime() - tappedTime;
            int alpha = Math.min((int) (timeElap * 180 / Math.pow(10, 9)), 180);

            int r = Color.red(playButtonColor);
            int g = Color.green(playButtonColor);
            int b = Color.blue(playButtonColor);

            paintButton.setColor(Color.argb(alpha, r, g, b));

            if (deltaY < 0) canvas.drawBitmap(pause, cx - pause.getWidth() / 2,
                    0, paintButtonHover);
            else canvas.drawBitmap(pause, cx - pause.getWidth() / 2,
                    0, paintButton);


            if (deltaX > 0) canvas.drawBitmap(next, (3 * cx + outerRadius - next.getWidth()) / 2,
                    cy - next.getHeight() / 2, paintButtonHover);
            else canvas.drawBitmap(next, (3 * cx + outerRadius - next.getWidth()) / 2,
                    cy - next.getHeight() / 2, paintButton);


            if (deltaX < 0) canvas.drawBitmap(back, (cx - outerRadius - back.getWidth()) / 2,
                    cy - back.getHeight() / 2, paintButtonHover);
            else canvas.drawBitmap(back, (cx - outerRadius - back.getWidth()) / 2,
                    cy - back.getHeight() / 2, paintButton);
        }
    }

    private void drawEstimatedTime(Canvas canvas) {
        PlanDay ex = exercises.get(index);
        Excercise exc = exercises.get(index).getExc();
        int sec = exc.getLevelJSON().get(selectLevel)[1];
        int gap = exc.getLevelJSON().get(selectLevel)[2];
        float timePassedInEx = turnsPassed * (sec + gap) + timePassed;
        int leftTime = (int) (timeLeftAfterCurrentExercise + timeForExerciseAt[index] - timePassedInEx);
        String mins = getTwoDigitStringFor(leftTime / 60);
        String secs = getTwoDigitStringFor(leftTime % 60);
        canvas.drawText(mins + ":" + secs, timerX, timerY, paintTimer);
    }

    private void drawSuccess(Canvas canvas) {
        String text = "Success!"; // ban tinh hieu success de log diem danh thanh cong
        if (index < 0)
            text = "Start";
        canvas.drawText(text, (cx - paintExName.measureText(text) / 2), turnsTextYpos, paintExName);
    }

    private void drawRestTime(Canvas canvas) {
        if (timePassed < -0.04f) {
            String restTime = Float.toString(-timePassed);
            for (int i = 0; i < restTime.length(); i++) {
                if (restTime.charAt(i) == '.') {
                    restTime = restTime.substring(0, i + 2);
                    break;
                }
            }
            canvas.drawText("Rest " + restTime, timerX, paintRest.descent() - paintRest.ascent(), paintRest);
        }
    }
    /*        Drawing code ends        */


    /*        Updating code begins        */
    protected void update() {
        if (incidentTappedX != -1) {
            updateControls();
        } else {
            updateTimers();
        }
        checkForNextTurn();
    }

    private void updateControls() {
        float change;

        //Check if dx is greater than the threshold value
        change = currentTappedX - incidentTappedX;
        if (Math.abs(change) > playSwipeThreshold) {

            deltaX = (float) Math.pow(change / outerRadius, 3) * RATIO;
            Log.d(TAG, "deltaX: " + deltaX);
            timePassed += deltaX;
            Excercise exc = exercises.get(index).getExc();
            int sec = exc.getLevelJSON().get(selectLevel)[1];
            int turn = exc.getLevelJSON().get(selectLevel)[0];
            if (timePassed < 0 && deltaX < 0) {
                timePassed = sec;
                if (turnsPassed <= 0) {
                    setIndex(getIndex() - 1); //index--
                    if (index < 0) {
                        setIndex(0);
                        turnsPassed = 0;
                        timePassed = 0;
                    } else {
                        turnsPassed = turn - 1;
                        timePassed = sec;
                    }
                } else {
                    turnsPassed--;
                }
            }
        }
        change = currentTappedY - incidentTappedY;
        if (Math.abs(change) > playSwipeThreshold) {
            deltaY = (float) Math.pow(change / outerRadius, 3) * RATIO;
            Log.d(TAG, "deltaX: " + deltaY);
        }
    }

    private void checkForNextTurn() {
        if (!paused)
            timePassed += (System.nanoTime() - time) / Math.pow(10, 9);
        time = System.nanoTime();
        Excercise exc = exercises.get(index).getExc();
        int sec = exc.getLevelJSON().get(selectLevel)[1];
        int turn = exc.getLevelJSON().get(selectLevel)[0];
        int gap = exc.getLevelJSON().get(selectLevel)[2];
        float tpt = sec;
        if (timePassed > tpt) {
            turnsPassed++;
            turnFinishListener.onNextTurn(turnsPassed);
            if (turnsPassed != turn && currentTappedX == -1) {
                final String text;
                if(true)
                    text = turn - turnsPassed + "to go";
                else text = turnsPassed + " completed";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ttsEnabled) {
                            speak(text);
                        } else {
                            soundPool.play(nextSound, countdownVolume, countdownVolume, 2, 0, 1);
                        }
                    }
                }, 300);
            }
            if (turnsPassed >= turn) {
                timePassed = -gapBetweenExercises;
                setIndex(getIndex() + 1); //index++
                turnsPassed = 0;

            } else timePassed = 0 - gap;
        }
    }

    private void updateTimers() {
        Excercise exc = exercises.get(index).getExc();
        int sec = exc.getLevelJSON().get(selectLevel)[1];
        int currSec = (int) (Math.max(0, timePassed) / sec * 10);
        if(timePassed < 0) currSec = 10;
        if (currSec != prevSec) {
            //Skip very first count
            if(prevSec == -1 && (currSec == 0 || currSec == 10)) {
                return;
            }
            Log.d("SOUND","Prev: " + prevSec + ", Current: " + currSec);
            prevSec = currSec;
            playSound(currSec);
        }
    }
    /*        Updating code ends        */


    /*        Overriden view methods begin        */

    @Override
    protected void onDetachedFromWindow() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        paused = true;
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("turns", turnsPassed);
        bundle.putFloat("time", timePassed);
        bundle.putInt("index", index);
        bundle.putBoolean("paused", paused);
        bundle.putFloat("countdownVolume", countdownVolume);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        paused = true;
        if (state instanceof Bundle) // implicit null check
        {
            Bundle bundle = (Bundle) state;
            timePassed = bundle.getFloat("time");
            turnsPassed = bundle.getInt("turns");
            setIndex(bundle.getInt("index", -1));
            state = bundle.getParcelable("superState");
            countdownVolume = bundle.getFloat("countdownVolume", 0.8f);
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        float ratio = 3;
        cx = getWidth() / 2;
        cy = getHeight() / 2;
        outerRadius = Math.min(getWidth() / ratio, getHeight() / ratio);
        innerRadius = outerRadius / 1.1f;
        circleLength = (outerRadius + innerRadius) / 2;
        Log.d(TAG, "Dimensions: " + "cx: " + cx + " cy: " + cy +
                " outerRadius: " + outerRadius + "innerRadius: " + innerRadius);
        float scale;

        float maxSize = Math.min(cx - outerRadius, getHeight() / 2);

        scale = maxSize / back.getWidth();
        back = Bitmap.createScaledBitmap(back, (int) (back.getWidth() * scale),
                (int) (back.getHeight() * scale), false);

        scale = maxSize / next.getWidth();
        next = Bitmap.createScaledBitmap(next, (int) (next.getWidth() * scale),
                (int) (next.getHeight() * scale), false);

        scale = (cy - outerRadius) / pause.getHeight();
        pause = Bitmap.createScaledBitmap(pause, (int) (pause.getWidth() * scale),
                (int) (pause.getHeight() * scale), false);

        int volumeButtonSize = (int) ((0.5 - 1 / ratio) * getHeight());
        mute = Bitmap.createScaledBitmap(mute, volumeButtonSize, volumeButtonSize, false);
        not_mute = Bitmap.createScaledBitmap(not_mute, volumeButtonSize, volumeButtonSize, false);

        volumeControlX = (cx - outerRadius) / 4;
        volumeControlY = getHeight() - mute.getHeight() - volumeControlX;

        paintTurns.setTextSize(210);
        float delta = (outerRadius * 0.8f) / paintTurns.measureText("32");
        paintTurns.setTextSize(delta * paintTurns.getTextSize());


        turnsTextYpos = (int) (cy - ((paintTurns.descent() + paintTurns.ascent()) / 2)
                - outerRadius / 9);

        timerX = cx + outerRadius * 0.4f;
        paintTimer.setTextSize(100);
        delta = (getWidth() - timerX) / paintTimer.measureText("12:12");
        paintTimer.setTextSize(100 * 0.7f * delta);
        timerY = getHeight() - paint.ascent() - volumeControlX;

        paintRest.setTextSize(100);
        delta = (getWidth() - timerX) / paintRest.measureText("Rest 8.23");
        paintRest.setTextSize(100 * 0.8f * delta);

        playSwipeThreshold = outerRadius / 2;

        if (index < 0)
            setRingFontFor("start");

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                currentTappedX = event.getX();
                currentTappedY = event.getY();
                if (incidentTappedX == -1)
                    incidentTappedX = currentTappedX;
                if (incidentTappedY == -1)
                    incidentTappedY = currentTappedY;
                return true;

            case MotionEvent.ACTION_DOWN:
                paused = true;
                incidentTappedX = event.getX();
                currentTappedX = incidentTappedX;
                incidentTappedY = event.getY();
                currentTappedY = incidentTappedY;
                tappedTime = System.nanoTime();
                return true;

            case MotionEvent.ACTION_UP:
                if (index < 0)
                    setIndex(0);
                if (event.getY() > incidentTappedY - playSwipeThreshold &&
                        event.getY() > cy - innerRadius)
                    paused = false;
                deltaX = 0;
                deltaY = 0;
                incidentTappedX = -1;
                incidentTappedY = -1;
                currentTappedX = -1;
                currentTappedY = -1;
                tappedTime = -1;
                //Check countdownVolume
                if (event.getX() < volumeControlX + mute.getWidth() &&
                        event.getY() > volumeControlY) {
                    soundMute = !soundMute;
                }
                return true;
        }
        return false;
    }
    /*        Overriden view methods end        */


    /*        Utility methods begin        */
    private void playSound(int n) {
        Log.d("SOUND","Playing " + n);
        if (!soundMute) {
            if (soundPool == null || sounds == null)
                initSounds();
            if(countdownVolume != 0) {
                if(true) {
                    n = 10 - n;
                }
                soundPool.play(sounds[n], countdownVolume, countdownVolume, 2, 0, 1);
            }
            else {
                if(n == 0) soundPool.play(nextSound,0.5f,0.5f,2,0,1);
            }
        }
    }

    private void speak(String text) {
        if (!soundMute && ttsEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, text + (Math.random() * 100));
            } else textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private String getTwoDigitStringFor(int num) {
        if (num < 10)
            return "0" + num;
        else return Integer.toString(num);
    }

    private void calculateTimeLeftAfterCurrentExercise(int val) {
        timeLeftAfterCurrentExercise = 0;
        for (int i = val + 1; i < exercises.size(); i++) {
            timeLeftAfterCurrentExercise += timeForExerciseAt[i];
            // Gap after exercise
            timeLeftAfterCurrentExercise += gapBetweenExercises;
        }

    }

    private void setRingFontFor(String text) {
        int tempFontSize = 100;
        //Select a temporary font size and measure the size of text in it
        paintExName.setTextSize(tempFontSize);
        float textWidth = paintExName.measureText(text);
        //Now choose fontSize such that the size is reduced to 1.5 * innerRadius
        float scaledFontSize = tempFontSize * (innerRadius * 1.3f) / textWidth;
        //However font size should not be very large for small texts.
        //It should be reasonably lesser than paintTurns' font size
        paintExName.setTextSize(Math.min(scaledFontSize, paintTurns.getTextSize() / 2.5f));
    }

    private void speakOutForIndexChange(int val) {
        if (val == exercises.size()) {
            speak("Session complete");
        }
        if (val >= 0 && val < exercises.size()) {
            final Handler handler = new Handler();
            final String text = exercises.get(val).getExc().getName();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (ttsEnabled) {
                        speak(text);
                    } else {
                        soundPool.play(nextSound, countdownVolume, countdownVolume, 2, 0, 1);
                    }
                }
            }, 400);
        }
    }

    private void calcTimeForExercises() {
        timeForExerciseAt = new int[exercises.size()];
        for (int i = 0; i < exercises.size(); i++) {
            PlanDay exercise = exercises.get(i);
            Excercise exc = exercise.getExc();
            int turn = exc.getLevelJSON().get(selectLevel)[0];
            int sec = exc.getLevelJSON().get(selectLevel)[1];
            int gap = exc.getLevelJSON().get(selectLevel)[2];
            timeForExerciseAt[i] = (int) (turn * sec
                    + (turn - 1) * gap);
        }
    }
    /*        Utility methods end        */


    /*        Implementation of interfaces begins        */
    @Override
    public void onInit(int status) {
        Log.d("TTSpeechPlay", "Initialized. Status : " + status);
        ttsEnabled = status == TextToSpeech.SUCCESS;
    }

    @Override
    public void onChange(boolean wasRemoval) {
        calcTimeForExercises();
        calculateTimeLeftAfterCurrentExercise(index);
    }
    /*        Implementation of interfaces ends        */


    /*        Getters,setters,etc begin       */
    public ArrayList<PlanDay> getExercises() {
        return exercises;
    }

    public void setExercises(ArrayList<PlanDay> exercises) {
        if (this.exercises == null) {
            this.exercises = exercises;
            calcTimeForExercises();
            setIndex(-1);
            Log.d("EXERSTAT", "Exercises set for the view");
        } else Log.e("EXERSTAT", "Cannot change exercises once it has been initialised");
    }

    public void setGapBetweenExercises(int gapBetweenExercises) {
        this.gapBetweenExercises = gapBetweenExercises;
    }

    public int getIndex() {
        return index;
    }

    private void setIndex(int val) {
        time = System.nanoTime();
        index = val;
        speakOutForIndexChange(val);
        try {
            System.gc();
            if (exercises != null && exercises.get(val) != null) {
                PlanDay ex = exercises.get(val);
                if (false)
                    timePassed = 0;
                calculateTimeLeftAfterCurrentExercise(val);
                setRingFontFor(ex.getExc().getName());
                paused = true ? paused : true;
            } else {
                Log.e(TAG, "Exercises is null");
            }
        } catch (IndexOutOfBoundsException ex) {
            if (index < 0) {
                setRingFontFor("Start");
            } else {
                setRingFontFor("Success!");
            }
            Log.e(TAG, "index out of bounds. Font size adjusted for \"Success!\"");
        }
        for (IntegerChangeListener listener : integerChangeListeners) {
            if (index == exercises.size()) {
                finishSetListener.onFinish(exercises.get(index-1));
            }
            listener.onChange(index);
        }
    }

    public void addFinishListener(FinishSetListener listener) {
        finishSetListener = listener;
    }
    public void removeFinishListener() {
        finishSetListener = null;
    }

    public void addTurnFinishListener(TurnFinishListener listener) {
        turnFinishListener = listener;
    }
    public void removeTurnFinishListener() {
        turnFinishListener = null;
    }

    public void addIntegerChangeListener(IntegerChangeListener listener) {
        if (integerChangeListeners == null) {
            integerChangeListeners = new LinkedList<>();
        }
        integerChangeListeners.add(listener);
    }

    public void removeIntegerChangeListener(IntegerChangeListener listener) {
        integerChangeListeners.remove(listener);
    }
    /*        Getters,setters,etc end       */

}