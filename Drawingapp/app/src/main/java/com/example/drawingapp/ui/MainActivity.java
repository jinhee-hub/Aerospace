package com.example.drawingapp.ui;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TextView;

import com.example.drawingapp.Painter_Model.PainterSingleton;

import com.example.drawingapp.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btn_draw;
    private Button btn_gallery;
    private Button btn_painter;
    private TextView welcome_textview;
    private Button btn_lang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v("GalleryActivity: ", "onCreate()");

        PainterSingleton painter = PainterSingleton.get(getApplicationContext());
        String name = painter.getPainter();
        welcome_textview = (TextView) findViewById(R.id.welcome);
        welcome_textview.setText(R.string.welcome);
        welcome_textview.append(" "+name);

        btn_draw=findViewById(R.id.btn_draw);   // Draw button will do below
        btn_draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DrawActivity.class);  // move to Draw Activity
                startActivity(intent); // move to Draw Activity

                Log.v("MainActivity: ", "onClick()");
            }
        });

        btn_gallery=findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
                Log.v("MainActivity: ", "onClick()");
            }
        });

        btn_painter=findViewById(R.id.btn_painter);
        btn_painter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                Log.v("MainActivity: ", "onClick()");
            }
        });







    }
}