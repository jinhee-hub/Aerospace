package com.example.drawingapp.Painter_Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drawingapp.Painter_Model.PainterDbSchema.PrinterTable;

public class PainterBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "printerBase.db";

    public PainterBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ PrinterTable.NAME+ "(" +"_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        PrinterTable.Cols.UUID + ", " +
        PrinterTable.Cols.NAME + ", " +
        PrinterTable.Cols.COLOR+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
