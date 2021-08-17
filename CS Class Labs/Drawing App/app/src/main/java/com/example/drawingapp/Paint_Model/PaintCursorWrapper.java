package com.example.drawingapp.Paint_Model;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drawingapp.Paint_Model.PaintDbSchema.PaintTable;
import com.example.drawingapp.Painter_Model.PainterDbSchema;

public class PaintCursorWrapper extends CursorWrapper {
    public PaintCursorWrapper(Cursor cursor) {super(cursor);}

    public Painting getPaint(){
        String path = getString(getColumnIndex(PaintDbSchema.PaintTable.Cols.PATH));
        String name = getString(getColumnIndex(PaintDbSchema.PaintTable.Cols.NAME));

        Painting paint = new Painting(path,name);

        return paint;
    }

}