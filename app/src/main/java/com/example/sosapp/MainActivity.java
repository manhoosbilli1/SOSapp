package com.example.sosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationSettingsRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{
    private EditText number, message;
    private Button send, add, button_location;
    private TextView textView_location;
    private FusedLocationProviderClient client;
    databaseHandler myDB;
    private final int REQUEST_CHECK_CODE = 8989;
    private LocationSettingsRequest.Builder builder;
    String x = "", y = "";
    private static final int REQUEST_LOCATION = 1;

    LocationManager locationManager;
    Intent mIntent;

    //this is the main activity that first open up. this will point to other activities for example register etc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = findViewById(R.id.number);
        message = findViewById(R.id.message);
        send = findViewById(R.id.send);
        add = findViewById(R.id.addtocontacts);
        myDB = new databaseHandler(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView_location = findViewById(R.id.text_location);
        button_location = findViewById(R.id.button_location);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

        }

        button_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    GPSTracker gpsTracker = new GPSTracker(MainActivity.this);
                    double lat = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    textView_location.setText("Latitude: "+lat + "," + "Longitude: "+ longitude);
                    Toast.makeText(MainActivity.this,"Fetched location succesfully",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Couldn't fetch location",Toast.LENGTH_SHORT).show();
                }

            }
        });

//        //if gps is not turned on. turn it on
//        if (!locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER )){
//            onGPS();
//        }
//        else {
//            startTrack();
//        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startTrack();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
//                        sendSMS();
//                    }
//                    else {
//                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
//                    }
            }
        });


    }

    private void loadData(){
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){
            Toast.makeText(this,"No content to show",Toast.LENGTH_SHORT).show();
        }
        else {
            String msg = "I AM IN DANGER:" +x+"Longitude:" +y;
            String number = "";
            while (data.moveToNext()){
                theList.add(data.getString(1));
                number = number + data.getString(1) + (data.isLast()?"":";");
                //TODO: tutorial stopped here. continue from here
            }
            if(!theList.isEmpty()){
                //sendSMS();
                Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void sendSMS(){
        String num = number.getText().toString().trim();
        String msg = message.getText().toString().trim();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(num,null,msg,null,null);
            Toast.makeText(this,"Message succesfully sent",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Message couldn't be sent", Toast.LENGTH_SHORT).show();
        }


    }

}