package com.rba.firebaseremoteconfigdemo;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity{


    private AppCompatButton btnAltered;

    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAltered = (AppCompatButton) findViewById(R.id.btnAltered);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        firebaseRemoteConfig.setConfigSettings(configSettings);


        firebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        long cacheExpiration = 3600;

        if(firebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }

        firebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "fetch succeded", Toast.LENGTH_SHORT).show();

                            btnAltered.setAllCaps(firebaseRemoteConfig.getBoolean("button_altered_textAllCaps"));

                            Toast.makeText(MainActivity.this, firebaseRemoteConfig.getString("button_altered_background"), Toast.LENGTH_SHORT).show();
                            btnAltered.setBackgroundColor(Color.parseColor(firebaseRemoteConfig.getString("button_altered_background")));

                            firebaseRemoteConfig.activateFetched();
                        }else{
                            Toast.makeText(MainActivity.this, "fetch failed", Toast.LENGTH_SHORT).show();
                            btnAltered.setAllCaps(firebaseRemoteConfig.getBoolean("button_altered_textAllCaps"));

                            Toast.makeText(MainActivity.this, firebaseRemoteConfig.getString("button_altered_background"), Toast.LENGTH_SHORT).show();
                            btnAltered.setBackgroundColor(Color.parseColor(firebaseRemoteConfig.getString("button_altered_background")));

                        }
                    }
                });

    }

}
