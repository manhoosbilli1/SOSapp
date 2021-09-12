package com.example.sosapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

public class MyReceiver extends BroadcastReceiver {
    public static final String TAG = "SCREEN_TOGGLE_TAG";
    public static int counter = 0;
    public static int previousTime;
    public static int currentTime;
    public static boolean isTime=false;
    @Override
    public void onReceive(Context context, @NonNull Intent intent) {


        Log.i(ScreenOffBackgroundService.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, ScreenOffBackgroundService.class));;
        String action = intent.getAction();
        Log.i(TAG, "Receiver triggered.");
        if(Intent.ACTION_SCREEN_OFF.equals(action))
        {
//            currentTime = (int) System.currentTimeMillis();
//            previousTime = (int) System.currentTimeMillis();
//            //check for quick succession
//            //if this receiver actives in less than a half a second, count that as increment
//            if (currentTime - previousTime <= 500){
//                counter++;
//                if(counter == 3){
//                    isTime = true;
//                }
//            } else {
//                //reset the counter since the timer ran out.
//                counter = 0;
//            }
//
//            if(isTime){
//
//                isTime = false;
//                counter =0;
//            }
            Log.i(TAG, "Screen is turn off.");
        }else if(Intent.ACTION_SCREEN_ON.equals(action))
        {
            Log.i(TAG, "Screen is turn on.");
        }}
}