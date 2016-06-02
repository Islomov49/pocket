package com.jim.pocketaccounter;

import com.jim.pocketaccounter.finance.CurrencyAdapter;
import com.jim.pocketaccounter.helper.FloatingActionButton;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.helper.ScrollDirectionListener;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class CurrencyFragment extends Fragment implements OnClickListener, OnItemClickListener {
	private FloatingActionButton fabCurrencyAdd;
	private ListView lvCurrency;
	public static final int CURRENCY_LIST_MODE = 0, EDIT_MODE=1;
	private int mode = CURRENCY_LIST_MODE;
	private boolean[] selected;
	private ImageView ivToolbar;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.currency_fragment, container, false);
		fabCurrencyAdd = (FloatingActionButton) rootView.findViewById(R.id.fabCurrencyAdd);
		fabCurrencyAdd.setOnClickListener(this);
		lvCurrency = (ListView) rootView.findViewById(R.id.lvCurrency);
		lvCurrency.setOnItemClickListener(this);
		fabCurrencyAdd.attachToListView(lvCurrency, new ScrollDirectionListener() {
			@Override
			public void onScrollUp() {
				if (mode == EDIT_MODE) return;
				if (fabCurrencyAdd.getVisibility() == View.GONE) return;
            	Animation down = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_down);
            	synchronized (down) {
	                down.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
							fabCurrencyAdd.setVisibility(View.GONE);
						}
						@Override
						public void onAnimationEnd(Animation animation) {
						}
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
	                });
	                fabCurrencyAdd.startAnimation(down);
				}
			}
			@Override
			public void onScrollDown() {
				if (mode == EDIT_MODE) return;
				if (fabCurrencyAdd.getVisibility() == View.VISIBLE) return;
            	Animation up = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_up);
            	synchronized (up) {
	            	up.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
							fabCurrencyAdd.setVisibility(View.VISIBLE);
						}
						@Override
						public void onAnimationEnd(Animation animation) {
						}
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
	                });
	            	fabCurrencyAdd.startAnimation(up);
            	}
			}
		});
		((TextView)PocketAccounter.toolbar.findViewById(R.id.tvToolbarTitle)).setText(getResources().getString(R.string.currencies));
		((TextView)PocketAccounter.toolbar.findViewById(R.id.tvToolbarSubTitle)).setVisibility(View.VISIBLE);
		((TextView)PocketAccounter.toolbar.findViewById(R.id.tvToolbarSubTitle)).setText(getResources().getString(R.string.main_currency)+" "+PocketAccounter.financeManager.getMainCurrency().getAbbr());
		((ImageView)PocketAccounter.toolbar.findViewById(R.id.ivToolbar)).setImageDrawable(null);
		((ImageView)PocketAccounter.toolbar.findViewById(R.id.ivDrawer)).setImageResource(R.drawable.ic_drawer);
		((ImageView)PocketAccounter.toolbar.findViewById(R.id.ivDrawer)).setVisibility(View.VISIBLE);
		((ImageView)PocketAccounter.toolbar.findViewById(R.id.ivDrawer)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PocketAccounter.drawer.openLeftSide();
			}
		});
		ivToolbar = (ImageView)PocketAccounter.toolbar.findViewById(R.id.ivToolbar);
		ivToolbar.setImageResource(R.drawable.pencil);
		ivToolbar.setOnClickListener(this);
		refreshList();
		return rootView;
	}
	private void setEditMode() {
		mode = EDIT_MODE;
		selected = new boolean[PocketAccounter.financeManager.getCurrencies().size()];
		for (int i=0; i<selected.length; i++)
			selected[i] = false;
		ivToolbar.setImageDrawable(null);
		ivToolbar.setImageResource(R.drawable.ic_trash);
		Animation fabDown = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_down);
		fabDown.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				fabCurrencyAdd.setVisibility(View.GONE);
			}
		});
		fabCurrencyAdd.startAnimation(fabDown);
		refreshList();
	}
	private void setCurrencyListMode() {
		mode = CURRENCY_LIST_MODE;
		ivToolbar.setImageDrawable(null);
		ivToolbar.setImageResource(R.drawable.pencil);
		Animation fabUp = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_up);
		fabUp.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {	fabCurrencyAdd.setVisibility(View.VISIBLE);}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {}
		});
		fabCurrencyAdd.startAnimation(fabUp);
		refreshList();
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.fabCurrencyAdd:
			((PocketAccounter)getActivity()).openFragment(new CurrencyChooseFragment());
			break;
		case R.id.ivToolbar:
			PocketAccounterGeneral.buttonClick(v, getActivity());
			if (PocketAccounter.financeManager.getCurrencies().size() == 1) {
				Toast.makeText(getActivity(), getResources().getString(R.string.currency_empty_warning), Toast.LENGTH_SHORT).show();
				return;
			}
			if (mode == CURRENCY_LIST_MODE) {
				setEditMode();
			}
			else {
				boolean selection = false;
				for (int i=0; i<selected.length; i++) {
					if (selected[i]) {
						selection = true;
						break;
					}
				}
				if (!selection) {
					setCurrencyListMode();
					return;
				}
				final Dialog dialog=new Dialog(getActivity());
				View dialogView = getActivity().getLayoutInflater().inflate(R.layout.warning_dialog, null);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(dialogView);
				TextView tvWarningText = (TextView) dialogView.findViewById(R.id.tvWarningText);
				tvWarningText.setText(getResources().getString(R.string.currency_delete_warning));
				Button btnYes = (Button) dialogView.findViewById(R.id.btnWarningYes);
				Button btnNo = (Button) dialogView.findViewById(R.id.btnWarningNo);
				btnYes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						boolean foundNoneSelected = false;
						for (int i=0; i<selected.length; i++) {
							if (!selected[i]) {
								foundNoneSelected = true;
								break;
							}
						}
						if (!foundNoneSelected) {
							for (int i=0; i<PocketAccounter.financeManager.getCurrencies().size(); i++) {
								if (!PocketAccounter.financeManager.getCurrencies().get(i).getMain()) {
									PocketAccounter.financeManager.getCurrencies().remove(i);
									i--;
								}
							}
						} else {
							for (int i=0; i<selected.length; i++) {
								if (selected[i]) {
									if (PocketAccounter.financeManager.getCurrencies().get(i).getMain()) {
										if (i==selected.length-1) {
											for (int j=0; j<PocketAccounter.financeManager.getCurrencies().size(); j++) {
												if (PocketAccounter.financeManager.getCurrencies().get(j) != null) {
													PocketAccounter.financeManager.getCurrencies().get(j).setMain(true);
													break;
												}
											}
										} else {
											PocketAccounter.financeManager.getCurrencies().get(i+1).setMain(true);
										}
									}
									PocketAccounter.financeManager.getCurrencies().set(i, null);
								}
							}
							for (int i=0; i<PocketAccounter.financeManager.getCurrencies().size(); i++) {
								if (PocketAccounter.financeManager.getCurrencies().get(i) == null) {
									PocketAccounter.financeManager.getCurrencies().remove(i);
									i--;
								}
							}
						}
						setCurrencyListMode();
						dialog.dismiss();
					}
				});
				btnNo.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				dialog.show();
			}
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view != null) {
			if (mode == EDIT_MODE) {
				CheckBox chbCurrencyEdit = (CheckBox) view.findViewById(R.id.chbCurrencyEdit);
				chbCurrencyEdit.setChecked(!chbCurrencyEdit.isChecked());
				selected[position] = chbCurrencyEdit.isChecked();
			} else {
				if (PocketAccounter.financeManager.getCurrencies().get(position).getMain()) {
					Toast.makeText(getActivity(), getResources().getString(R.string.main_currency_edit), Toast.LENGTH_SHORT).show();
					return;
				}
				((PocketAccounter) getActivity()).openFragment(new CurrencyEditFragment(PocketAccounter.financeManager.getCurrencies().get(position)));
			}
		}
	};
	private void refreshList() {
		CurrencyAdapter adapter = new CurrencyAdapter(getActivity(), PocketAccounter.financeManager.getCurrencies(), selected, mode);
		lvCurrency.setAdapter(adapter);
	}
}