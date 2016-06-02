package com.jim.pocketaccounter;

import java.util.ArrayList;
import java.util.UUID;

import com.jim.pocketaccounter.finance.Category;
import com.jim.pocketaccounter.finance.IconAdapter;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.finance.SubCategory;
import com.jim.pocketaccounter.finance.SubCategoryAdapter;
import com.jim.pocketaccounter.helper.FABIcon;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class RootCategoryEditFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private EditText etCatEditName;
	private CheckBox chbCatEditExpanse, chbCatEditIncome;
	private FABIcon fabCatIcon;
	private ImageView ivSubCatAdd, ivSubCatDelete, ivDrawer, ivToolbar;
	private ListView lvSubCats;
	private RootCategory category;
	public static final int DELETE_MODE=0, NORMAL_MODE=1;
	private int mode, selectedIcon = 0, type;
	private boolean[] selected;
	private int[] icons;
	private ArrayList<SubCategory> subCategories;
	public RootCategoryEditFragment(RootCategory category) {
		this.category = category;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cat_edit_layout, container, false);
		ivDrawer = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivDrawer);
		ivDrawer.setImageDrawable(null);
		ivDrawer.setImageResource(R.drawable.ic_back_button);
		ivDrawer.setOnClickListener(this);
		ivToolbar = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbar);
		ivToolbar.setImageDrawable(null);
		ivToolbar.setImageResource(R.drawable.check_sign);
		ivToolbar.setOnClickListener(this);
		etCatEditName = (EditText) rootView.findViewById(R.id.etAccountEditName);
		chbCatEditExpanse = (CheckBox) rootView.findViewById(R.id.chbCatEditExpanse);
		chbCatEditIncome = (CheckBox) rootView.findViewById(R.id.chbCatEditIncome);
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
		type = Category.EXPANCE;
		selectedIcon = icons[0];
		subCategories = new ArrayList<SubCategory>();
		mode = NORMAL_MODE;
		setMode(mode);
		if (category != null) {
			etCatEditName.setText(category.getName());
			chbCatEditIncome.setChecked(false);
			chbCatEditExpanse.setChecked(false);
			switch(category.getType()) {
			case Category.INCOME:
				chbCatEditIncome.setChecked(true);
				break;
			case Category.EXPANCE:
				chbCatEditExpanse.setChecked(true);
				break;
			case Category.BOTH:
				chbCatEditIncome.setChecked(true);
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
		if (mode == NORMAL_MODE)
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
			if (mode == NORMAL_MODE) {
				mode = DELETE_MODE;
				setMode(mode);
			}
			else {
				deleteSubcats();
				mode = NORMAL_MODE;
				setMode(mode);
				refreshSubCatList(mode);
			}
			break;
		case R.id.ivDrawer:
			((PocketAccounter)getActivity()).openFragment(new CategoryFragment());
			break;
		case R.id.ivToolbar:
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
				type = Category.INCOME;
			if (chbCatEditExpanse.isChecked()) 
				type = Category.EXPANCE;
			if (chbCatEditIncome.isChecked() && chbCatEditExpanse.isChecked())
				type = Category.BOTH;
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
				PocketAccounter.financeManager.getCategories().add(newCategory);
			}
			((PocketAccounter)getActivity()).openFragment(new CategoryFragment());
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
		mode = NORMAL_MODE;
		setMode(mode);
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
				if (subCategory != null) 
					subCategory.setName(etSubCategoryName.getText().toString());
				else {
					SubCategory newSubCategory = new SubCategory();
					newSubCategory.setName(etSubCategoryName.getText().toString());
					newSubCategory.setId("subcat_"+UUID.randomUUID().toString());
					newSubCategory.setParent(category);
					subCategories.add(newSubCategory);
				}
				refreshSubCatList(NORMAL_MODE);
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
		if (mode == NORMAL_MODE) {
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