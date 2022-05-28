package com.example.applicationprojettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationprojettest.models.User;
import com.example.applicationprojettest.R;
import com.example.applicationprojettest.recyclerView.recyclerActivity;

public class LoggedInActivity extends AppCompatActivity {
    private String TAG = "LoggedInActivity";
    private final String[] statuses = {"Sain", "Diagnostiqué avec COVID-19"};
    private final String[] statusDescription = {"Je suis sain", "J'ai reçu un diagnostic de COVID-19"};
    private NumberPicker picker;
    private Button button,recyclerbutton;
    private ProgressBar progressBar;
    private TextView textTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        picker = findViewById(R.id.choisirStatusHealth);
        button = findViewById(R.id.btnUpdateHealthStatus);
        recyclerbutton = findViewById(R.id.recyclerActivity);
        progressBar = findViewById(R.id.progressBar);
        textTracking = findViewById(R.id.textTracking);
        picker.setMinValue(0);
        picker.setMaxValue(statuses.length - 1);
        picker.setDisplayedValues(statusDescription);

        final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);



        final String strUserUID = sharedPreferences.getString(getString(R.string.UID), "Vide");
        String token = sharedPreferences.getString(getString(R.string.token), "None");
        String currentStatus = sharedPreferences.getString(getString(R.string.status), "Vide");
        if (currentStatus.equals(statuses[picker.getValue()])) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }

        FirebaseSupport.getInstance().
                updateDeviceToken(strUserUID, token, new FirebaseSupport.StatusDeDonnees() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Saved token");
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed to save token");
                    }
                });

        picker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            String oldStatus = statuses[oldVal];
            String newStatus = statuses[newVal];
            String currentStatus1 = sharedPreferences.getString(getString(R.string.status), "Vide");
            if (currentStatus1.equals(newStatus)) {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          progressBar.setVisibility(View.VISIBLE);
                                          textTracking.setVisibility(View.GONE);
                                          FirebaseSupport.getInstance().updateUserStatus(strUserUID,
                                                  statuses[picker.getValue()], new FirebaseSupport.StatusDeDonnees() {
                                                      @Override
                                                      public void Success() {
                                                          Toast.makeText(LoggedInActivity.this, "Status Mis A Jour",
                                                                  Toast.LENGTH_LONG).show();
                                                          SharedPreferences.Editor editor = sharedPreferences.edit();
                                                          editor.putString(getString(R.string.status), statuses[picker.getValue()]);
                                                          editor.commit();
                                                          button.setEnabled(false);
                                                          progressBar.setVisibility(View.GONE);
                                                          textTracking.setVisibility(View.VISIBLE);
                                                      }

                                                      @Override
                                                      public void Fail() {
                                                          Toast.makeText(LoggedInActivity.this, "Echec de la mise à jour des status",
                                                                  Toast.LENGTH_LONG).
                                                                  show();
                                                          progressBar.setVisibility(View.GONE);
                                                          textTracking.setVisibility(View.VISIBLE);
                                                      }
                                                  });

                                      }
                                  }
        );

        recyclerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recyclerIntent = new Intent(LoggedInActivity.this, recyclerActivity.class);
                startActivity(recyclerIntent);

            }
        });

        startService(new Intent(this, NearbyTrackingService.class));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, NearbyTrackingService.class));

    }




}
