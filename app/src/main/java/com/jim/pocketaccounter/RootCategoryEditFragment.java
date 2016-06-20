package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout.LayoutParams;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.IconAdapter;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.finance.SubCategory;
import com.jim.pocketaccounter.finance.SubCategoryAdapter;
import com.jim.pocketaccounter.helper.FABIcon;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

@SuppressLint({"InflateParams", "ValidFragment"})
public class RootCategoryEditFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private EditText etCatEditName;
	private CheckBox chbCatEditExpanse, chbCatEditIncome;
	private FABIcon fabCatIcon;
	private ImageView ivSubCatAdd, ivSubCatDelete, ivToolbarMostRight;
	private ListView lvSubCats;
	private RootCategory category;
	private int mode = PocketAccounterGeneral.NORMAL_MODE, selectedIcon = 0, type;
	private boolean[] selected;
	private int[] icons;
	private ArrayList<SubCategory> subCategories;
	private int edit_mode, pos, subcatIcon;
	private Calendar calendar;
	private String categoryId;
	public RootCategoryEditFragment(RootCategory category, int mode, int pos, Calendar date) {
		this.category = category;
		if (category == null)
			categoryId = "category_"+UUID.randomUUID().toString();
		else
			categoryId = category.getId();
		this.edit_mode = mode;
		this.pos = pos;
		if (date != null)
			calendar = (Calendar) date.clone();
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cat_edit_layout, container, false);
		((PocketAccounter)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		((PocketAccounter)getContext()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button);
		PocketAccounter.toolbar.setNavigationOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (edit_mode == PocketAccounterGeneral.NO_MODE)
					((PocketAccounter)getContext()).replaceFragment(new CategoryFragment());
				else
					((PocketAccounter)getContext()).replaceFragment(new RecordFragment(calendar));
			}
		});
		ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
		ivToolbarMostRight.setImageDrawable(null);
		ivToolbarMostRight.setImageResource(R.drawable.check_sign);
		ivToolbarMostRight.setOnClickListener(this);
		etCatEditName = (EditText) rootView.findViewById(R.id.etAccountEditName);
		chbCatEditExpanse = (CheckBox) rootView.findViewById(R.id.chbCatEditExpanse);
		chbCatEditIncome = (CheckBox) rootView.findViewById(R.id.chbCatEditIncome);
		if (edit_mode == PocketAccounterGeneral.EXPANSE_MODE) {
			chbCatEditExpanse.setChecked(true);
			chbCatEditIncome.setChecked(false);
		}
		if (edit_mode == PocketAccounterGeneral.INCOME_MODE) {
			chbCatEditExpanse.setChecked(false);
			chbCatEditIncome.setChecked(true);
		}
		chbCatEditExpanse.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (edit_mode == PocketAccounterGeneral.NO_MODE) {
					chbCatEditExpanse.setChecked(isChecked);
					chbCatEditIncome.setChecked(!isChecked);
				}
				if (edit_mode == PocketAccounterGeneral.EXPANSE_MODE) {
					chbCatEditExpanse.setChecked(true);
					chbCatEditIncome.setChecked(false);
				}
				if (edit_mode == PocketAccounterGeneral.INCOME_MODE) {
					chbCatEditExpanse.setChecked(false);
					chbCatEditIncome.setChecked(true);
				}
			}
		});
		chbCatEditIncome.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (edit_mode == PocketAccounterGeneral.NO_MODE) {
					chbCatEditExpanse.setChecked(!isChecked);
					chbCatEditIncome.setChecked(isChecked);
				}
				if (edit_mode == PocketAccounterGeneral.EXPANSE_MODE) {
					chbCatEditExpanse.setChecked(true);
					chbCatEditIncome.setChecked(false);
				}
				if (edit_mode == PocketAccounterGeneral.INCOME_MODE) {
					chbCatEditExpanse.setChecked(false);
					chbCatEditIncome.setChecked(true);
				}
			}
		});
		fabCatIcon = (FABIcon) rootView.findViewById(R.id.fabAccountIcon);
		fabCatIcon.setOnClickListener(this);
		ivSubCatAdd = (ImageView) rootView.findViewById(R.id.ivSubCatAdd);
		ivSubCatAdd.setOnClickListener(this);
		ivSubCatDelete = (ImageView) rootView.findViewById(R.id.ivSubCatDelete);
		ivSubCatDelete.setOnClickListener(this);
		lvSubCats = (ListView) rootView.findViewById(R.id.lvAccountHistory);
		lvSubCats.setOnItemClickListener(this);
		String[] temps = getResources().getStringArray(R.array.icons);
		icons = new int[temps.length];
		for (int i=0; i<temps.length; i++) {
			int resId = getResources().getIdentifier(temps[i], "drawable", getActivity().getPackageName());
			icons[i] = resId;
		}
		type = PocketAccounterGeneral.EXPANCE;
		selectedIcon = icons[0];
		subCategories = new ArrayList<SubCategory>();
		mode = PocketAccounterGeneral.NORMAL_MODE;
		setMode(mode);
		if (category != null) {
			etCatEditName.setText(category.getName());
			chbCatEditIncome.setChecked(false);
			chbCatEditExpanse.setChecked(false);
			switch(category.getType()) {
			case PocketAccounterGeneral.INCOME:
				chbCatEditIncome.setChecked(true);
				break;
			case PocketAccounterGeneral.EXPANCE:
				chbCatEditExpanse.setChecked(true);
				break;
			}
			type = category.getType();
			selectedIcon = category.getIcon();
			subCategories = category.getSubCategories();
			refreshSubCatList(mode);
		}
		Bitmap temp = BitmapFactory.decodeResource(getResources(), selectedIcon);
		Bitmap icon = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
		fabCatIcon.setImageBitmap(icon);
		return rootView;
	}
	private void refreshSubCatList(int mode) {
		SubCategoryAdapter adapter = new SubCategoryAdapter(getActivity(), subCategories, selected, mode);
		lvSubCats.setAdapter(adapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		if (mode == PocketAccounterGeneral.NORMAL_MODE)
			openSubCatEditDialog(subCategories.get(position));
		else {
			CheckBox chbSubCat = (CheckBox)view.findViewById(R.id.chbSubCat);
			chbSubCat.setChecked(!chbSubCat.isChecked());
			selected[position] = chbSubCat.isChecked();
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.fabAccountIcon:
			openIconsDialog();
			break;
		case R.id.ivSubCatAdd:
			openSubCatEditDialog(null);
			break;
		case R.id.ivSubCatDelete:
			if (mode == PocketAccounterGeneral.NORMAL_MODE) {
				mode = PocketAccounterGeneral.EDIT_MODE;
				setMode(mode);
			}
			else {
				deleteSubcats();
				mode = PocketAccounterGeneral.NORMAL_MODE;
				setMode(mode);
				refreshSubCatList(mode);
			}
			break;
		case R.id.ivToolbarMostRight:
			if (etCatEditName.getText().toString().matches("")) {
				Toast.makeText(getActivity(), getResources().getString(R.string.enter_cat_name), Toast.LENGTH_SHORT).show();
				Animation wobble = AnimationUtils.loadAnimation(getActivity(), R.anim.wobble);
				etCatEditName.startAnimation(wobble);
				return;
			}
			if (!chbCatEditIncome.isChecked() && !chbCatEditExpanse.isChecked()) {
				Toast.makeText(getActivity(), getResources().getString(R.string.cat_type_not_choosen), Toast.LENGTH_SHORT).show();
				Animation wobble = AnimationUtils.loadAnimation(getActivity(), R.anim.wobble);
				((LinearLayout)chbCatEditIncome.getParent()).startAnimation(wobble);
				return;
			}
			if (chbCatEditIncome.isChecked())
				type = PocketAccounterGeneral.INCOME;
			if (chbCatEditExpanse.isChecked())
				type = PocketAccounterGeneral.EXPANCE;
			if (edit_mode == PocketAccounterGeneral.NO_MODE) {
				if (category != null) {
					category.setName(etCatEditName.getText().toString());
					category.setType(type);
					category.setIcon(selectedIcon);
					category.setSubCategories(subCategories);
				}
				else {
					RootCategory newCategory = new RootCategory();
					newCategory.setName(etCatEditName.getText().toString());
					newCategory.setType(type);
					newCategory.setIcon(selectedIcon);
					newCategory.setSubCategories(subCategories);
					newCategory.setId("rootcategory_"+UUID.randomUUID().toString());
					PocketAccounter.financeManager.getCategories().add(newCategory);
				}
				((PocketAccounter)getActivity()).replaceFragment(new CategoryFragment());
			}
			else if (type==PocketAccounterGeneral.INCOME) {
				RootCategory newCategory = new RootCategory();
				newCategory.setName(etCatEditName.getText().toString());
				newCategory.setType(type);
				newCategory.setIcon(selectedIcon);
				newCategory.setSubCategories(subCategories);
				newCategory.setId(categoryId);
				PocketAccounter.financeManager.getIncomes().set(pos, newCategory);
				PocketAccounter.financeManager.getCategories().add(newCategory);
				((PocketAccounter)getActivity()).replaceFragment(new RecordFragment(calendar));
			} else if (type==PocketAccounterGeneral.EXPANCE) {
				RootCategory newCategory = new RootCategory();
				newCategory.setName(etCatEditName.getText().toString());
				newCategory.setType(type);
				newCategory.setIcon(selectedIcon);
				newCategory.setSubCategories(subCategories);
				newCategory.setId(categoryId);
				PocketAccounter.financeManager.getExpanses().set(pos, newCategory);
				PocketAccounter.financeManager.getCategories().add(newCategory);
				((PocketAccounter)getActivity()).replaceFragment(new RecordFragment(calendar));
			}
			break;
		}
	}
	@SuppressLint({ "InfateParams", "NewApi" })
	public void openIconsDialog() {
		final Dialog dialog=new Dialog(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.cat_icon_select, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		final GridView gvCategoryIcons = (GridView) dialogView.findViewById(R.id.gvCategoryIcons);
		IconAdapter adapter = new IconAdapter(getActivity(), icons, selectedIcon);
		gvCategoryIcons.setAdapter(adapter);
		gvCategoryIcons.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				selectedIcon = icons[position];
				Bitmap temp = BitmapFactory.decodeResource(getResources(), selectedIcon);
				Bitmap icon = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
				fabCatIcon.setImageBitmap(icon);
				dialog.dismiss();
			}
		});
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		dialog.getWindow().setLayout(8*width/9, LayoutParams.MATCH_PARENT);
		dialog.show();
	}
	@SuppressLint("InflateParams")
	private void openSubCatEditDialog(final SubCategory subCategory) {
		final Dialog dialog=new Dialog(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.sub_category_edit_layout, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		mode = PocketAccounterGeneral.NORMAL_MODE;
		setMode(mode);

		final FABIcon fabChooseIcon = (FABIcon) dialogView.findViewById(R.id.fabChooseIcon);
		Bitmap temp, scaled;
		if (subCategory != null)
			subcatIcon = subCategory.getIcon();
		else
			subcatIcon = R.drawable.ball;
		temp = BitmapFactory.decodeResource(getResources(), subcatIcon);
		scaled = Bitmap.createScaledBitmap(temp, (int) getResources().getDimension(R.dimen.twentyfive_dp), (int) getResources().getDimension(R.dimen.twentyfive_dp), false);
		fabChooseIcon.setImageBitmap(scaled);
		fabChooseIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog=new Dialog(getActivity());
				View dialogView = getActivity().getLayoutInflater().inflate(R.layout.cat_icon_select, null);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				final GridView gvCategoryIcons = (GridView) dialogView.findViewById(R.id.gvCategoryIcons);
				IconAdapter adapter = new IconAdapter(getActivity(), icons, subcatIcon);
				gvCategoryIcons.setAdapter(adapter);
				gvCategoryIcons.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						subCategory.setIcon(icons[position]);
						Bitmap temp  = BitmapFactory.decodeResource(getResources(), icons[position]);
						Bitmap scaled = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
						fabChooseIcon.setImageBitmap(scaled);
						subcatIcon = icons[position];
						dialog.dismiss();
					}
				});
				DisplayMetrics dm = getResources().getDisplayMetrics();
				int width = dm.widthPixels;
				dialog.getWindow().setLayout(8*width/9, LayoutParams.MATCH_PARENT);
				dialog.show();
			}
		});
		ImageView ivSubCatClose = (ImageView) dialogView.findViewById(R.id.ivSubCatClose);
		ImageView ivSubCatSave = (ImageView) dialogView.findViewById(R.id.ivSubCatSave);
		final EditText etSubCategoryName = (EditText) dialogView.findViewById(R.id.etSubCategoryName);
		if (subCategory != null)
			etSubCategoryName.setText(subCategory.getName());
		ivSubCatSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etSubCategoryName.getText().toString().matches("")) {
					Animation wobble = AnimationUtils.loadAnimation(getActivity(), R.anim.wobble);
					etSubCategoryName.startAnimation(wobble);
					return;
				}
				if (subCategory != null) {
					subCategory.setName(etSubCategoryName.getText().toString());
					subCategory.setIcon(subcatIcon);
				}
				else {
					SubCategory newSubCategory = new SubCategory();
					newSubCategory.setName(etSubCategoryName.getText().toString());
					newSubCategory.setId("subcat_"+UUID.randomUUID().toString());
					newSubCategory.setParent(categoryId);
					newSubCategory.setIcon(subcatIcon);
					subCategories.add(newSubCategory);
				}
				refreshSubCatList(PocketAccounterGeneral.NORMAL_MODE);
				dialog.dismiss();
			}
		});
		ivSubCatClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	private void setMode(int mode) {
		if (mode == PocketAccounterGeneral.NORMAL_MODE) {
			ivSubCatDelete.setImageResource(R.drawable.subcat_delete);
			selected = null;
		}
		else {
			ivSubCatDelete.setImageResource(R.drawable.ic_cat_trash);
			selected = new boolean[subCategories.size()];
		}
		refreshSubCatList(mode);
	}
	private void deleteSubcats() {
		for (int i=0; i<selected.length; i++) {
			if (selected[i])
				subCategories.set(i, null);
		}
		for (int i=0; i<subCategories.size(); i++) {
			if (subCategories.get(i) == null) {
				subCategories.remove(i);
				i--;
			}
		}
	}
}