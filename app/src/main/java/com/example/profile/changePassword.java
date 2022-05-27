package com.example.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.MotionEvent;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.text.method.HideReturnsTransformationMethod;

import android.widget.ImageView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class changePassword extends AppCompatActivity {
    private ImageView img;
    private EditText email,passwor,passwordd,passworr;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    Button saveBt;
    FirebaseAuth fAuth;
    FirebaseUser user;
    boolean passwordVisible;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.lightTheme);}
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_password);
        this.img = (ImageView) findViewById(R.id.imag);
        img.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent otherActivity = new Intent(changePassword.this, Setting.class);
                                       startActivity(otherActivity);
                                       finish();
                                   }
                               }
        );
        passwor = findViewById(R.id.frsname_textview);
        this.saveBt = findViewById(R.id.savepassword);

        passwor.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
            final int Right=2;
            if(event.getAction()==MotionEvent.ACTION_UP){
                if(event.getRawX()>=passwor.getRight()-passwor.getCompoundDrawables()[Right].getBounds().width()){
                    int selection=passwor.getSelectionEnd();
                    if(passwordVisible){
                        passwor.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,  0, R.drawable.ic_baseline_visibility_off_24, 0);
                        passwor.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordVisible=false;
                    }else{
                         passwor.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0,R.drawable.ic_baseline_visibility_24, 0);
                        passwor.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordVisible=true;
                    }
                    passwor.setSelection(selection);
                    return true;
                }
            }
                return false;
            }
        });
        passwordd = findViewById(R.id.passw);
        passwordd.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=passwordd.getRight()-passwordd.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=passwordd.getSelectionEnd();
                        if(passwordVisible){
                            passwordd.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,  0, R.drawable.ic_baseline_visibility_off_24, 0);
                            passwordd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            passwordd.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0,R.drawable.ic_baseline_visibility_24, 0);
                            passwordd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        passwordd.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        passworr = findViewById(R.id.password);
        passworr.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                final int Right=2;
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(event.getRawX()>=passworr.getRight()-passworr.getCompoundDrawables()[Right].getBounds().width()){
                        int selection=passworr.getSelectionEnd();
                        if(passwordVisible){
                            passworr.setCompoundDrawablesRelativeWithIntrinsicBounds( 0,  0, R.drawable.ic_baseline_visibility_off_24, 0);
                            passworr.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible=false;
                        }else{
                            passworr.setCompoundDrawablesRelativeWithIntrinsicBounds( 0, 0,R.drawable.ic_baseline_visibility_24, 0);
                            passworr.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible=true;
                        }
                        passworr.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        String uid = fAuth.getUid();
        // Read from the database
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // for (QueryDocumentSnapshot document : task.getResult()) {
                        passwor.setText(documentSnapshot.getString("password"));
                    }
                });
        saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwor.getText().toString().isEmpty() || passwordd.getText().toString().isEmpty() || passworr.getText().toString().isEmpty()) {
                    Toast.makeText(changePassword.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                    return ;
                }
                String password = passwor.getText().toString();
                user.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = db.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("password", password);
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                           public void onSuccess(Void aVoid) {
                                Toast.makeText(changePassword.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        });
                        Toast.makeText(changePassword.this, "password is changed.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(changePassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                });
            }
        });
    }




}