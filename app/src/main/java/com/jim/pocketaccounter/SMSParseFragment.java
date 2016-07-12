package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.jim.pocketaccounter.helper.FloatingActionButton;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.helper.SmsParseObject;

import java.util.ArrayList;

@SuppressLint("InflateParams")
public class SMSParseFragment extends Fragment {
	private FloatingActionButton fabSmsParse;
	private RecyclerView rvSmsParseList;
	private boolean[] selected;
	private int mode = PocketAccounterGeneral.NORMAL_MODE;
	private ImageView ivToolbarMostRight;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.sms_parse_layout, container, false);
		ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
		ivToolbarMostRight.setImageResource(R.drawable.pencil);
		ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				setMode(mode);
			}
		});
		rvSmsParseList = (RecyclerView) rootView.findViewById(R.id.rvSmsParseList);
		rvSmsParseList.setLayoutManager(new LinearLayoutManager(getContext()));
		fabSmsParse = (FloatingActionButton)  rootView.findViewById(R.id.fabSmsParse);
		Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.add_green);
		int size = (int) getResources().getDimension(R.dimen.thirty_dp);
		Bitmap add = Bitmap.createScaledBitmap(temp, size, size, false);
		fabSmsParse.setImageBitmap(add);
		fabSmsParse.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				((PocketAccounter)getContext()).replaceFragment(new SMSParseEditFragment(null));
			}
		});
		refreshList();
		return rootView;
	}
	private void refreshList() {
		MyAdapter adapter = new MyAdapter(PocketAccounter.financeManager.getSmsObjects());
		rvSmsParseList.setAdapter(adapter);
	}
	private void setMode(int mode) {
		if (mode == PocketAccounterGeneral.NORMAL_MODE) {
			ivToolbarMostRight.setImageResource(R.drawable.trash);
			this.mode = PocketAccounterGeneral.EDIT_MODE;
			selected = new boolean[PocketAccounter.financeManager.getSmsObjects().size()];
			RecyclerView.Adapter adapter = rvSmsParseList.getAdapter();
			for (int i=0; i<adapter.getItemCount(); i++)
				adapter.notifyItemChanged(i);
		}
		else {
			ivToolbarMostRight.setImageResource(R.drawable.pencil);
			this.mode = PocketAccounterGeneral.NORMAL_MODE;
			RecyclerView.Adapter adapter = rvSmsParseList.getAdapter();
			for (int i=0; i<adapter.getItemCount(); i++)
				adapter.notifyItemChanged(i);
			deleteSelected();
			selected = null;
		}

	}
	private void deleteSelected() {
		for (int i=0; i<selected.length; i++) {
			if (selected[i])
				PocketAccounter.financeManager.getSmsObjects().set(i, null);
		}
		for (int i=0; i<PocketAccounter.financeManager.getSmsObjects().size(); i++) {
			if (PocketAccounter.financeManager.getSmsObjects().get(i) == null) {
				PocketAccounter.financeManager.getSmsObjects().remove(i);
				i--;
			}
		}
		RecyclerView.Adapter adapter = rvSmsParseList.getAdapter();
		for (int i=0; i<adapter.getItemCount(); i++)
			adapter.notifyItemRemoved(i);
		PocketAccounter.financeManager.saveSmsObjects();
	}
	private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
		private ArrayList<SmsParseObject> objects;
		public MyAdapter(ArrayList<SmsParseObject> objects) {
			this.objects = objects;
		}
		public int getItemCount() {
			return objects.size();
		}
		public void onBindViewHolder(final ViewHolder view, final int position) {
			view.tvSmsParseItemNumber.setText(objects.get(position).getNumber());
			String text = "";
			if (objects.get(position).getType() == PocketAccounterGeneral.SMS_ONLY_EXPENSE)
				text = text + getResources().getString(R.string.only_expense);
			else if (objects.get(position).getType() == PocketAccounterGeneral.SMS_ONLY_EXPENSE)
				text = text + getResources().getString(R.string.only_income);
			else {
				text = text + getResources().getString(R.string.income_keywords)+objects.get(position).getIncomeWords()+"\n"+
						getResources().getString(R.string.expense_keywords)+objects.get(position).getExpenseWords()+"\n";
			}
			text = text + getResources().getString(R.string.amount_keywords)+objects.get(position).getAmountWords();
			view.tvSmsParsingItemInfo.setText(text);
			if (mode == PocketAccounterGeneral.NORMAL_MODE)
				view.chbSmsObjectItem.setVisibility(View.GONE);
			else {
				view.chbSmsObjectItem.setVisibility(View.VISIBLE);
				view.chbSmsObjectItem.setChecked(selected[position]);
			}
			view.rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mode == PocketAccounterGeneral.NORMAL_MODE) {
						((PocketAccounter)getContext()).replaceFragment(new SMSParseEditFragment(objects.get(position)));
					}
					else {
						view.chbSmsObjectItem.setChecked(!view.chbSmsObjectItem.isChecked());
						selected[position] = view.chbSmsObjectItem.isChecked();
					}
				}
			});
		}
		public ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_object_list_item, parent, false);
			return new ViewHolder(view);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public CheckBox chbSmsObjectItem;
		public TextView tvSmsParseItemNumber;
		public TextView tvSmsParsingItemInfo;
		public View rootView;
		public ViewHolder(View view) {
			super(view);
			chbSmsObjectItem = (CheckBox) view.findViewById(R.id.chbSmsObjectItem);
			tvSmsParseItemNumber = (TextView) view.findViewById(R.id.tvSmsParseItemNumber);
			tvSmsParsingItemInfo = (TextView) view.findViewById(R.id.tvSmsParsingItemInfo);
			rootView = view;
		}
	}
}
