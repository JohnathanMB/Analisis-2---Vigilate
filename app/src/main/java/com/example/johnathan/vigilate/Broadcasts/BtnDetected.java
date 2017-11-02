package com.example.johnathan.vigilate.Broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.johnathan.vigilate.Firebase.FirebaseRTDB;
import com.example.johnathan.vigilate.MainActivity;
import com.example.johnathan.vigilate.Services.ServiceLocationGPS;

/**
 * Created by JohnathanMB on 30/10/2017.
 */

public class BtnDetected extends BroadcastReceiver {
    private double lat;
    private double longit;
    ServiceLocationGPS serviceLocationGPS;
    FirebaseRTDB firebaseRTDB = new FirebaseRTDB();
    private boolean alarmSent = false;
    private SharedPreferences settings;


    public BtnDetected(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        settings = context.getSharedPreferences(MainActivity.NAME_SHAREDPREFERENCE_SETTING, Context.MODE_PRIVATE);

        //logicia sobre si está activo la función de detectar el botón o no
        boolean btnDectedActived = settings.getBoolean(MainActivity.NAME_BTNDETECTED_ACTIVED,false);
        if(btnDectedActived){
            alarmSent = true;
            //falta lógica para secuencia de botones
            Intent serviceSendLocation = new Intent(context, ServiceLocationGPS.class);
            context.startService(serviceSendLocation);
        }
    }

    public void setAlarmSent(Boolean changeAlarm){
        alarmSent = changeAlarm;
    }

    public boolean getAlarmSent(){
        return alarmSent;
    }
}
