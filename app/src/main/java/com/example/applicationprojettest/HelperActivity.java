package com.example.applicationprojettest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.applicationprojettest.R;
import com.example.applicationprojettest.covidApi.MenuActivity;
import com.example.applicationprojettest.gmailauthentification.MainActivity;

public class HelperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        /*sharedPreferences.edit().clear().commit();*/

        boolean isRegistered = sharedPreferences.getBoolean(getString(R.string.inscrit), false);
        Intent intent;
        if (isRegistered) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, DescriptionActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}
