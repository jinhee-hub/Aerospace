package com.example.drawingapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drawingapp.Painter_Model.PainterLab;
import com.example.drawingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PainterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPainterName;
    private boolean mExist;
    private EditText mFavColor;
    private Button btn_back3;
    private EditText mPassword;
    public String mDoc;
    private String mColor;
    private CollectionReference mUsersRef = FirebaseFirestore.getInstance().collection("users");
    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";
    public static final String PASSWORD_KEY = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painter);
        Log.v("PainterActivity: ", "onCreate()");

        btn_back3=findViewById(R.id.btn_back3);
        btn_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PainterActivity.this, LoginActivity.class);  // move to Draw Activity
                startActivity(intent); // move to Draw Activity

                Log.v("MainActivity: ", "onClick()");

            }
        });

        mPainterName = findViewById(R.id.create_name);
        mFavColor = findViewById(R.id.favcolor);
        mPassword = findViewById(R.id.create_password);

        Button btnCreate = findViewById(R.id.create_button);
        btnCreate.setOnClickListener(this);
        Button btnUpdate = findViewById(R.id.update_button);
        btnUpdate.setOnClickListener(this);
        Button btnShow = findViewById(R.id.show_button);
        btnShow.setOnClickListener(this);
        Button btnDelete = findViewById(R.id.delete_button);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PainterLab painterlab = PainterLab.get(getApplicationContext());
        final int viewId = view.getId();
        if(viewId == R.id.create_button){
            if(!isNetworkOnline1(getApplicationContext())){
                Toast.makeText(getApplicationContext(), R.string.ci, Toast.LENGTH_SHORT).show();
                return;
            }
            String name = mPainterName.getText().toString();
            String color = mFavColor.getText().toString();
            String password = mPassword.getText().toString();
            if(name.isEmpty()||color.isEmpty()||password.isEmpty()){
                Toast.makeText(getApplicationContext(), R.string.nb, Toast.LENGTH_SHORT).show();
            }
            else{
                /*
                if(userExist(name)){
                    Toast.makeText(getApplicationContext(), "Name already exists", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String,Object> user = new HashMap<>();
                    user.put(NAME_KEY,name);
                    user.put(COLOR_KEY,color);
                    user.put(PASSWORD_KEY,password);
                    String userId = UUID.randomUUID().toString();
                    user.put("UserID",userId);
                    mUsersRef.document("User"+userId)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Created successfully", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error Writing File", Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                 */
                mUsersRef
                        .whereEqualTo(NAME_KEY,name)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                if(task.getResult().isEmpty()){
                                    Map<String,Object> user = new HashMap<>();
                                    user.put(NAME_KEY,name);
                                    user.put(COLOR_KEY,color);
                                    user.put(PASSWORD_KEY,password);
                                    String userId = UUID.randomUUID().toString();
                                    user.put("UserID",userId);
                                    mUsersRef.document("User"+userId)
                                            .set(user)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getApplicationContext(), R.string.cs, Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), R.string.ed, Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }else {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Toast.makeText(getApplicationContext(), R.string.nae, Toast.LENGTH_SHORT).show();
                                        }
                                    } else {

                                    }
                                }

                            }
                        });
            }
            /*
            if(!name.matches("")&&!color.matches("")) {
                Boolean success = painterlab.createPainter(name, color);
                if(success){
                    Toast.makeText(getApplicationContext(), "Created successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Name already exists", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Should not left anything blank", Toast.LENGTH_SHORT).show();
            }

             */
            Log.v("CreateButoon: ", "onClick()");
        }
        else if(viewId == R.id.update_button) {
            if(!isNetworkOnline1(getApplicationContext())){
                Toast.makeText(getApplicationContext(), R.string.ci, Toast.LENGTH_SHORT).show();
                return;
            }
            String name = mPainterName.getText().toString();
            String color = mFavColor.getText().toString();
            if (name.isEmpty() || color.isEmpty()) {
                Toast.makeText(getApplicationContext(), R.string.nb, Toast.LENGTH_SHORT).show();
                return;
            }
            mUsersRef
                    .whereEqualTo(NAME_KEY,name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if(task.getResult().isEmpty()){
                                Toast.makeText(getApplicationContext(), R.string.nde, Toast.LENGTH_SHORT).show();
                            }else {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String document_name = document.getString("UserID");
                                        mUsersRef.document("User"+document_name)
                                                .update(COLOR_KEY,color)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), R.string.us, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), R.string.ed, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        //Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }

                        }
                    });
            /*
            String document_name = getDocName(name);
            //Toast.makeText(getApplicationContext(), mDoc, Toast.LENGTH_SHORT).show();
            if(document_name.matches("")){
                //Toast.makeText(getApplicationContext(), "Name doesn't exist"+name, Toast.LENGTH_SHORT).show();
                return;
            }
            mUsersRef.document("User"+document_name)
                    .update(COLOR_KEY,color)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error in update", Toast.LENGTH_SHORT).show();
                        }
                    });
            /*
            if(!name.matches("")&&!color.matches("")) {
                Boolean success=painterlab.updatePainter(name,color);
                if(success){
                    Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Name doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Should not left anything blank", Toast.LENGTH_SHORT).show();
            }

             */
            Log.v("Update Button: ", "onClick()");
        }
        else if(viewId==R.id.show_button){
            if(!isNetworkOnline1(getApplicationContext())){
                Toast.makeText(getApplicationContext(), R.string.ci, Toast.LENGTH_SHORT).show();
                return;
            }
            String name = mPainterName.getText().toString();
            if(name.isEmpty()){
                Toast.makeText(getApplicationContext(), R.string.nb, Toast.LENGTH_SHORT).show();
                return;
            }
            mColor = "";
            mUsersRef
                    .whereEqualTo(NAME_KEY,name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if(task.getResult().isEmpty()){
                                Toast.makeText(getApplicationContext(), R.string.nde, Toast.LENGTH_SHORT).show();
                            }else {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String uname = document.getString("name");
                                        String ucolor = document.getString("color");
                                        Toast.makeText(getApplicationContext(), uname+" "+R.string.fc+" "+ucolor, Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }

                        }
                    });

            /*
            if(mColor.matches("")){
                Toast.makeText(getApplicationContext(), "Name doesn't exist", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getApplicationContext(), name+" favorite color is "+mColor, Toast.LENGTH_SHORT).show();
            /*
            if(!name.matches("")) {
                String color = painterlab.getFavoriteColor(name);
                if(color!=""){
                    Toast.makeText(getApplicationContext(), name+" favorite color is "+color, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Name doesn't exist", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Should not left name blank", Toast.LENGTH_SHORT).show();
            }
            */

            Log.v("Show Button: ", "onClick()");
        }
        else if( viewId == R.id.delete_button){
            if(!isNetworkOnline1(getApplicationContext())){
                Toast.makeText(getApplicationContext(), R.string.ci, Toast.LENGTH_SHORT).show();
                return;
            }
            String name = mPainterName.getText().toString();

            if (name.isEmpty()) {
                return;
            }
            /*
            String document_name = getDocName(name);
            if(document_name.matches("")){
                return;
            }
            mUsersRef.document("User"+document_name)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Delete successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Error in deleting", Toast.LENGTH_SHORT).show();
                        }
                    });

             */
            mUsersRef
                    .whereEqualTo(NAME_KEY,name)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(Task<QuerySnapshot> task) {
                            if(task.getResult().isEmpty()){
                                Toast.makeText(getApplicationContext(), R.string.nde, Toast.LENGTH_SHORT).show();
                            }else {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String document_name = document.getString("UserID");
                                        mUsersRef.document("User"+document_name)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), R.string.os, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), R.string.ed, Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        //Toast.makeText(getApplicationContext(), "Delete Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }

                        }
                    });
            Log.v("DeleteButton: ", "onClick()");
        }
    }

    public boolean userExist(String name){
        mExist = false;

        mUsersRef
                .whereEqualTo(NAME_KEY,name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mExist = true;
                            }
                        }

                    }
                });
        return mExist;
    }

    public String getDocName(String name){
        mDoc = "";

        Task<QuerySnapshot> task = mUsersRef
                .whereEqualTo(NAME_KEY,name)
                .get();

        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                mDoc = document.getString("UserID");
                Toast.makeText(getApplicationContext(), mDoc, Toast.LENGTH_SHORT).show();
            }
        }


        return mDoc;
    }

    public void setDoc(String doc) {
        mDoc = doc;
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static boolean isNetworkOnline1(Context context) {
        boolean isOnline = false;
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());  // need ACCESS_NETWORK_STATE permission
            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isOnline;
    }
}
