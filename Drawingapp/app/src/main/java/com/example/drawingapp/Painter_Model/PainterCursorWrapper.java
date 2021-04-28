package com.example.drawingapp.Painter_Model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.drawingapp.Painter_Model.PainterDbSchema.PrinterTable;

public class PainterCursorWrapper extends CursorWrapper {
    PainterCursorWrapper(Cursor cursor) {super(cursor);}

    String getColor(){
        String color = getString(getColumnIndex(PrinterTable.Cols.COLOR));

        return color;
    }

}
