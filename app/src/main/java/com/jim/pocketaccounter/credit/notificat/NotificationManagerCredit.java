package com.jim.pocketaccounter.credit.notificat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.debt.DebtBorrow;
import com.jim.pocketaccounter.finance.FinanceManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class NotificationManagerCredit {
	
	private Context context;
	private int OK=0;
	private AlarmManager alarmManager;
	private ArrayList<CreditDetials> myCredits;
	private ArrayList<DebtBorrow> myDebdbor;
	private FinanceManager myFinance;
	Random rand;
	final static long forDay=1000L*60L*60L*24L;
	final static long forMoth=1000L*60L*60L*24L*30L;
	final static long forYear=1000L*60L*60L*24L*365L;
	final static long forWeek=1000L*60L*60L*24L*7L;
	public NotificationManagerCredit(Context context) {
		this.context = context;
		myFinance= PocketAccounter.financeManager;
		myCredits= myFinance.loadCredits();
		myDebdbor=myFinance.loadDebtBorrows();
		alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		rand=new Random(10000000);

	}
		
	public void notificSetCredit()  {

		cancelAllNotifs();
		Calendar today = Calendar.getInstance();

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String time = prefs.getString("planningNotifTime", "09:00");

		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date morning_time=null;
		try {
			morning_time=df.parse(time);

		}
		 catch (ParseException e) {
			e.printStackTrace();
			 return;
		}
		cal.setTime(morning_time);

		today.set(Calendar.HOUR_OF_DAY, 9);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		for (CreditDetials item:myCredits) {
		Calendar to= (Calendar) item.getTake_time().clone();
			to.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
			to.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
			to.set(Calendar.SECOND, 0);

			to.add(Calendar.DAY_OF_YEAR,-1);
			int znacVmesyatse=to.get(Calendar.DAY_OF_MONTH);

			long period_tip=item.getProcent_interval();
			long period_voqt=item.getPeriod_time();

			if(period_tip==forDay||item.isKey_for_archive()){
				continue;}

			int voqt_soni= (int) (period_voqt/period_tip);
			Log.d("AlarmSet","Marta "+voqt_soni+"---"+znacVmesyatse);

			String msg = "Credit on behalf of "+item.getCredit_name()+ " tomorrow . If you have not paid pay today !";
			for (int i = 0; i < voqt_soni ; i++) {
				if(period_tip==forDay){
					to.add(Calendar.DAY_OF_YEAR, 1);
				}
				else if(period_tip==forWeek){
					to.add(Calendar.WEEK_OF_YEAR, 1);
				}
				else if(period_tip==forMoth){

					to.add(Calendar.MONTH, 1);
					if(to.getActualMaximum(Calendar.DAY_OF_MONTH)<znacVmesyatse){
						to.set(Calendar.DAY_OF_MONTH,to.getActualMaximum(Calendar.DAY_OF_MONTH));
					}
					else{
						to.set(Calendar.DAY_OF_MONTH,znacVmesyatse);
					}
				}
				else {
					to.add(Calendar.YEAR, 1);
				}
				if(to.compareTo(today)>0){
					Intent intent=new Intent(context, AlarmReceiver.class);
					intent.putExtra("msg",msg);
					intent.putExtra("TIP",AlarmReceiver.TO_CRIDET);

					int _id=rand.nextInt();
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  (_id<0)?_id*(-1):_id,
							intent, 0 );
					SimpleDateFormat dff = new SimpleDateFormat("dd.MM.yyyy HH:mm");
					Log.d("AlarmSet",""+dff.format(to.getTime())+" - "+_id);

					alarmManager.set(AlarmManager.RTC_WAKEUP, to.getTimeInMillis(), pendingIntent);

				}
			}
		}

	}
	
	public void cancelAllNotifs() {
		Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
	    PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);
	    try {
	        alarmManager.cancel(pendingUpdateIntent);
	    } catch (Exception e) {
	        Log.e("AlarmSet", "AlarmManager update was not canceled. " + e.toString());
	    }
	}
	
}
