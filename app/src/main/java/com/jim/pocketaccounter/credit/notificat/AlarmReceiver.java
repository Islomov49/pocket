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

        String message = "";
        String title="";
        int which_photo_will_choose=R.drawable.icons_4;

        int tipFragment=0;
        if (intent != null) {
            message = intent.getExtras().getString("msg");
            tipFragment=intent.getExtras().getInt("TIP");
            title=intent.getExtras().getString("title");

        }
        else
            return;
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Class opActivity = PocketAccounter.class;
        Intent resultIntent=new Intent(context, opActivity);
        if(tipFragment==TO_DEBT){
            resultIntent.putExtra("TIP",TO_DEBT);
            which_photo_will_choose=R.drawable.icons_4;
        }
        else if(tipFragment==TO_CRIDET){
            resultIntent.putExtra("TIP",TO_CRIDET);
            which_photo_will_choose=intent.getExtras().getInt("icon_number");
        }

        PendingIntent pIntent=PendingIntent.getActivity(context,0,resultIntent,0);

        NotificationCompat.Builder notif_builder= new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), which_photo_will_choose))
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
