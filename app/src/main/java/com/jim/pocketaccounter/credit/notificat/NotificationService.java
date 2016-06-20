package com.jim.pocketaccounter.credit.notificat;


import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class NotificationService extends Service {
	public static final int TO_DEBT=0,TO_CRIDET=1;
    @Override
	public IBinder onBind(Intent intent) {
    	return null;
	}
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		long[] vibration = {0, 100, 200, 300, 400};
		String message = "";
		int tipFragment=0;
		if (intent != null) {
			message = intent.getExtras().getString("msg");
			tipFragment=intent.getExtras().getInt("TIP");
				}
		else 
			stopSelf();
        Log.d("AlarmSet",message);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Class opActivity = PocketAccounter.class;
		Intent resultIntent=new Intent(this, opActivity);
		if(tipFragment==TO_DEBT){
			resultIntent.putExtra("TIP",TO_DEBT);
		}
		else if(tipFragment==TO_CRIDET){
			resultIntent.putExtra("TIP",TO_DEBT);
		}

		PendingIntent pIntent=PendingIntent.getActivity(this,0,resultIntent,0);
		String title = getResources().getString(R.string.app_name);

		NotificationCompat.Builder notif_builder= new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher))
				.setAutoCancel(true)
				.setDefaults(Notification.DEFAULT_SOUND )
				.setContentTitle(title)
				.setContentText(message)
				.setContentIntent(pIntent)
                .setLights(Color.GREEN, 500, 500)
				.setSound(alarmSound);

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(1, notif_builder.build());


        return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		stopSelf();
	}
	
	
}
