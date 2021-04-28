package com.example.drawingapp.Paint_Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.drawingapp.Painter_Model.PainterBaseHelper;
import com.example.drawingapp.Painter_Model.PainterDbSchema;
import com.example.drawingapp.Painter_Model.PainterLab;
import com.example.drawingapp.Paint_Model.PaintDbSchema.PaintTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PaintLab {
    private static PaintLab sPaintLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static PaintLab get(Context context){
        if(sPaintLab==null){
            sPaintLab = new PaintLab(context);
        }
        return sPaintLab;
    }

    private PaintLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new PaintBaseHelper(mContext).getWritableDatabase();
    }

    public Boolean createPainter(Painting paint){
        Cursor cursor = mDatabase.query(PaintDbSchema.PaintTable.NAME,null, PaintDbSchema.PaintTable.Cols.PATH+" = ?",new String[]{paint.getPath()},null,null,null);
        try{
            if(cursor.getCount()==0){
                ContentValues values = getContentValues(paint);
                mDatabase.insert(PaintDbSchema.PaintTable.NAME,null,values);
                return true;
            }
            return false;
        }finally{
            cursor.close();
        }
    }

    public List<Painting> getPaints() {
        List<Painting> paints = new ArrayList<>();
        PaintCursorWrapper cursor = queryPaths(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                paints.add(cursor.getPaint());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return paints;
    }

    private PaintCursorWrapper queryPaths(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                PaintTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new PaintCursorWrapper(cursor);
    }


    private static ContentValues getContentValues(Painting paint){
        ContentValues values = new ContentValues();

        values.put(PaintDbSchema.PaintTable.Cols.PATH,paint.getPath());
        values.put(PaintDbSchema.PaintTable.Cols.NAME,paint.getName());

        return values;
    }
}
