package com.jim.pocketaccounter.credit.notificat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;

public class AlarmReceiver extends BroadcastReceiver
{
    public static final int TO_DEBT=10,TO_CRIDET=11;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("AlarmSet","test");
        long[] vibration = {0, 100, 200, 300, 400};
        String message = "";
        int tipFragment=0;
        if (intent != null) {
            message = intent.getExtras().getString("msg");
            tipFragment=intent.getExtras().getInt("TIP");
        }
        else
            return;
        Log.d("AlarmSet","test");
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Class opActivity = PocketAccounter.class;
        Intent resultIntent=new Intent(context, opActivity);
        if(tipFragment==TO_DEBT){
            resultIntent.putExtra("TIP",TO_DEBT);
        }
        else if(tipFragment==TO_CRIDET){
            resultIntent.putExtra("TIP",TO_CRIDET);
        }

        PendingIntent pIntent=PendingIntent.getActivity(context,0,resultIntent,0);
        String title = context.getResources().getString(R.string.app_name);

        NotificationCompat.Builder notif_builder= new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND )
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pIntent)
                .setLights(Color.GREEN, 500, 500)
                .setSound(alarmSound);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notif_builder.build());



    }   
}
