package com.jim.pocketaccounter.credit.notificat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;


import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
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
	int count=0;
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
			String msg = context.getString(R.string.payment_ends_for_notify);
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
						to.set(Calendar.DAY_OF_MONTH,to.getActualMaximum(Calendar.DAY_OF_MONTH));}
					else{to.set(Calendar.DAY_OF_MONTH,znacVmesyatse);}	}
				else {to.add(Calendar.YEAR, 1);}
				if(to.compareTo(today)>0){
					Intent intent=new Intent(context, AlarmReceiver.class);
					intent.putExtra("msg",msg);
					intent.putExtra("TIP",AlarmReceiver.TO_CRIDET);
					intent.putExtra("title",item.getCredit_name());
					intent.putExtra("icon_number",item.getIcon_ID());
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  ++count,
							intent, 0 );
					alarmManager.set(AlarmManager.RTC_WAKEUP, to.getTimeInMillis(), pendingIntent);
				}
			}
		}
	}

	public void notificSetDebt() {
		cancelAllNotifs();
		Calendar today = Calendar.getInstance();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String time = prefs.getString("planningNotifTime", "09:00");
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		Calendar cal = Calendar.getInstance();
		Date morning_time = null;
		try {
			morning_time = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		cal.setTime(morning_time);

		today.set(Calendar.HOUR_OF_DAY, 9);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);

		for (DebtBorrow item : myDebdbor) {
			if (item.getReturnDate() == null && item.isTo_archive()) continue;
			if (item.getReturnDate() != null) {
				Calendar to = (Calendar) item.getReturnDate().clone();
				to.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
				to.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
				to.set(Calendar.SECOND, 0);

				to.add(Calendar.DAY_OF_YEAR, -1);

				String msg = context.getString(R.string.payment_ends_for_notify);

				if (to.compareTo(today) > 0) {
					Intent intent = new Intent(context, AlarmReceiver.class);
					intent.putExtra("msg", msg);
					intent.putExtra("title", item.getPerson().getName());
					intent.putExtra("TIP", AlarmReceiver.TO_DEBT);
					int _id = (int) (Math.random() * 10000);
					PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ++count,
							intent, 0);
					alarmManager.set(AlarmManager.RTC_WAKEUP, to.getTimeInMillis(), pendingIntent);
				}
			}
		}
	}

	public void cancelAllNotifs() {
		Intent updateServiceIntent = new Intent(context, AlarmReceiver.class);
	  	for (int i = 0; i < 500 ; i++) {
			PendingIntent pendingUpdateIntent = PendingIntent.getService(context, 0, updateServiceIntent, 0);

			try {
				alarmManager.cancel(pendingUpdateIntent);
			} catch (Exception e) {
				Log.e("AlarmSet", "AlarmManager update was not canceled. " + e.toString());
			}
		}

	}
	
}
