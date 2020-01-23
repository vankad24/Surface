package com.example.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TestSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    float x,y;
    int radius;
    Paint paint;
    MyThread thread;

    public TestSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
        paint = new Paint();
        paint.setColor(Color.YELLOW);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        holder = surfaceHolder;
        thread = new MyThread();
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
            radius = 0;
            x = event.getX();
            y = event.getY();
        return true;
    }

    class MyThread extends Thread{
        boolean isRunning=true;


        @Override
        public void run() {
            super.run();
            Canvas canvas;

            while (isRunning) {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    canvas.drawColor(Color.BLUE);
                    if (radius != 0 && x!=0 && y!=0) {
                        canvas.drawCircle(x,y,radius,paint);
                        try {
                            Thread.sleep(1000);
                        }catch (Exception e){}
                    }
                    radius+=5;
                    holder.unlockCanvasAndPost(canvas);
                }
            }
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }
    }
}
