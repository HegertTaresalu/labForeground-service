package com.example.lab8foreground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout contained = findViewById(R.id.myConstraintlayout);
        if (isMyServiceRunning(ForegroundService.class)){
            findViewById(R.id.button).setEnabled(false);
            Snackbar snackbar = Snackbar.make(contained,R.string.stop,Snackbar.LENGTH_INDEFINITE).setAction("Stop",view -> stopMyService());

        }
        else {
            Log.i("MainActivity", "Foreground Service not running");
        }
    }

    private void stopMyService() {
        Intent stopMyForegroundService = new Intent(this,ForegroundService.class);
        stopService(stopMyForegroundService);
        finish();
    }

    private boolean isMyServiceRunning(Class<ForegroundService> ServiceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo info : manager.getRunningServices(Integer.MAX_VALUE)){
            if (ServiceClass.getName().equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }
}