package com.example.applicationprojettest.recyclerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationprojettest.FirebaseSupport;
import com.example.applicationprojettest.R;
import com.example.applicationprojettest.models.Meet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class recyclerActivity extends AppCompatActivity {
    Meet meet = null;
    String TAG = "";
    private Context context = this;
    FusedLocationProviderClient client;
    public static ArrayList<CoordonnesData> myCoordonnes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Meet meet = new Meet("33.9752 ","-6.874959","18/04/2022");
        Meet meet2 = new Meet("34.002135","-6.855586","18/04/2022");
        FirebaseSupport.getInstance().addMeeting("iZr9wzluM3e7tTDRoKlRZt0bE1v1", "Fv2ll3IAFVMNnSYuvojOX4UCqfW2"
                ,
                meet, new FirebaseSupport.StatusDeDonnees() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Inserted user");
                        Toast.makeText(context, "Inserted meeting", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed inserting user");
                        Toast.makeText(context, "Failed to insert meeting",
                                Toast.LENGTH_LONG).show();


                    }
                });

        FirebaseSupport.getInstance().addMeeting("iZr9wzluM3e7tTDRoKlRZt0bE1v1", "Fv2ll3IAFVMNnSYuvojOX4UCqfW2"
                ,
                meet2, new FirebaseSupport.StatusDeDonnees() {
                    @Override
                    public void Success() {
                        Log.d(TAG, "Inserted user");
                        Toast.makeText(context, "Inserted meeting", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void Fail() {
                        Log.d(TAG, "Failed inserting user");
                        Toast.makeText(context, "Failed to insert meeting",
                                Toast.LENGTH_LONG).show();


                    }
                });

        LogAdapter logAdapter = new LogAdapter(recyclerActivity.myCoordonnes,recyclerActivity.this);
        recyclerView.setAdapter(logAdapter);

    }



}
