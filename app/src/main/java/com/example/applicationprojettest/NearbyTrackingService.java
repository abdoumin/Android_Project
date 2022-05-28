package com.example.applicationprojettest;

import static com.example.applicationprojettest.Utils.writeToStorage;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.applicationprojettest.recyclerView.CoordonnesData;
import com.example.applicationprojettest.recyclerView.recyclerActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.example.applicationprojettest.models.Meet;
import com.example.applicationprojettest.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NearbyTrackingService extends Service {
    private final String TAG = "MessageService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private MessageListener messageListener;
    private Message myUserUIDMessage;
    private String myUserUID;
    private Context context = this;
    private long onFoundStart = -1;
    public long contactDuration = -1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Service crée");
        SharedPreferences prefs = this.getSharedPreferences
                (getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        /*UID est User ID qui represente l'ID du document dans la DB Firestore
        ou est enregistree l'utilisateur*/
        myUserUID = prefs.getString(getString(R.string.UID), "None");

        myUserUIDMessage = new Message(myUserUID.getBytes());
        Log.d(TAG,"Je suis au dessus de messageListener");
        messageListener = new MessageListener() {
            @SuppressLint("MissingPermission")
            @Override
            /*Cette methode est declenchee quand un message est recu
            * de la part d'un autre utilisateur à travers la methode
            * suscribe*/
            public void onFound(Message message) {
                String metUserUID = new String(message.getContent());
                Log.d(TAG, "Found user: " + metUserUID);
                Meet meet = new Meet( "31.0215", "-74.013382",
                        "28/05/2020 8:29:03");

                onFoundStart = System.currentTimeMillis();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date time = Calendar.getInstance().getTime();
                String formattedDate = df.format(time);

                LocationRequest mLocationRequest = new LocationRequest();

                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                getFusedLocationProviderClient(getApplicationContext()).getLastLocation().
                        addOnSuccessListener(location -> {
                    String filePath = getApplicationContext().getFilesDir()
                            .toString() + "/meetings" + "/" + metUserUID;
                    writeToStorage(filePath , "latitude.txt",
                            String.valueOf(location.getLatitude()) );
                            meet.setLongitude(String.valueOf(location.getLongitude()));
                            meet.setLatitude(String.valueOf(location.getLatitude()));

                    writeToStorage(filePath , "longitude.txt",
                            String.valueOf(location.getLongitude()));
                });

                String filePath = getApplicationContext().getFilesDir().toString()
                        + "/meetings" + "/" + metUserUID;
                writeToStorage(filePath , "date.txt", formattedDate);



                FirebaseSupport.getInstance().addMeeting(myUserUID, metUserUID,
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
            }

        };

    }


    /*Cette methode est déclenchée quand on appelle la methode startService*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "Started tracking", Toast.LENGTH_LONG).show();

        Nearby.getMessagesClient(this).publish(myUserUIDMessage);
        Nearby.getMessagesClient(this).subscribe(messageListener);

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, LoggedInActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Tracking of people nearby")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Nearby.getMessagesClient(this).unpublish(myUserUIDMessage);
        Nearby.getMessagesClient(this).unsubscribe(messageListener);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    /*###############################################################*/



}
