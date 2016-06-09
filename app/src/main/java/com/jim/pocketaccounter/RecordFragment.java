package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.helper.record.RecordExpanseView;
import com.jim.pocketaccounter.helper.record.RecordIncomesView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint({"InflateParams", "ValidFragment"})
public class RecordFragment extends Fragment {
	private RelativeLayout rlRecordsMain, rlRecordIncomes;
	private Calendar date;
	private ImageView ivToolbar;
	private TextView tvRecordIncome, tvRecordBalanse, tvRecordExpanse;
	public RecordFragment(Calendar date) {this.date = (Calendar)date.clone();}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.record_layout, container, false);
		((PocketAccounter)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((PocketAccounter)getContext()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		PocketAccounter.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PocketAccounter.drawer.openLeftSide();
			}
		});
		PocketAccounter.toolbar.setTitle(getResources().getString(R.string.records));
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
		PocketAccounter.toolbar.setSubtitle(dateFormat.format(date.getTime()));
		ivToolbar = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
		ivToolbar.setImageResource(R.drawable.finance_calendar);
		ivToolbar.setVisibility(View.VISIBLE);
		ivToolbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog=new Dialog(getActivity());
				View dialogView = getActivity().getLayoutInflater().inflate(R.layout.date_picker, null);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.dp);
				ImageView ivDatePickOk = (ImageView) dialogView.findViewById(R.id.ivDatePickOk);
				ivDatePickOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(Calendar.YEAR, dp.getYear());
						calendar.set(Calendar.MONTH, dp.getMonth());
						calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
						((PocketAccounter)getContext()).replaceFragment(new RecordFragment(calendar));
						dialog.dismiss();
					}
				});
				ImageView ivDatePickCancel = (ImageView) dialogView.findViewById(R.id.ivDatePickCancel);
				ivDatePickCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
		((Spinner)PocketAccounter.toolbar.findViewById(R.id.spToolbar)).setVisibility(View.GONE);
		rlRecordsMain = (RelativeLayout) rootView.findViewById(R.id.rlRecordExpanses);
		tvRecordIncome = (TextView) rootView.findViewById(R.id.tvRecordIncome);
		tvRecordBalanse = (TextView) rootView.findViewById(R.id.tvRecordBalanse);
		tvRecordBalanse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				((PocketAccounter)getContext()).replaceFragment(new RecordDetailFragment(date));
			}
		});
		tvRecordExpanse = (TextView) rootView.findViewById(R.id.tvRecordExpanse);
		calclulateBalanse();
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		int side = 0;
		if (height*0.6 > width) 
			side =  width;
		else
			side = (int)(height*0.6);
		RecordExpanseView rv = new RecordExpanseView(getActivity());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(side, side);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rv.setLayoutParams(lp);
		rlRecordsMain.addView(rv);
		rlRecordIncomes = (RelativeLayout) rootView.findViewById(R.id.rlRecordIncomes);
		RecordIncomesView rIncomes = new RecordIncomesView(getActivity());
		RelativeLayout.LayoutParams lpIncomes = new RelativeLayout.LayoutParams(width, width/4+(int)(getResources().getDimension(R.dimen.thirty_dp)));
		lpIncomes.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rIncomes.setLayoutParams(lpIncomes);
		rlRecordIncomes.addView(rIncomes);
		return rootView;
	}
	public void calclulateBalanse() {
		Calendar begTime = (Calendar)date.clone();
		begTime.set(Calendar.HOUR_OF_DAY, 0);
		begTime.set(Calendar.MINUTE, 0);
		begTime.set(Calendar.SECOND, 0);
		begTime.set(Calendar.MILLISECOND, 0);
		Calendar endTime = (Calendar)date.clone();
		endTime.set(Calendar.HOUR_OF_DAY, 23);
		endTime.set(Calendar.MINUTE, 59);
		endTime.set(Calendar.SECOND, 59);
		endTime.set(Calendar.MILLISECOND, 59);
		ArrayList<FinanceRecord> records = new ArrayList<FinanceRecord>();
		for (int i=0; i<PocketAccounter.financeManager.getRecords().size(); i++) {
			if (PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(begTime)>=0 &&
					PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(endTime)<=0)
				records.add(PocketAccounter.financeManager.getRecords().get(i));
		}
		double income = 0.0, expanse = 0.0, balanse = 0.0;
		for (int i=0; i<records.size(); i++) {
			if (records.get(i).getCategory().getType() == PocketAccounterGeneral.INCOME)
				income = income + PocketAccounterGeneral.getCost(records.get(i));
			else
				expanse = expanse + PocketAccounterGeneral.getCost(records.get(i));
		}
		balanse = income - expanse;
		String mainCurrencyAbbr = PocketAccounter.financeManager.getMainCurrency().getAbbr();
		DecimalFormat decFormat = new DecimalFormat("0.00");
		tvRecordIncome.setText(getResources().getString(R.string.income)+": "+decFormat.format(income)+mainCurrencyAbbr);
		tvRecordExpanse.setText(getResources().getString(R.string.expanse)+": "+decFormat.format(expanse)+mainCurrencyAbbr);
		tvRecordBalanse.setText(getResources().getString(R.string.balanse)+": "+decFormat.format(balanse)+mainCurrencyAbbr);
	}
	@Override
	public void onStop() {
		super.onStop();
		PocketAccounter.financeManager.saveRecords();
	}
	public void setDate(Calendar date) {this.date = (Calendar)date.clone();}
	public Calendar getDate() {return date;}
}