package com.jim.pocketaccounter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.CategoryAdapter;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.helper.FloatingActionButton;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;

public class CategoryFragment extends Fragment implements OnClickListener, OnItemClickListener, OnCheckedChangeListener {
	private FloatingActionButton fabCategoryAdd;
	private ListView lvCategories;
	private CheckBox chbCatIncomes, chbCatExpanses, chbCatBoth;
	private ImageView ivToolbarMostRight;
	public static final int NORMAL_MODE=0, DELETE_MODE=1;
	private int mode;
	private boolean[] selected;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.category_layout, container, false);
		PocketAccounter.toolbar.setTitle(getResources().getString(R.string.category));
		PocketAccounter.toolbar.setSubtitle("");
		((PocketAccounter)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((PocketAccounter)getContext()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
		ivToolbarMostRight.setImageResource(R.drawable.pencil);
		ivToolbarMostRight.setOnClickListener(this);
		fabCategoryAdd = (FloatingActionButton) rootView.findViewById(R.id.fabAccountAdd);
		fabCategoryAdd.setOnClickListener(this);
		lvCategories = (ListView) rootView.findViewById(R.id.lvAccounts);
		lvCategories.setOnItemClickListener(this);
		chbCatIncomes = (CheckBox) rootView.findViewById(R.id.chbCatIncomes);
		chbCatIncomes.setOnCheckedChangeListener(this);
		chbCatExpanses = (CheckBox) rootView.findViewById(R.id.chbCatExpanses);
		chbCatExpanses.setOnCheckedChangeListener(this);
		chbCatBoth = (CheckBox) rootView.findViewById(R.id.chbCatBoth);
		chbCatBoth.setOnCheckedChangeListener(this);
		mode = NORMAL_MODE;
		setMode(mode);
		refreshList(mode);
		return rootView;
	}
	private void refreshList(int mode) {
		ArrayList<RootCategory> categories = new ArrayList<RootCategory>();
		for (int i = 0; i< PocketAccounter.financeManager.getCategories().size(); i++) {
			if (chbCatIncomes.isChecked()) {
				if (PocketAccounter.financeManager.getCategories().get(i).getType() == Category.INCOME)
					categories.add(PocketAccounter.financeManager.getCategories().get(i));
			}
			if(chbCatExpanses.isChecked()) {
				if (PocketAccounter.financeManager.getCategories().get(i).getType() == Category.EXPANCE)
					categories.add(PocketAccounter.financeManager.getCategories().get(i));
			}
			if(chbCatBoth.isChecked()) {
				if (PocketAccounter.financeManager.getCategories().get(i).getType() == Category.BOTH)
					categories.add(PocketAccounter.financeManager.getCategories().get(i));
			}
		}
		CategoryAdapter adapter = new CategoryAdapter(getActivity(), categories, selected, mode);
		lvCategories.setAdapter(adapter);
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.fabAccountAdd:
			((PocketAccounter)getActivity()).replaceFragment(new RootCategoryEditFragment(null));
			break;
		case R.id.ivToolbarMostRight:
			PocketAccounterGeneral.buttonClick(ivToolbarMostRight, getActivity());
			if (mode == NORMAL_MODE)
				mode = DELETE_MODE;
			else {
				mode = NORMAL_MODE;
				deleteCategories();
			}
			setMode(mode);
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mode == NORMAL_MODE)
			((PocketAccounter)getActivity()).replaceFragment(new RootCategoryEditFragment(PocketAccounter.financeManager.getCategories().get(position)));
		else {
			CheckBox chbCatListItem = (CheckBox) view.findViewById(R.id.chbAccountListItem);
			chbCatListItem.setChecked(!chbCatListItem.isChecked());
			selected[position] = chbCatListItem.isChecked();
		}
	}
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		refreshList(mode);
	}
	private void setMode(int mode) {
		if (mode == NORMAL_MODE) {
			selected = null;
			ivToolbarMostRight.setImageResource(R.drawable.pencil);
		}
		else {
			selected = new boolean[PocketAccounter.financeManager.getCategories().size()];
			ivToolbarMostRight.setImageResource(R.drawable.ic_trash);
		}
		refreshList(mode);
	}
	private void deleteCategories() {
		for (int i=0; i<selected.length; i++) {
			if (selected[i])
				PocketAccounter.financeManager.getCategories().set(i, null);
		}
		for (int i = 0; i< PocketAccounter.financeManager.getCategories().size(); i++) {
			if (PocketAccounter.financeManager.getCategories().get(i) == null) {
				PocketAccounter.financeManager.getCategories().remove(i);
				i--;
			}
		}
	}
}