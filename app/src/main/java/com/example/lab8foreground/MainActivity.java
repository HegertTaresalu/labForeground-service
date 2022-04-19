package com.example.lab8foreground;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;

import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.widget.EditText;

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
            snackbar.show();

        }
        else {
            Log.i("MainActivity", "Foreground Service not running");
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        boolean areNotificationEnabled = notificationManager.areNotificationsEnabled();
        if (!areNotificationEnabled){
            Snackbar snackbar = Snackbar.make(contained, R.string.enabled, Snackbar.LENGTH_INDEFINITE)
                    .setAction("Enable", view -> openNotificationSettingsForApp());
            snackbar.show();
        }

        findViewById(R.id.button).setOnClickListener(view -> {
            EditText input = view.findViewById(R.id.editTextTextPersonName);
            String text = input.getText().toString();
            Intent foregroundStart = new Intent(this,ForegroundService.class);
            foregroundStart.putExtra("input",text);
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O){
                startForegroundService(foregroundStart);

            }
            else {
                startService(foregroundStart);
            }
        });

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
    private void openNotificationSettingsForApp(){
        Intent intent = new Intent();
        intent.setAction("android,settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("app_package",getPackageName());
        intent.putExtra("app_uid",getApplicationInfo().uid);
        intent.putExtra("android.provider.extra.APP_PACKAGE",getPackageName());
        startActivity(intent);
    }
}