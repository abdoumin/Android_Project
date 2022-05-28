package com.example.applicationprojettest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.applicationprojettest.covidApi.MenuActivity;
import com.example.applicationprojettest.gmailauthentification.MainActivity;
import com.example.applicationprojettest.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.concurrent.TimeUnit;

public class PhoneRegisterActivity extends AppCompatActivity {
    private static final String TAG = "PhoneRegisterActivity";
    EditText phone, otp;
    Context context;
    Button btngenOTP, btnverify;
    FirebaseAuth mAuth;
    String verificationID;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
        context = this;
        phone = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        btngenOTP = findViewById(R.id.btngenerateOTP);
        btnverify =findViewById(R.id.btnverifyOTP);
        mAuth = FirebaseAuth.getInstance();
        /*// set this to remove reCaptcha web
        mAuth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true);*/
        bar = findViewById(R.id.bar);
        btngenOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(phone.getText().toString()))
                {
                    Toast.makeText(PhoneRegisterActivity.this, "Enter Valid Phone No.", Toast.LENGTH_SHORT).show();
                }
                else {
                    String number = phone.getText().toString();
                    bar.setVisibility(View.VISIBLE);
                    sendverificationcode(number);
                }
            }
        });
        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(TextUtils.isEmpty(otp.getText().toString()))
                {
                    Toast.makeText(PhoneRegisterActivity.this, "Wrong OTP Entered", Toast.LENGTH_SHORT).show();
                }
                else
                    verifycode(otp.getText().toString());
            }
        });
    }

    private void sendverificationcode(String phoneNumber)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential)
        {
            final String code = credential.getSmsCode();
            if(code!=null)
                {
                verifycode(code);
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneRegisterActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token)
        {
            super.onCodeSent(s, token);
            verificationID = s;
            Toast.makeText(PhoneRegisterActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            btnverify.setEnabled(true);
            bar.setVisibility(View.INVISIBLE);
        }};
    private void verifycode(String Code)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,Code);
        signinbyCredentials(credential);
    }

    private void signinbyCredentials(PhoneAuthCredential credential)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(PhoneRegisterActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {
                                                Log.w(TAG, "getInstanceId failed", task.getException());
                                                return;
                                            }

                                            String token = task.getResult().getToken();
                                            Log.d(TAG, token);
                                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                                                    Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(getString(R.string.token), token);
                                            editor.commit();
                                        }
                                    });
                            final String phoneNumber = phone.getText().toString();
                            final User newUser = new User(phoneNumber, "Healthy");
                            FirebaseSupport.getInstance().addUser(newUser, context,
                                    new FirebaseSupport.StatusDeDonnees() {
                                        @Override
                                        public void Success() {
                                            Log.d(TAG, "Ajout d'un nouvel utilisateur");
                                            final SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                                                    Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(getString(R.string.phone), phoneNumber);
                                            editor.putString(getString(R.string.status), "Sain");
                                            editor.putBoolean(getString(R.string.inscrit), true);
                                            editor.commit();
                                            Intent intent = new Intent(context, MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }

                                        @Override
                                        public void Fail() {
                                            Log.d(TAG, "Echec d'ajout d'un nouvel utilisateur");

                                        }
                                    });

                        }

                    }
                });}}
                
