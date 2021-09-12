package com.example.sosapp;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity<ScreenOnOffReceiver> extends AppCompatActivity{
    private static final String TAG = "SCREEN_TOGGLE_TAG";
    private TextView textView_location;
    double lat;
    double longitude;
    List<String> myList = new ArrayList<String>();
    LocationManager locationManager;
    DatabaseHelper dbHandler;
    Intent backgroundService;
    private MyReceiver MyReceiver = null;
    //this is the main activity that first open up. this will point to other activities for example register etc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add = findViewById(R.id.addtocontacts);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView_location = findViewById(R.id.text_location);
        Button button_location = findViewById(R.id.button_location);
        dbHandler = new DatabaseHelper(MainActivity.this);
        myList = dbHandler.getEveryone();
        setTitle("dev2qa.com - Keep BroadcastReceiver Running After App Exit.");
        backgroundService = new Intent(getApplicationContext(), ScreenOffBackgroundService.class);
        if (!isMyServiceRunning(ScreenOffBackgroundService.class)){
            startService(backgroundService);
        }

        Log.i(MyReceiver.TAG, "Activity onCreate");
//get permission to track location
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        }
//button location is actually emergency button on mainacitivity

        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    try {
                        GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
                        lat = gpsTracker.getLatitude();
                        longitude = gpsTracker.getLongitude();
                        textView_location.setText("Lat: "+lat + ", " + "Lon: "+ longitude);
                        Toast.makeText(MainActivity.this,"Fetched location successfully",Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"Couldn't fetch location",Toast.LENGTH_SHORT).show();
                    }
                    //checking for permission to send sms
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                            if(lat != 0.0 && longitude != 0.0) {
                                for (int i = 0; i < myList.size(); i++) {
                                    sendSMS(myList.get(i));
                                }
                            }else {
                                Toast.makeText(MainActivity.this, "Coordinate values zero", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                        }
                    }
                }
            });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), database.class);
                startActivity(i);
            }
        });






    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    private void sendSMS(String num){
        String msg = "Help! I am in danger"  + ". location:" + lat + ", " + longitude;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num,null,msg,null,null);
            Toast.makeText(this,"Message successfully sent",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Message couldn't be sent", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        stopService(backgroundService);
        Log.i(MyReceiver.TAG, "Activity onDestroy");
        super.onDestroy();
        // Unregister screenOnOffReceiver when destroy.

        if(MyReceiver!=null)
        {
            unregisterReceiver(MyReceiver);
            Log.i(MyReceiver.TAG, "Service onDestroy: screenOnOffReceiver is unregistered.");
        }
    }

    @Override
    protected void onResume() {

        super.onResume();
    }
}