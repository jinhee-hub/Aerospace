package com.example.drawingapp.ui;

import android.content.Context;
import android.content.res.Resources;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.drawingapp.R;

public class MyPaint extends View {

    Bitmap mbit;
    Canvas mCanvas;
    Paint mBitmapPaint;
    Paint mPaint;
    Path path;
    int color=Color.BLACK;
    //final int color=Color.BLACK;

    boolean erase=false;


    public MyPaint(Context context) {
        super(context);
        path= new Path();
        mPaint= new Paint();
        mPaint.setAntiAlias((false));
        mPaint.setFilterBitmap(false);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);  // pen width
        mPaint.setColor(color); // pen color

        mBitmapPaint= new Paint();
        mBitmapPaint= new Paint(Paint.DITHER_FLAG);
    }

    public MyPaint(Context context, AttributeSet attrs) {
        super(context, attrs);
        path= new Path();
        mPaint= new Paint();
        mPaint.setAntiAlias((false));
        mPaint.setFilterBitmap(false);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);  // pen width
        mPaint.setColor(color); // pen color

        mBitmapPaint= new Paint();
        mBitmapPaint= new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        mbit=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);

        mCanvas=new Canvas(mbit);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE); // canvas Color

        canvas.drawBitmap(mbit,0,0,mBitmapPaint);
        canvas.drawPath(path, mPaint);   // draw path
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX= event.getX();
        float touchY= event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(touchX,touchY);
                mCanvas.drawPath(path, mPaint);
                path.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public void setErase(boolean isErase){
        erase=isErase;
        if(erase) mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        else mPaint.setXfermode(null);
    }


}
