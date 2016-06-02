package com.jim.pocketaccounter.finance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.RootCategoryEditFragment;

import java.util.ArrayList;

public class SubCategoryAdapter extends BaseAdapter {
	private ArrayList<SubCategory> result;
	private LayoutInflater inflater;
	private int mode=10;
	private boolean[] selected;
	public SubCategoryAdapter(Context context, ArrayList<SubCategory> result, boolean[] selected, int mode) {
	    this.result = result;
	    this.mode = mode;
	    this.selected = selected;
	    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return result.size();
	}
	@Override
	public Object getItem(int position) {
		return result.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.subcat_item, parent, false);
		TextView tvSubCatName = (TextView) view.findViewById(R.id.tvSubCatName);
		tvSubCatName.setText(result.get(position).getName());
		CheckBox chbSubCat = (CheckBox)view.findViewById(R.id.chbSubCat);
		chbSubCat.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selected[position] = isChecked;
			}
		});
		if (mode == RootCategoryEditFragment.DELETE_MODE) {
			chbSubCat.setVisibility(View.VISIBLE);
			chbSubCat.setChecked(selected[position]);
		} else 
			chbSubCat.setVisibility(View.GONE);
		return view;
	}
}