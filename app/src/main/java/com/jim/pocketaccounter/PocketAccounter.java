package com.jim.pocketaccounter;

import java.util.ArrayList;

import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.helper.LeftMenuAdapter;
import com.jim.pocketaccounter.helper.LeftMenuItem;
import com.jim.pocketaccounter.helper.LeftSideDrawer;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PocketAccounter extends Activity implements OnClickListener{
	public static LeftSideDrawer drawer;
	private ImageView ivDrawer;
	private TextView tvToolbarTitle;
	public static LinearLayout toolbar;
	public static FinanceManager financeManager;
	private ListView lvLeftMenu;
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pocket_accounter);
		financeManager = new FinanceManager(this);
		drawer = new LeftSideDrawer(this);
		drawer.setLeftBehindContentView(R.layout.activity_behind_left_simple);
		ivDrawer = (ImageView) findViewById(R.id.ivDrawer);
		ivDrawer.setOnClickListener(this);
		toolbar = (LinearLayout) findViewById(R.id.toolbar);
		tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
		tvToolbarTitle.setText(getResources().getString(R.string.app_name));
		lvLeftMenu = (ListView) findViewById(R.id.lvLeftMenu);
		fillLeftMenu();
		if (financeManager.getCurrencies().isEmpty())
			openFragment(new RecordFragment());
		else if (financeManager.getAccounts().isEmpty())
			openFragment(new AccountFragment());
		else
			openFragment(new RecordFragment());
		lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Fragment tempFrag=null;
				switch (position){
					case 8:
						tempFrag=new CreditFragment();
						break;
					default:

				}
				if(!drawer.isClosed()){
					drawer.closeLeftSide();
				}
				openFragment(tempFrag);
			}
		});
	}
	private void fillLeftMenu() {
		String[] cats = getResources().getStringArray(R.array.drawer_cats);
		String[] financeSubItemTitles = getResources().getStringArray(R.array.finance_subitems);
		String[] financeSubItemIcons = getResources().getStringArray(R.array.finance_subitem_icons);
		String[] debtSubItemTitles = getResources().getStringArray(R.array.debts_subitems);
		String[] debtSubItemIcons = getResources().getStringArray(R.array.debts_subitem_icons);
		ArrayList<LeftMenuItem> items = new ArrayList<LeftMenuItem>();
		LeftMenuItem main = new LeftMenuItem(cats[0], R.drawable.drawer_home, 100);
		main.setGroup(true);
		items.add(main);
		LeftMenuItem finance = new LeftMenuItem(cats[1], R.drawable.drawer_finance, 100);
		finance.setGroup(true);
		items.add(finance);
		for (int i=0; i<financeSubItemTitles.length; i++) {
			int resId = getResources().getIdentifier(financeSubItemIcons[i], "drawable", getPackageName());
			LeftMenuItem subItem = new LeftMenuItem(financeSubItemTitles[i], resId, 100);
			subItem.setGroup(false);
			items.add(subItem);
		}
		LeftMenuItem planning = new LeftMenuItem(cats[2], R.drawable.drawer_planning, 100);
		planning.setGroup(true);
		items.add(planning);
		LeftMenuItem debts = new LeftMenuItem(cats[3], R.drawable.drawer_debts, 100);
		debts.setGroup(true);
		items.add(debts);
		for (int i=0; i<debtSubItemTitles.length; i++) {
			int resId = getResources().getIdentifier(debtSubItemIcons[i], "drawable", getPackageName());
			LeftMenuItem subItem = new LeftMenuItem(debtSubItemTitles[i], resId, 100);
			subItem.setGroup(false);
			items.add(subItem);
		}

		LeftMenuItem settings = new LeftMenuItem(cats[4], R.drawable.drawer_settings, 100);
		settings.setGroup(true);
		items.add(settings);
		LeftMenuItem rateApp = new LeftMenuItem(cats[5], R.drawable.drawer_rate, 100);
		rateApp.setGroup(true);
		items.add(rateApp);
		LeftMenuItem share = new LeftMenuItem(cats[6], R.drawable.drawer_share, 100);
		share.setGroup(true);
		items.add(share);
		LeftMenuItem writeToUs = new LeftMenuItem(cats[7], R.drawable.drawer_letter_us, 100);
		writeToUs.setGroup(true);
		items.add(writeToUs);
		LeftMenuAdapter adapter = new LeftMenuAdapter(this, items);
		lvLeftMenu.setAdapter(adapter);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.ivDrawer:
			PocketAccounterGeneral.buttonClick(v, this);
			drawer.openLeftSide();
			break;
		}
	}
	
	public void openFragment(Fragment fragment) {
		if (fragment != null) {
			final FragmentTransaction ft = getFragmentManager().beginTransaction(); 
			ft.replace(R.id.flMain, fragment, null); 
			ft.commit(); 
		}
	}
}
