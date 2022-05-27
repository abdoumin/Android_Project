package com.example.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button join_us;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        join_us = findViewById(R.id.join_us);
        login = findViewById(R.id.login);
        join_us.setOnClickListener(view -> {
        Intent intent = new Intent(getApplicationContext(), InscriptionActivity.class);
        startActivity(intent);
    });
        login.setOnClickListener(view -> {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    });
    }
}
