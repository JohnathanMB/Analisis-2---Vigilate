package com.example.johnathan.vigilate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.johnathan.vigilate.Firebase.FirebaseRTDB;
import com.example.johnathan.vigilate.Services.ServiceLocationGPS;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JohnathanMB on 30/10/2017.
 */

public class BtnDetected extends BroadcastReceiver {
    private double lat;
    private double longit;
    ServiceLocationGPS serviceLocationGPS;
    FirebaseRTDB firebaseRTDB = new FirebaseRTDB();
    //int i =0;
    public BtnDetected(){
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        serviceLocationGPS = new ServiceLocationGPS(context);
        lat = serviceLocationGPS.getLat();
        longit = serviceLocationGPS.getLongit();

        firebaseRTDB.addChild(lat, longit);
        Toast.makeText(context, ""+intent.getAction(), Toast.LENGTH_LONG).show();
    }
}
