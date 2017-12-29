package com.colinear.graphstuff.CustomUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.colinear.graphstuff.R;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private MainThread thread;

    float touchX=0f;
    float touchY=0f;
    boolean touched = false;

    Rect positionRect;
    Bitmap fingerprint;


    public GameView(Context context) {
        super(context);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @SuppressLint("NewApi")
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        setFocusable(true);
        fingerprint = BitmapFactory.decodeResource(getResources(), R.drawable.fingerprint_glow);
        fingerprint =  applyGradient(fingerprint, Color.parseColor("#abef5353"),  Color.parseColor("#abae00ff"));
        positionRect = new Rect((int) (touchX - 100), (int) (touchY - 100), (int) (touchX + 100), (int) (touchY + 100));

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                touched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touched = true;
                break;
            case MotionEvent.ACTION_UP:
                touched = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                touched = false;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched = false;
                break;
            default:
        }
        return true;

    }


    public void update() {
        positionRect.set((int) (touchX - 100), (int) (touchY - 100), (int) (touchX + 100), (int) (touchY + 100));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {


            if (touched) {
                positionRect = new Rect((int) (touchX - 150), (int) (touchY - 150), (int) (touchX + 150), (int) (touchY + 150));
                canvas.drawBitmap(fingerprint, null, positionRect, null);

            }else{
                canvas.drawBitmap(fingerprint, null, positionRect, null);
            }


        }
    }

    public Bitmap applyGradient(Bitmap originalBitmap, int colorStart, int colorEnd) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, colorStart, colorEnd, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }



}
