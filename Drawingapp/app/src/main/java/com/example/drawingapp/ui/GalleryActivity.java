package com.example.drawingapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.drawingapp.Paint_Model.DrawLab;
import com.example.drawingapp.Paint_Model.PaintLab;
import com.example.drawingapp.Paint_Model.Painting;
import com.example.drawingapp.Painter_Model.PainterSingleton;
import com.example.drawingapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.UUID;

import io.grpc.Context;

import static com.example.drawingapp.ui.PainterActivity.isNetworkOnline1;

public class GalleryActivity extends AppCompatActivity {

    private ImageView img_test;
    private Button btn_back2;
    private RecyclerView mPaintRecyclerView;
    private ImageAdapter mAdapter;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        Log.v("GalleryActivity: ", "onCreate()");

        btn_back2=findViewById(R.id.btn_back2);
        btn_back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GalleryActivity.this, MainActivity.class);  // move to Draw Activity
                startActivity(intent); // move to Draw Activity

                Log.v("MainActivity: ", "onClick()");

            }
        });

        /*
        img_test = (ImageView) findViewById(R.id.img_test);  // get image
        img_test.setOnClickListener(new View.OnClickListener() {  // if click do
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "image1", Toast.LENGTH_SHORT).show();
                Log.v("GalleryActivity: ", "onClick()");
            }
        });
        */
        /*
        ImageView img_1 = (ImageView) findViewById(R.id.img);
        try {
            ContextWrapper cw = new ContextWrapper(getApplicationContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(directory, "test.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            img_1.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        */
        mPaintRecyclerView = (RecyclerView) findViewById(R.id.paint_recycler_view);
        mPaintRecyclerView.setLayoutManager(new LinearLayoutManager(GalleryActivity.this));

        updateUI();
        if(!isNetworkOnline1(getApplicationContext())){
            Toast.makeText(getApplicationContext(), R.string.ci, Toast.LENGTH_SHORT).show();

        }
    }

    public void updateUI(){
        /*
        PaintLab paintlab = PaintLab.get(getApplicationContext());
        List<Painting> paints = paintlab.getPaints();
        */
        DrawLab draws = DrawLab.getDraws(getApplicationContext());
        draws.emptyDraws();

        StorageReference listRef = mStorageRef.child("gallery");
        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            DrawLab draws = DrawLab.getDraws(getApplicationContext());
                            draws.addDraw(item);
                            //Toast savedToast = Toast.makeText(getApplicationContext(),
                             //       "there is file", Toast.LENGTH_SHORT);
                            //savedToast.show();
                        }
                        mAdapter = new ImageAdapter(listResult.getItems());

                        //savedToast.show();

                        mPaintRecyclerView.setAdapter(mAdapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "error occurs", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                });

        List<StorageReference> paints = draws.getDraws();
        int length = paints.size();
        Log.v("Gallery Activity: ", "here we are!"+length);
        //mAdapter = new ImageAdapter(paints);

        //savedToast.show();

        //mPaintRecyclerView.setAdapter(mAdapter);
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        private StorageReference mPaint;

        private ImageView mImageView;
        private TextView mTextView;
        private TextView mPainterName;
        private Button mDeleteButton;
        private Button mExportButton;

        public ImageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_gallery, parent, false));
            mImageView = (ImageView) itemView.findViewById(R.id.drawing);
            mTextView = (TextView) itemView.findViewById(R.id.paint_name);
            mPainterName  = (TextView) itemView.findViewById(R.id.painter_name);
            mDeleteButton = (Button) itemView.findViewById(R.id.delete_image);
            mExportButton = (Button) itemView.findViewById(R.id.export_image);
        }

        public void bind(StorageReference paint){
            mPaint = paint;

            paint.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                @Override
                public void onSuccess(StorageMetadata storageMetadata) {
                    // Metadata now contains the metadata for 'images/forest.jpg'
                    mTextView.setText(storageMetadata.getCustomMetadata("PaintName"));
                    mPainterName.setText("By "+storageMetadata.getCustomMetadata("Painter"));
                    PainterSingleton painter = PainterSingleton.get(getApplicationContext());
                    String name = painter.getPainter();
                    if(!storageMetadata.getCustomMetadata("Painter").matches(name)){
                        mDeleteButton.setEnabled(false);
                    }
                    else{
                        mDeleteButton.setEnabled(true);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPaint.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // File deleted successfully
                            Toast.makeText(getApplicationContext(), R.string.os, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Uh-oh, an error occurred!
                        }
                    });
                }
            });

            mExportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(GalleryActivity.this);
                    saveDialog.setMessage(R.string.qdd);
                    saveDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            //save drawing
                            final long ONE_MEGABYTE = 1024 * 1024;
                            mPaint.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    // Data for "images/island.jpg" is returns, use this as needed
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
                                    String imgSaved = MediaStore.Images.Media.insertImage(
                                            getContentResolver(), bitmap,
                                            UUID.randomUUID().toString()+".png", "drawing");
                                    if(imgSaved!=null){
                                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                                R.string.success, Toast.LENGTH_SHORT);
                                        savedToast.show();
                                    }
                                    else{
                                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                                R.string.fail, Toast.LENGTH_SHORT);
                                        unsavedToast.show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    });
                    saveDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
                    saveDialog.show();
                }
            });
            /*
            //mTextView.setText(paint.getName());
            String path = mPaint.getPath();
            try {
                File f = new File(path);
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                mImageView.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
            */
            Glide.with(GalleryActivity.this)
                    .load(mPaint)
                    .into(mImageView);
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder>{
        private List<StorageReference> mPaints;
        public ImageAdapter(List<StorageReference> paints){
            mPaints = paints;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(GalleryActivity.this);
            return new ImageHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            StorageReference paint = mPaints.get(position);
            holder.bind(paint);
        }

        @Override
        public int getItemCount() {
            return mPaints.size();
        }
    }
}