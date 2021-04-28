package com.example.drawingapp.Paint_Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Paint;

import com.example.drawingapp.Paint_Model.PaintDbSchema.PaintTable;
import com.example.drawingapp.Painter_Model.PainterDbSchema;

public class PaintBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "paintingBase.db";

    public PaintBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ PaintTable.NAME+ "(" +"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PaintTable.Cols.PATH + ", " +
                PaintTable.Cols.NAME+ ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
