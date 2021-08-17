package com.example.drawingapp.Painter_Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PainterSingleton {
    private static PainterSingleton sPainterSingleton;

    private Context mContext;

    private String mPainter;


    public static PainterSingleton get(Context context){
        if(sPainterSingleton==null){
            sPainterSingleton = new PainterSingleton(context);
        }
        return sPainterSingleton;
    }

    private PainterSingleton(Context context){
        mContext = context.getApplicationContext();
        mPainter = "Default";
    }

    public String getPainter() {

        return mPainter;
    }

    public void setPainter(String painter) {
        mPainter = painter;
    }
}
