package com.example.johnathan.vigilate.Notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;

import com.example.johnathan.vigilate.MapsActivity;
import com.example.johnathan.vigilate.R;

/**
 * Created by JohnathanMB on 29/10/2017.
 */

public class Notification {

    private int NOTIFICATION_ID=1;

    public void sendNotification(Context context){
        //construir la acción post press en notificación
        Intent intent = new Intent(context, MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);

        //construir la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_menu_camera);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_menu_camera));
        builder.setContentTitle("¡Sé Un Heroe!");
        builder.setContentText("Alguien Necesita Tu Ayuda");


        //Enviar notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
