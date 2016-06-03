package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.AccountAdapter;
import com.jim.pocketaccounter.finance.IconAdapter;
import com.jim.pocketaccounter.helper.FABIcon;
import com.jim.pocketaccounter.helper.FloatingActionButton;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.UUID;

@SuppressLint("InflateParams")
public class AccountFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private FloatingActionButton fabAccountAdd;
	private boolean[] selected;
	private int mode = PocketAccounterGeneral.NORMAL_MODE, selectedIcon;
	private ListView lvAccounts;
	private ImageView ivToolbarMostRight;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.account_layout, container, false);
		fabAccountAdd = (FloatingActionButton) rootView.findViewById(R.id.fabAccountAdd);
		fabAccountAdd.setOnClickListener(this);
		lvAccounts = (ListView) rootView.findViewById(R.id.lvAccounts);
		lvAccounts.setOnItemClickListener(this);
		((PocketAccounter)getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
		ivToolbarMostRight.setImageResource(R.drawable.pencil);
		ivToolbarMostRight.setOnClickListener(this);
		refreshList(mode);
		return rootView;
	}
	private void refreshList(int mode) {
		AccountAdapter adapter = new AccountAdapter(getActivity(), PocketAccounter.financeManager.getAccounts(), selected, mode);
		lvAccounts.setAdapter(adapter);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mode == PocketAccounterGeneral.NORMAL_MODE)
			openAccountsAddDialog(PocketAccounter.financeManager.getAccounts().get(position));
		else {
			CheckBox chbAccountListItem = (CheckBox) view.findViewById(R.id.chbAccountListItem);
			chbAccountListItem.setChecked(!chbAccountListItem.isChecked());
			selected[position] = chbAccountListItem.isChecked();
		}
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivToolbarMostRight:
			if (mode == PocketAccounterGeneral.NORMAL_MODE) {
				mode = PocketAccounterGeneral.EDIT_MODE;
				ivToolbarMostRight.setImageDrawable(null);
				ivToolbarMostRight.setImageResource(R.drawable.ic_trash);
				selected = new boolean[PocketAccounter.financeManager.getAccounts().size()];
				refreshList(mode);
			}
			else {
				mode = PocketAccounterGeneral.NORMAL_MODE;
				ivToolbarMostRight.setImageDrawable(null);
				ivToolbarMostRight.setImageResource(R.drawable.pencil);
				deleteAccounts();
				refreshList(mode);
				selected = null;
			}
			break;
		case R.id.fabAccountAdd:
			mode = PocketAccounterGeneral.NORMAL_MODE;
			ivToolbarMostRight.setImageDrawable(null);
			ivToolbarMostRight.setImageResource(R.drawable.pencil);
			refreshList(mode);
			openAccountsAddDialog(null);
			break;
		}
	}
	private void openAccountsAddDialog(final Account account) {
		final Dialog dialog=new Dialog(getActivity());
		View dialogView = getActivity().getLayoutInflater().inflate(R.layout.account_edit_layout, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		final EditText etAccountEditName = (EditText) dialogView.findViewById(R.id.etAccountEditName);
		final FABIcon fabAccountIcon = (FABIcon) dialogView.findViewById(R.id.fabAccountIcon);
		String[] tempIcons = getResources().getStringArray(R.array.icons);
		final int[] icons = new int[tempIcons.length];
		for (int i=0; i<tempIcons.length; i++)
			icons[i] = getResources().getIdentifier(tempIcons[i], "drawable", getActivity().getPackageName());
		selectedIcon = icons[0];
		if (account != null) {
			etAccountEditName.setText(account.getName());
			selectedIcon = account.getIcon();
		}
		Bitmap temp = BitmapFactory.decodeResource(getResources(), selectedIcon);
		Bitmap icon = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
		fabAccountIcon.setImageBitmap(icon);
		fabAccountIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog=new Dialog(getActivity());
				View dialogView = getActivity().getLayoutInflater().inflate(R.layout.cat_icon_select, null);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				GridView gvIcon = (GridView) dialogView.findViewById(R.id.gvCategoryIcons);
				IconAdapter adapter = new IconAdapter(getActivity(), icons, selectedIcon);
				gvIcon.setAdapter(adapter);
				gvIcon.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Bitmap temp = BitmapFactory.decodeResource(getResources(), icons[position]);
						Bitmap icon = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfive_dp), (int)getResources().getDimension(R.dimen.twentyfive_dp), false);
						fabAccountIcon.setImageBitmap(icon);
						selectedIcon = icons[position];
						dialog.dismiss();
					}
				});
				DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
				int width = displayMetrics.widthPixels;
				dialog.getWindow().setLayout(7*width/8, LayoutParams.WRAP_CONTENT);
				dialog.show();
			}
		});
		ImageView ivAccountSave = (ImageView) dialogView.findViewById(R.id.ivAccountSave);
		ivAccountSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (etAccountEditName.getText().toString().matches("")) {
					Animation wooble = AnimationUtils.loadAnimation(getActivity(), R.anim.wobble);
					etAccountEditName.startAnimation(wooble);
					return;
				}
				if (account != null) {
					account.setName(etAccountEditName.getText().toString());
					account.setIcon(selectedIcon);
				}
				else {
					Account newAccount = new Account();
					newAccount.setName(etAccountEditName.getText().toString());
					newAccount.setIcon(selectedIcon);
					newAccount.setId("account_"+UUID.randomUUID().toString());
					PocketAccounter.financeManager.getAccounts().add(newAccount);
				}
				dialog.dismiss();
				refreshList(mode);
			}
		});
		ImageView ivAccountClose = (ImageView) dialogView.findViewById(R.id.ivAccountClose);
		ivAccountClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		dialog.getWindow().setLayout(7*width/8, LayoutParams.WRAP_CONTENT);
		dialog.show();
	}
	private void deleteAccounts() {
		for (int i=0; i<selected.length; i++) {
			if (selected[i])
				PocketAccounter.financeManager.getAccounts().set(i, null);
		}
		for (int i = 0; i< PocketAccounter.financeManager.getAccounts().size(); i++) {
			if (PocketAccounter.financeManager.getAccounts().get(i) == null) {
				PocketAccounter.financeManager.getAccounts().remove(i);
				i--;
			}
		}
	}
}
