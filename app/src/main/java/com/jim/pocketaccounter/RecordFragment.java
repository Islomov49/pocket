package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jim.pocketaccounter.helper.record.RecordExpanseView;
import com.jim.pocketaccounter.helper.record.RecordIncomesView;

import java.util.Calendar;

@SuppressLint({"InflateParams", "ValidFragment"})
public class RecordFragment extends Fragment {
	private RelativeLayout rlRecordsMain, rlRecordIncomes;
	private Calendar date;
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
		rlRecordsMain = (RelativeLayout) rootView.findViewById(R.id.rlRecordExpanses);
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
	@Override
	public void onStop() {
		super.onStop();
		PocketAccounter.financeManager.saveRecords();
	}
	public void setDate(Calendar date) {this.date = (Calendar)date.clone();}
	public Calendar getDate() {return date;}
}