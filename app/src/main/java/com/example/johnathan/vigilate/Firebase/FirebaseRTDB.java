package com.example.johnathan.vigilate.Firebase;

import android.provider.ContactsContract;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by JohnathanMB on 30/10/2017.
 */

public class FirebaseRTDB {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();


    public void addChild(double lat, double longit){
        final DatabaseReference newHelp = database.getReference(FirebaseRefencesRTDB.NEW_HELP);
        DatabaseReference newHelpAdded = newHelp.push();
        newHelpAdded.child(New_Help.FIELD_LAT).setValue(lat);
        newHelpAdded.child(New_Help.FIELD_LONG).setValue(longit);
    }

}