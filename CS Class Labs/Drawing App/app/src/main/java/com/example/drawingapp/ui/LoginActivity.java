package com.example.drawingapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.drawingapp.Painter_Model.PainterLab;
import com.example.drawingapp.Painter_Model.PainterSingleton;
import com.example.drawingapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;

import static com.example.drawingapp.ui.PainterActivity.isNetworkOnline1;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mPainterName;
    private EditText mPassword;
    private CollectionReference mUsersRef = FirebaseFirestore.getInstance().collection("users");
    public static final String NAME_KEY = "name";
    public static final String COLOR_KEY = "color";
    public static final String PASSWORD_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.v("LoginActivity: ", "onCreate()");

        mPainterName = findViewById(R.id.name);
        mPassword = findViewById(R.id.password);

        Button btnCreate = findViewById(R.id.login_button);
        btnCreate.setOnClickListener(this);
        Button btnUpdate = findViewById(R.id.create_profile_button);
        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if(viewId == R.id.login_button){
            if(!isNetworkOnline1(getApplicationContext())){
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(LoginActivity.this);
                saveDialog.setMessage(R.string.nonetlogin);
                saveDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        //save drawing
                        PainterSingleton painter = PainterSingleton.get(getApplicationContext());
                        painter.setPainter(getString(R.string.guest));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
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
            String name = mPainterName.getText().toString();
            String password = mPassword.getText().toString();
            if(name.isEmpty()||password.isEmpty()){
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
                                        String realpassword = document.getString(PASSWORD_KEY);
                                        if(realpassword.matches(password)){
                                            PainterSingleton painter = PainterSingleton.get(getApplicationContext());
                                            painter.setPainter(name);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            Log.v("LoginActivity: ", "onClick()");
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), R.string.pi, Toast.LENGTH_SHORT).show();
                                        }
                                        //Toast.makeText(getApplicationContext(), "Update successfully", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                }
                            }

                        }
                    });

        }
        else if(viewId == R.id.create_profile_button){
            Intent intent = new Intent(LoginActivity.this, PainterActivity.class);
            startActivity(intent);
            Log.v("LoginActivity: ", "onClick()");
        }
    }
}
