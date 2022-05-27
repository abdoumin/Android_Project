package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.util.Log;
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

public class settin extends AppCompatActivity {

    private TextView occupationTxtView, lsnameTxtView, frsnameTxtView;
    private EditText emailTxt;
    private ImageView img;
    private ImageView userImageView;
    private String _USERNAME, _NAME, _EMAIL, _PASSWORD;
    private final String TAG = this.getClass().getName().toUpperCase();
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    FirebaseAuth fAuth;
    FirebaseUser user;
    Button saveBtn;
    private Map<String, String> userMap;
    private String email;
    private static final String USERS = "user";

    protected void onCreate(Bundle savedInstanceState) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.lightTheme);}
        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       Intent otherActivity = new Intent(settin.this, Setting.class);
                                       startActivity(otherActivity);
                                       finish();
                                   }
                               }
        );



        //receive data from login screen
        //  Intent intent = new Intent(context, LoginActivity.class);

        // iduser = intent.getStringExtra("userID");
        occupationTxtView = findViewById(R.id.occupation_textview);
        frsnameTxtView = findViewById(R.id.frsname_textview);
        lsnameTxtView = findViewById(R.id.name_textview);
        this.saveBtn = findViewById(R.id.button_send);
        emailTxt = findViewById(R.id.editText);


        userImageView = findViewById(R.id.user_imageview);
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
                        lsnameTxtView.setText(documentSnapshot.getString("name"));
                        frsnameTxtView.setText(documentSnapshot.getString("first name"));
                        emailTxt.setText(documentSnapshot.getString("email"));
                        occupationTxtView.setText(documentSnapshot.getString("name") + documentSnapshot.getString("first name"));
                        //  }
                    }
                });




    /*  public void update(View view ){
            occupationTxtView = findViewById(R.id.occupation_textview);
            frsnameTxtView = findViewById(R.id.frsname_textview);
            lsnameTxtView = findViewById(R.id.name_textview);

            emailTxt = findViewById(R.id.editText);

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child(USERS);
            Log.v("USERID", userRef.getKey());
            userRef.child("1").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dataSnapshot.getRef().child("frstname").setValue(frsnameTxtView.getText().toString());


                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("User", databaseError.getMessage());
                }

            });
            Toast toast = Toast.makeText( getApplicationContext() , "Data hase been update", Toast.LENGTH_LONG);
            toast.show();

        }
     */



        saveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (frsnameTxtView.getText().toString().isEmpty() || emailTxt.getText().toString().isEmpty() || lsnameTxtView.getText().toString().isEmpty()) {
                Toast.makeText(settin.this, "One or Many fields are empty.", Toast.LENGTH_SHORT).show();
                return ;
            }
            final String email = emailTxt.getText().toString();
            user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    DocumentReference docRef = db.collection("users").document(user.getUid());
                    Map<String, Object> edited = new HashMap<>();
                    edited.put("email", email);
                    edited.put("name", frsnameTxtView.getText().toString());
                    edited.put("first name", lsnameTxtView.getText().toString());
                    docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(settin.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    });
                    Toast.makeText(settin.this, "Email is changed.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(settin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            });
    }
    });

    }


}
