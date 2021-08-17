package com.example.drawingapp.Paint_Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class DrawLab {
    private static DrawLab sDrawLab;
    private Context mContext;
    private List<StorageReference> mDraws;

    public static DrawLab getDraws(Context context){
        if(sDrawLab==null){
            sDrawLab = new DrawLab(context);
        }
        return sDrawLab;
    }

    private DrawLab(Context context){
        mContext = context.getApplicationContext();
        mDraws = new ArrayList<>();
    }
    public void emptyDraws(){
        mDraws = new ArrayList<>();
    }

    public void addDraw(StorageReference draw){
        mDraws.add(draw);
    }

    public List<StorageReference> getDraws(){
        return mDraws;
    }
}
