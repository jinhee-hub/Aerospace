package com.example.drawingapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.drawingapp.Paint_Model.Painting;
import com.example.drawingapp.Painter_Model.PainterSingleton;
import com.example.drawingapp.R;
import com.example.drawingapp.Paint_Model.PaintLab;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import static com.example.drawingapp.ui.PainterActivity.isNetworkOnline1;

public class DrawActivity extends AppCompatActivity {

    MyPaint mypaint;
    private Button backBtn;
    private Button penBtn;
    private Button eraseBtn;
    private Button saveBtn;
    private Button exportBtn;
    private Button bgdBtn;
    private ImageView imageView;
    private String mText;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    static final int REQUEST_IMAGE=111;
    static final int REQUEST_CAMERA=333;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        Log.v("DrawActivity: ", "onCreate()");

        mypaint = (MyPaint) findViewById(R.id.paint);

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(R.string.camautho)
                .setDeniedMessage(R.string.camdeny)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        bgdBtn = findViewById(R.id.btn_bgd);
        bgdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DrawActivity: ", "click background");
                AlertDialog.Builder camDialog= new AlertDialog.Builder(DrawActivity.this);
                camDialog.setTitle(R.string.corg);
                camDialog.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                }).setNeutralButton(R.string.Gallery, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        fromGallary(); // get a background from the gallery
                    }
                }).setNegativeButton(R.string.cam, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        fromCamera(); // take a picture from camera sensor and get it as background
                    }
                });
                camDialog.show();
            }
        });

        penBtn = findViewById(R.id.btn_pen);
        penBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DrawActivity: ", "click pen");
                mypaint.setErase(false);
            }
        });

        eraseBtn = findViewById(R.id.btn_erase);
        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DrawActivity: ", "click erase");
                mypaint.setErase(true);
            }
        });

        saveBtn = findViewById(R.id.btn_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DrawActivity: ", "click save");
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(DrawActivity.this);
                View viewInflated = LayoutInflater.from(DrawActivity.this).inflate(R.layout.text_input_name,(ViewGroup)findViewById(R.id.content),false);
                final EditText inputt = (EditText) viewInflated.findViewById(R.id.input);
                saveDialog.setView(viewInflated);
                saveDialog.setMessage(R.string.qdg);
                saveDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        if(!isNetworkOnline1(getApplicationContext())){
                            AlertDialog.Builder saveDialog = new AlertDialog.Builder(DrawActivity.this);
                            saveDialog.setMessage(R.string.nonetexport);
                            saveDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    //save drawing
                                    mypaint.setDrawingCacheEnabled(true);
                                    //attempt to save
                                    String imgSaved = MediaStore.Images.Media.insertImage(
                                            getContentResolver(), mypaint.getDrawingCache(),
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
                                    mypaint.destroyDrawingCache();
                                }
                            });
                            saveDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.cancel();
                                }
                            });
                            saveDialog.show();
                            return;
                        }
                        mText = inputt.getText().toString();
                        //save drawing
                        mypaint.setDrawingCacheEnabled(true);
                        String text = mText;
                        if(text.matches("")){
                            text = "NewImage";
                        }
                        String imgname  = text+UUID.randomUUID().toString();

                        StorageReference imgRef = mStorageRef.child("gallery/"+imgname+".jpg");
                        /*
                        Bitmap img = mypaint.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        // Use the compress method on the BitMap object to write image to the OutputStream
                        img.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();

                         */
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        File mypath = new File(directory, UUID.randomUUID().toString()+".jpg");

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mypath);
                            Bitmap img = mypaint.getDrawingCache();
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            img.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    "save success", Toast.LENGTH_SHORT);
                            savedToast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        Uri file = Uri.fromFile(mypath);

                        PainterSingleton painter = PainterSingleton.get(getApplicationContext());
                        String name = painter.getPainter();
                        StorageMetadata metadata = new StorageMetadata.Builder()
                                .setContentType("image/jpg")
                                .setCustomMetadata("Painter", name)
                                .setCustomMetadata("PaintName",text)
                                .build();
                        UploadTask uploadTask = imgRef.putFile(file,metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                Toast savedToast = Toast.makeText(getApplicationContext(),
                                        "Upload failed", Toast.LENGTH_SHORT);
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                                // ...
                                Toast savedToast = Toast.makeText(getApplicationContext(),
                                        "save success", Toast.LENGTH_SHORT);
                            }
                        });

                        /*
                        imgRef.updateMetadata(metadata)
                                .addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                    @Override
                                    public void onSuccess(StorageMetadata storageMetadata) {
                                        // Updated metadata is in storageMetadata
                                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                                "Metadata updated", Toast.LENGTH_SHORT);
                                        savedToast.show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Uh-oh, an error occurred!
                                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                                "Update fail", Toast.LENGTH_SHORT);
                                        savedToast.show();
                                    }
                                });

                         */

                        //attempt to save
                        /*
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                        File mypath = new File(directory, UUID.randomUUID().toString()+".png");

                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(mypath);
                            Bitmap img = mypaint.getDrawingCache();
                            // Use the compress method on the BitMap object to write image to the OutputStream
                            img.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            Toast savedToast = Toast.makeText(getApplicationContext(),
                                    R.string.success, Toast.LENGTH_SHORT);
                            String text = mText;
                            if(text.matches("")){
                                text = "NewImage";
                            }
                            PaintLab paintlab = PaintLab.get(getApplicationContext());
                            Painting paint = new Painting(mypath.getAbsolutePath(),text);
                            paintlab.createPainter(paint);
                            savedToast.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        */
                        mypaint.destroyDrawingCache();

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

        // Make Button
        backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawActivity.this, MainActivity.class);  // move to Draw Activity
                startActivity(intent); // move to Draw Activity

                Log.v("MainActivity: ", "onClick()");
            }
        });

        exportBtn = findViewById(R.id.btn_export);
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("DrawActivity: ", "click export");
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(DrawActivity.this);
                saveDialog.setMessage(R.string.qdd);
                saveDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //save drawing
                        mypaint.setDrawingCacheEnabled(true);
                        //attempt to save
                        String imgSaved = MediaStore.Images.Media.insertImage(
                                getContentResolver(), mypaint.getDrawingCache(),
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
                        mypaint.destroyDrawingCache();
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
    }

    public void fromGallary() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    public void fromCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CAMERA);
        }

    }


    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(getApplicationContext(), R.string.ap, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(getApplicationContext(), R.string.dp , Toast.LENGTH_SHORT).show();
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE && resultCode==RESULT_OK) {
            Uri imguri=data.getData();
            imageView=(ImageView)findViewById(R.id.imgbgd);
            imageView.setImageURI(imguri);
            imageView.setAlpha(50);
        }
        else if (requestCode==REQUEST_CAMERA && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView=(ImageView)findViewById(R.id.imgbgd);
            imageView.setImageBitmap(imageBitmap);
            imageView.setAlpha(80);
        }

    }
}