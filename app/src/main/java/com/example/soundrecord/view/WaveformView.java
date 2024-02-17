package com.example.soundrecord.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;


public class WaveformView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private final Paint paint = new Paint();
    private DrawingThread drawingThread;

    public WaveformView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(5);
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.v("sjkdjkajdkajkjadkajdka","crateedeedeededed");
        // init
        drawingThread = new DrawingThread(holder,paint);
        drawingThread.setRunning(true);
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        // surface size changed process
    }


    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        // release resource
        boolean retry = true;
        drawingThread.setRunning(false);
        while (retry) {
            try {
                drawingThread.join();
                retry = false;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void updateAmplitude(int amplitude) {
        if (drawingThread != null) {
            drawingThread.setAmplitude(amplitude);
        }
    }

    public static class DrawingThread extends Thread {
        private final SurfaceHolder surfaceHolder;
        private final Paint paint;
        private boolean running = false;
        private int amplitude = 0;

        public DrawingThread(SurfaceHolder surfaceHolder,Paint paint) {
            this.surfaceHolder = surfaceHolder;
            this.paint = paint;
        }
        public void setRunning(boolean running) {
            this.running = running;
        }
        public void setAmplitude(int amplitude) {
            this.amplitude = amplitude;
        }

        @Override
        public void run() {
            while (running) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas(null);
                    if (canvas != null) {
                        synchronized (surfaceHolder) {
                            canvas.drawColor(Color.BLACK);
                            canvas.drawLine((float) canvas.getWidth() / 2, (float) canvas.getHeight() / 2,
                                    (float) canvas.getWidth() / 2, (float) canvas.getHeight() / 2 - amplitude, paint);
                        }
                    }
                }finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

    }
}
