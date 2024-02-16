package com.example.soundrecord.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;

public class WaveformView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private short[] audioData;

    public WaveformView(Context context) {
        super(context);
        init();
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }
    private void init() {
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
    }

    public void updateAudioData(short[] audioData) {
        this.audioData = audioData;
    }

    public void drawWaveform() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            // clean screen
            canvas.drawColor(Color.BLACK);
            // draw wave
            if (audioData != null) {
                int width = getWidth();
                int height = getHeight();
                int numBars = audioData.length;
                int barWidth = width / numBars;
                for (int i = 0; i < numBars; i++) {
                    float barHeight = (audioData[i] / 32768.0f) * height / 2;
                    canvas.drawRect(i * barWidth, (float) height / 2 - barHeight,(i + 1) * barWidth, (float) height / 2 + barHeight,paint);
                }
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // init
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // surface size changed process
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // release resource
    }
}
