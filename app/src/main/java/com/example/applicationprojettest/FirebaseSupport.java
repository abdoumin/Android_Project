package com.example.applicationprojettest;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.example.applicationprojettest.models.Meet;
import com.example.applicationprojettest.models.User;
import com.example.applicationprojettest.recyclerView.CoordonnesData;
import com.example.applicationprojettest.recyclerView.recyclerActivity;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.saivittalb.covsense.NearbyTrackingService;
//import com.saivittalb.covsense.R;
//import com.saivittalb.covsense.models.Meet;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.applicationprojettest.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

import androidx.annotation.NonNull;

public class FirebaseSupport {
    private final String TAG = "MessageService";
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private MessageListener messageListener;
    private Message myUserUIDMessage;
    private String myUserUID;
    //private Context context = this;
    private long onFoundStart = -1;
    public long contactDuration = -1;
    //private static final String TAG = "FirebaseSupport";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseSupport firebasesupport = new FirebaseSupport();
    private CollectionReference CollectionUtilisateurs, CollectionMeetings;
    private String healthStatus;

    FirebaseSupport() {
        CollectionUtilisateurs = db.collection("utilisateurs");
        CollectionMeetings = db.collection("meetings_utilisateurs");
    }

    public static FirebaseSupport getInstance() {
        return firebasesupport;
    }

    public interface StatusDeDonnees {
        void Success();

        void Fail();
    }
    
    public void addUser(User user, final Context context, final StatusDeDonnees status) {
        final DocumentReference nouveauUserRef = CollectionUtilisateurs.document();
        nouveauUserRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences(
                                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.UID), nouveauUserRef.getId());
                        Log.d(TAG, "Un nouvel utilisateur a été ajouté: " + nouveauUserRef.getId());
                        editor.commit();
                        status.Success();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                status.Fail();
            }
        });
    }


    public void updateUserStatus(String monUserID, String nouveauStatus, final StatusDeDonnees status) {
        DocumentReference userToUpdate = CollectionUtilisateurs.document(monUserID);
        Map<String, Object> fieldsUpdated = new HashMap<>();
        fieldsUpdated.put("status", nouveauStatus);
        userToUpdate.update(fieldsUpdated)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });
    }


    public void updateDeviceToken(String myUserID, String deviceToken,
                                  final StatusDeDonnees status) {
        DocumentReference userToUpdate = CollectionUtilisateurs.document(myUserID);
        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("token", deviceToken);
        userToUpdate.update(updatedFields)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        status.Success();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        status.Fail();
                    }
                });

    }

    public void addMeeting(String myUserUID, String metUserUID, Meet meet, final StatusDeDonnees status) {
        CollectionMeetings.document(myUserUID).collection("meetings")
                .document(metUserUID).set(meet)
                .addOnSuccessListener(aVoid -> status.Success())
                .addOnFailureListener(e -> status.Fail());
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        recyclerActivity.myCoordonnes.add(new CoordonnesData(df.format(time),Double.parseDouble(meet.getLatitude()),
                Double.parseDouble(meet.getLongitude()),
                "Diagnostiqué avec COVID"));
        Log.d(TAG, "Added meeting between : " + myUserUID + "and" + metUserUID);
    }



}
