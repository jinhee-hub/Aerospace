package com.example.drawingapp.Painter_Model;

import java.util.UUID;

public class Painter {
    private UUID mId;
    private String mName;
    private String mColor;

    public Painter(String n, String c){
        this(n,c,UUID.randomUUID());
    }

    public Painter(String n, String c, UUID id){
        mId = id;
        mName = n;
        mColor = c;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }
}
