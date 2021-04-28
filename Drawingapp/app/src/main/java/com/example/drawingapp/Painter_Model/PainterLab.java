package com.example.drawingapp.Painter_Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.UUID;

import com.example.drawingapp.Painter_Model.PainterDbSchema.PrinterTable;

public class PainterLab {
    private static PainterLab sPainterLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PainterLab get(Context context){
        if(sPainterLab==null){
            sPainterLab = new PainterLab(context);
        }
        return sPainterLab;
    }

    private PainterLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new PainterBaseHelper(mContext).getWritableDatabase();
    }

    public Boolean createPainter(String name, String color){
        Cursor cursor = mDatabase.query(PrinterTable.NAME,null,PrinterTable.Cols.NAME+" = ?",new String[]{name},null,null,null);
        try{
            if(cursor.getCount()==0){
                ContentValues values = getContentValues(name,color);
                mDatabase.insert(PrinterTable.NAME,null,values);
                return true;
            }
            return false;
        }finally{
            cursor.close();
        }
    }

    public Boolean updatePainter(String name, String color){
        Cursor cursor = mDatabase.query(PrinterTable.NAME,null,PrinterTable.Cols.NAME+" = ?",new String[]{name},null,null,null);
        try{
            if(cursor.getCount()!=0){
                ContentValues values = getContentValues(name,color);
                mDatabase.update(PrinterTable.NAME,values,PrinterTable.Cols.NAME+" = ?",new String[]{name});
                return true;
            }
            return false;
        }finally{
            cursor.close();
        }

    }

    public void deleterPainter(String name){
        mDatabase.delete(PrinterTable.NAME,PrinterTable.Cols.NAME+" = ?",new String[]{name});
    }

    public String getFavoriteColor(String name){
        Cursor cursor = mDatabase.query(PrinterTable.NAME,null,PrinterTable.Cols.NAME+" = ?",new String[]{name},null,null,null);
        try{
            if(cursor.getCount()!=0){
                PainterCursorWrapper cursorWrapper = new PainterCursorWrapper(cursor);
                cursorWrapper.moveToFirst();
                String color = cursorWrapper.getColor();

                return color;
            }
            return "";
        }finally{
            cursor.close();
        }
    }

    private static ContentValues getContentValues(String name, String color){
        ContentValues values = new ContentValues();
        values.put(PrinterTable.Cols.UUID,UUID.randomUUID().toString());
        values.put(PrinterTable.Cols.NAME,name);
        values.put(PrinterTable.Cols.COLOR,color);
        return values;
    }
}
