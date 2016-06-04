package com.jim.pocketaccounter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.helper.LeftMenuAdapter;
import com.jim.pocketaccounter.helper.LeftMenuItem;
import com.jim.pocketaccounter.helper.LeftSideDrawer;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.util.ArrayList;

import static com.jim.pocketaccounter.R.color.toolbar_text_color;

public class PocketAccounter extends AppCompatActivity {
    public static Toolbar toolbar;
    public static LeftSideDrawer drawer;
    private ListView lvLeftMenu;
    public static FinanceManager financeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_accounter);
        financeManager = new FinanceManager(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, toolbar_text_color));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        drawer = new LeftSideDrawer(this);
        drawer.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        lvLeftMenu = (ListView) findViewById(R.id.lvLeftMenu);
        fillLeftMenu();
        if (financeManager.getCurrencies().isEmpty())
            replaceFragment(new CurrencyChooseFragment());
        else
            replaceFragment(new RecordFragment());
    }
    private void fillLeftMenu() {
        String[] cats = getResources().getStringArray(R.array.drawer_cats);
        String[] financeSubItemTitles = getResources().getStringArray(R.array.finance_subitems);
        String[] financeSubItemIcons = getResources().getStringArray(R.array.finance_subitem_icons);
        String[] debtSubItemTitles = getResources().getStringArray(R.array.debts_subitems);
        String[] debtSubItemIcons = getResources().getStringArray(R.array.debts_subitem_icons);
        ArrayList<LeftMenuItem> items = new ArrayList<LeftMenuItem>();
        LeftMenuItem main = new LeftMenuItem(cats[0], R.drawable.drawer_home);
        main.setGroup(true);
        items.add(main);
        LeftMenuItem finance = new LeftMenuItem(cats[1], R.drawable.drawer_finance);
        finance.setGroup(true);
        items.add(finance);
        for (int i=0; i<financeSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(financeSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(financeSubItemTitles[i], resId);
            subItem.setGroup(false);
            items.add(subItem);
        }
        LeftMenuItem planning = new LeftMenuItem(cats[2], R.drawable.drawer_planning);
        planning.setGroup(true);
        items.add(planning);
        LeftMenuItem debts = new LeftMenuItem(cats[3], R.drawable.drawer_debts);
        debts.setGroup(true);
        items.add(debts);
        for (int i=0; i<debtSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(debtSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(debtSubItemTitles[i], resId);
            subItem.setGroup(false);
            items.add(subItem);
        }
        LeftMenuItem settings = new LeftMenuItem(cats[4], R.drawable.drawer_settings);
        settings.setGroup(true);
        items.add(settings);
        LeftMenuItem rateApp = new LeftMenuItem(cats[5], R.drawable.drawer_rate);
        rateApp.setGroup(true);
        items.add(rateApp);
        LeftMenuItem share = new LeftMenuItem(cats[6], R.drawable.drawer_share);
        share.setGroup(true);
        items.add(share);
        LeftMenuItem writeToUs = new LeftMenuItem(cats[7], R.drawable.drawer_letter_us);
        writeToUs.setGroup(true);
        items.add(writeToUs);
        LeftMenuAdapter adapter = new LeftMenuAdapter(this, items);
        lvLeftMenu.setAdapter(adapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        replaceFragment(new RecordFragment());
                        break;
                    case 2:
                        replaceFragment(new CurrencyFragment());
                        break;
                    case 3:
                        replaceFragment(new CategoryFragment());
                        break;
                    case 4:
                        replaceFragment(new AccountFragment());
                        break;
                    case 5:
                        //statistics
                        break;
                    case 6:
                        //planning
                        break;
                    case 8:
                        replaceFragment(new CreditFragment());

                        //credit
                        break;
                    case 9:

                        //debts
                        break;
                    case 10:
                        //settings
                        break;
                    case 11:
                        //rate app
                        break;
                    case 12:
                        //share app
                        break;
                    case 13:
                        //write us
                        break;
                }
                drawer.closeLeftSide();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawer.openLeftSide();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flMain, fragment);
            ft.commit();
        }
    }
}