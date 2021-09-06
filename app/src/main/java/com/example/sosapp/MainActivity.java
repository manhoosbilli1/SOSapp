package com.example.sosapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity{
    private EditText number;
    private TextView textView_location;
    databaseHandler myDB;
    double lat;
    double longitude;

    LocationManager locationManager;

    //this is the main activity that first open up. this will point to other activities for example register etc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = findViewById(R.id.number);
        Button add = findViewById(R.id.addtocontacts);
        myDB = new databaseHandler(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        textView_location = findViewById(R.id.text_location);
        Button button_location = findViewById(R.id.button_location);

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
                    lat = gpsTracker.getLatitude();
                    longitude = gpsTracker.getLongitude();
                    textView_location.setText("Lat: "+lat + ", " + "Lon: "+ longitude);
                    Toast.makeText(MainActivity.this,"Fetched location successfully",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,"Couldn't fetch location",Toast.LENGTH_SHORT).show();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        if(lat != 0.0 && longitude != 0.0) {
                            sendSMS();
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
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
            }
        });



    }

//    private void loadData(){
//        ArrayList<String> theList = new ArrayList<>();
//        Cursor data = myDB.getListContents();
//        if(data.getCount() == 0){
//            Toast.makeText(this,"No content to show",Toast.LENGTH_SHORT).show();
//        }
//        else {
//            String msg = "I AM IN DANGER:" +x+"Longitude:" +y;
//            String number = "";
//            while (data.moveToNext()){
//                theList.add(data.getString(1));
//                number = number + data.getString(1) + (data.isLast()?"":";");
//                //TODO: tutorial stopped here. continue from here
//            }
//            if(!theList.isEmpty()){
//                //sendSMS();
//                Toast.makeText(this, "List is empty", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }



    private void sendSMS(){
        String num = number.getText().toString().trim();
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

}