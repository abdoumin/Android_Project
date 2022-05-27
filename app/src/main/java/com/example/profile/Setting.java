package com.example.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.view.View;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth;


public class Setting extends AppCompatActivity {
     private TextView  TXT,log,pass;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.darkTheme);
        } else {
            setTheme(R.style.lightTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settin);
        this.TXT= (TextView) findViewById(R.id.TXT);
        TXT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view ){
              Intent otherActivity = new Intent(Setting.this,settin.class);
              startActivity(otherActivity);
              finish();
            }
                               }
        );
        fAuth = FirebaseAuth.getInstance();
        this.log = (TextView) findViewById(R.id.logg);
        log.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       fAuth.signOut();
                                       signOutUser();
                                   }
                               }
        );
        this.pass= (TextView) findViewById(R.id.pass);
        pass.setOnClickListener(new View.OnClickListener(){
                                   @Override
                                   public void onClick(View view ){
                                       Intent otherActivity = new Intent(Setting.this,changePassword.class);
                                       startActivity(otherActivity);
                                       finish();
                                   }
                               }
        );

        Switch switchtheme = (Switch) findViewById(R.id.switch1);
        switchtheme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
    private void signOutUser () {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}