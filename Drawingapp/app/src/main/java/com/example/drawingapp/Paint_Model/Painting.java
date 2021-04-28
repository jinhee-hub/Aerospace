package com.example.drawingapp.Paint_Model;

import java.util.UUID;

public class Painting {
    private String mPath;
    private String mName;

    public Painting(String n, String m){
        mPath = n;
        mName = m;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
