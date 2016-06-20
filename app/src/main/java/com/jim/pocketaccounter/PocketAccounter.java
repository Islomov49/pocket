package com.jim.pocketaccounter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jim.pocketaccounter.helper.FABIcon;
import com.jim.pocketaccounter.helper.PockerTag;
import com.jim.pocketaccounter.report.FilterFragment;
import com.jim.pocketaccounter.debt.DebtBorrowFragment;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.helper.LeftMenuAdapter;
import com.jim.pocketaccounter.helper.LeftMenuItem;
import com.jim.pocketaccounter.helper.LeftSideDrawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import static com.jim.pocketaccounter.R.color.toolbar_text_color;
public class PocketAccounter extends AppCompatActivity {
    public static Toolbar toolbar;
    public static LeftSideDrawer drawer;
    private ListView lvLeftMenu;
    public static FinanceManager financeManager;
    private boolean tek = false;
    private boolean first = false;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_accounter);
        financeManager = new FinanceManager(this);
        fragmentManager = getSupportFragmentManager();
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
        if (financeManager.getCurrencies().isEmpty()) {
            replaceFragment(new CurrencyChooseFragment());
            first = true;
        }
        else
            replaceFragment(new RecordFragment(Calendar.getInstance()));
    }
    private void fillLeftMenu() {
        String[] cats = getResources().getStringArray(R.array.drawer_cats);
        String[] financeSubItemTitles = getResources().getStringArray(R.array.finance_subitems);
        String[] financeSubItemIcons = getResources().getStringArray(R.array.finance_subitem_icons);
        String[] statisticsSubItemTitles = getResources().getStringArray(R.array.statistics_subitems);
        String[] statisticsSubItemIcons = getResources().getStringArray(R.array.statistics_subitems_icons);
        String[] debtSubItemTitles = getResources().getStringArray(R.array.debts_subitems);
        String[] debtSubItemIcons = getResources().getStringArray(R.array.debts_subitem_icons);
        ArrayList<LeftMenuItem> items = new ArrayList<LeftMenuItem>();
        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
        Bitmap bitmap = Bitmap.createScaledBitmap(temp, (int)getResources().getDimension(R.dimen.twentyfour_dp), (int)getResources().getDimension(R.dimen.twentyfour_dp), false);
        FABIcon fabIcon = (FABIcon)findViewById(R.id.fabDrawerNavIcon);
        fabIcon.setImageBitmap(bitmap);
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
        LeftMenuItem statistics = new LeftMenuItem(cats[2], R.drawable.drawer_statistics);
        statistics.setGroup(true);
        items.add(statistics);
        for (int i=0; i<statisticsSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(statisticsSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(statisticsSubItemTitles[i], resId);
            subItem.setGroup(false);
            items.add(subItem);
        }
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
                String tag = null;
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        tag = PockerTag.HOME;
                        fragment = new RecordFragment(Calendar.getInstance());
                        replaceFragment(fragment, tag);
                        break;
                    case 2:
                        replaceFragment(new CurrencyFragment(), PockerTag.CURRENCY);
                        //Currency management
                        break;
                    case 3:
                        replaceFragment(new CategoryFragment(), PockerTag.CATEGORY);
                        //Category management
                        break;
                    case 4:
                        replaceFragment(new AccountFragment(), PockerTag.ACCOUNT);
                        //Accounting management
                        break;
                    case 6:
                        replaceFragment(new CreditTabLay(), PockerTag.CREDITS);
                        //Statistics by account
                        break;
                    case 7:
                        replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
                        //Statistics by income/expanse
                        break;
                    case 9:
                        replaceFragment(new ReportByAccountFragment(), PockerTag.REPORT_ACCOUNT);
                        // accounting debt
                        break;
                    case 10:
                        replaceFragment(new TableBarFragment(), PockerTag.REPORT_INCOM_EXPENSE);
                        break;
                    case 11:
                        replaceFragment(new TableBarFragment(), PockerTag.REPORT_CATEGORY);
                        break;
                    case 12:
                        Intent settings = new Intent(PocketAccounter.this, SettingsActivity.class);
                        startActivity(settings);
                        break;
                    case 13:
                        Intent rate_app_web = new Intent(Intent.ACTION_VIEW);
                        rate_app_web.setData(Uri.parse(getString(R.string.rate_app_web)));
                        startActivity(rate_app_web);
                        break;
                    case 14:
                        Intent Email = new Intent(Intent.ACTION_SEND);
                        Email.setType("text/email");
                        Email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_app));
                        Email.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_text));
                        startActivity(Intent.createChooser(Email, getString(R.string.share_app)));
                        break;
                    case 15:
                        openGmail(PocketAccounter.this, new String[]{getString(R.string.to_email)}, getString(R.string.feedback_subject), getString(R.string.feedback_content));
                        break;
                }
                drawer.closeLeftSide();
            }
        });
    }
    public static void openGmail(Activity activity, String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = activity.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        activity.startActivity(emailIntent);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        android.support.v4.app.Fragment temp00 = getSupportFragmentManager().
                findFragmentById(R.id.flMain);
        if (!drawer.isClosed()) {
            drawer.closeLeftSide();
        } else
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                if (temp00.getTag() != null) {
                    if (temp00.getTag().equals(AddCreditFragment.OPENED_TAG) && AddCreditFragment.to_open_dialog) {
                        Log.d("somethinkkk", "DIALOG OPENED IN ADDCREDIT");
                        final AlertDialog.Builder builder = new AlertDialog.Builder(PocketAccounter.this);
                        builder.setMessage("You have not added credit, just want to go out?")
                                .setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                }).setNegativeButton("DISCARD", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                getSupportFragmentManager().popBackStack();
                            }
                        });
                        builder.create().show();
                    } else {
                        AddCreditFragment.to_open_dialog = true;
                        getSupportFragmentManager().popBackStack();
                        if (first) {
                            replaceFragment(new RecordFragment(Calendar.getInstance()), PockerTag.HOME);
                            first = !first;
                        }
                    }
                } else {
                    getSupportFragmentManager().popBackStack();
                    if (getSupportFragmentManager().findFragmentById(R.id.flMain) != null) {
                        tek = true;
                        if (getSupportFragmentManager().findFragmentById(R.id.flMain).getTag() != null && getSupportFragmentManager().findFragmentById(R.id.flMain).getTag().matches(PockerTag.DEBTS))
                            replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
                    }
                }
            } else {
                if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }

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
            for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount() - 2; i++) {
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .add(R.id.flMain, fragment)
                    .commit();
        }
    }
    public void replaceFragment(Fragment fragment, String tag) {
        if (fragment != null) {
            int size = fragmentManager.getBackStackEntryCount();
            for (int i = 0; i < size- 1 - (tek ? 1 : 0); i++) {
                fragmentManager.popBackStack();
            }

//
//            klass kl = new klass(getSupportFragmentManager(), tag, fragment);

//            MyTask myTask = new MyTask();
//            myTask.execute(kl);

            tek = false;
            long start = System.currentTimeMillis()+1;

            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction
                    .addToBackStack(null)
                    .add(R.id.flMain, fragment, tag)
                    .commit();

            long end = System.currentTimeMillis();

            Log.d("task", ""+ (end - start));
        }
    }

    class klass {
        private FragmentManager manager;
        private String tag;
        private Fragment fragment;

        public klass(FragmentManager manager, String tag, Fragment fragment) {
            this.manager = manager;
            this.tag = tag;
            this.fragment = fragment;
        }

        public FragmentManager getManager() {
            return manager;
        }

        public String getTag() {
            return tag;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }

    private class MyTask extends AsyncTask<klass, Void, Void> {
        klass ll;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(klass... fragmentManagers) {
            ll = fragmentManagers[0];
            for (int i = 0; i < ll.getManager().getBackStackEntryCount() - 1 - (tek ? 1 : 0); i++) {
                getSupportFragmentManager().popBackStack();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.flMain, ll.getFragment(), ll.getTag())
                    .commit();
        }
    }
}