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
	private CheckBox chbCatIncomes, chbCatExpanses;
	private ImageView ivToolbarMostRight;
	private int mode = PocketAccounterGeneral.NORMAL_MODE;
	private boolean[] selected;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.category_layout, container, false);
		PocketAccounter.toolbar.setTitle(getResources().getString(R.string.category));
		PocketAccounter.toolbar.setSubtitle("");
		((PocketAccounter)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((PocketAccounter)getContext()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
		PocketAccounter.toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PocketAccounter.drawer.openLeftSide();
			}
		});
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
		setMode(mode);
		refreshList(mode);
		return rootView;
	}
	private void refreshList(int mode) {
		ArrayList<RootCategory> categories = new ArrayList<RootCategory>();
		for (int i = 0; i< PocketAccounter.financeManager.getCategories().size(); i++) {
			if (chbCatIncomes.isChecked()) {
				if (PocketAccounter.financeManager.getCategories().get(i).getType() == PocketAccounterGeneral.INCOME)
					categories.add(PocketAccounter.financeManager.getCategories().get(i));
			}
			if(chbCatExpanses.isChecked()) {
				if (PocketAccounter.financeManager.getCategories().get(i).getType() == PocketAccounterGeneral.EXPANCE)
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
			((PocketAccounter)getActivity()).replaceFragment(new RootCategoryEditFragment(null, PocketAccounterGeneral.NO_MODE, 0, null));
			break;
		case R.id.ivToolbarMostRight:
			if (mode == PocketAccounterGeneral.NORMAL_MODE)
				mode = PocketAccounterGeneral.EDIT_MODE;
			else {
				mode = PocketAccounterGeneral.NORMAL_MODE;
				deleteCategories();
			}
			setMode(mode);
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mode == PocketAccounterGeneral.NORMAL_MODE)
			((PocketAccounter)getActivity()).replaceFragment(new RootCategoryEditFragment(PocketAccounter.financeManager.getCategories().get(position), PocketAccounterGeneral.NO_MODE, 0, null));
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
		if (mode == PocketAccounterGeneral.NORMAL_MODE) {
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
		//delete from all categories
		for (int i=0; i<selected.length; i++) {
			if (selected[i]) {
				String id = PocketAccounter.financeManager.getCategories().get(i).getId();
				for (int j=0; j<PocketAccounter.financeManager.getExpanses().size(); j++) {
					if (PocketAccounter.financeManager.getExpanses().get(j) == null)	continue;
					if (PocketAccounter.financeManager.getExpanses().get(j).getId().matches(id))
						PocketAccounter.financeManager.getExpanses().set(j, null);
				}
				for (int j=0; j<PocketAccounter.financeManager.getIncomes().size(); j++) {
					if (PocketAccounter.financeManager.getIncomes().get(j) == null)	continue;
					if (PocketAccounter.financeManager.getIncomes().get(j).getId().matches(id))
						PocketAccounter.financeManager.getIncomes().set(j, null);
				}
				PocketAccounter.financeManager.getCategories().set(i, null);
			}
		}
		for (int i = 0; i< PocketAccounter.financeManager.getCategories().size(); i++) {
			if (PocketAccounter.financeManager.getCategories().get(i) == null) {
				PocketAccounter.financeManager.getCategories().remove(i);
				i--;
			}
		}
	}
	@Override
	public void onStop() {
		super.onStop();
		PocketAccounter.financeManager.saveCategories();
	}
}