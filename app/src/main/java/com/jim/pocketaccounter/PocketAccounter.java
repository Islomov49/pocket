package com.jim.pocketaccounter;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.helper.FABIcon;
import com.jim.pocketaccounter.helper.PockerTag;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.helper.record.RecordExpanseView;
import com.jim.pocketaccounter.helper.record.RecordIncomesView;
import com.jim.pocketaccounter.debt.DebtBorrowFragment;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.helper.LeftMenuAdapter;
import com.jim.pocketaccounter.helper.LeftMenuItem;
import com.jim.pocketaccounter.helper.LeftSideDrawer;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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
    private Fragment current;
    private RelativeLayout rlRecordsMain, rlRecordIncomes;
    private TextView tvRecordIncome, tvRecordBalanse, tvRecordExpanse;
    private ImageView ivToolbarMostRight;
    private RecordExpanseView expanseView;
    private RecordIncomesView incomeView;
    private Calendar date;
    private Spinner spToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_accounter);
        try {
            financeManager = new FinanceManager(this);
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, toolbar_text_color));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = new LeftSideDrawer(this);
        drawer.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        lvLeftMenu = (ListView) findViewById(R.id.lvLeftMenu);
        fillLeftMenu();
        rlRecordsMain = (RelativeLayout) findViewById(R.id.rlRecordExpanses);
        tvRecordIncome = (TextView) findViewById(R.id.tvRecordIncome);
        tvRecordBalanse = (TextView) findViewById(R.id.tvRecordBalanse);
        rlRecordIncomes = (RelativeLayout) findViewById(R.id.rlRecordIncomes);
        ivToolbarMostRight = (ImageView) findViewById(R.id.ivToolbarMostRight);
        spToolbar = (Spinner) toolbar.findViewById(R.id.spToolbar);
        tvRecordBalanse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new RecordDetailFragment(date));
            }
        });
        tvRecordExpanse = (TextView) findViewById(R.id.tvRecordExpanse);
        date = Calendar.getInstance();
        initialize(date);
    }
    public Calendar getDate() {
        return date;
    }
    public void initialize(Calendar date) {
        toolbar.setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openLeftSide();
            }
        });
        spToolbar.setVisibility(View.GONE);
        ivToolbarMostRight.setImageResource(R.drawable.finance_calendar);
        ivToolbarMostRight.setVisibility(View.VISIBLE);
        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(PocketAccounter.this);
                View dialogView = getLayoutInflater().inflate(R.layout.date_picker, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                final DatePicker dp = (DatePicker) dialogView.findViewById(R.id.dp);
                ImageView ivDatePickOk = (ImageView) dialogView.findViewById(R.id.ivDatePickOk);
                ivDatePickOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, dp.getYear());
                        calendar.set(Calendar.MONTH, dp.getMonth());
                        calendar.set(Calendar.DAY_OF_MONTH, dp.getDayOfMonth());
                        PocketAccounter.this.date = (Calendar) calendar.clone();
                        initialize(PocketAccounter.this.date);
                        dialog.dismiss();
                    }
                });
                ImageView ivDatePickCancel = (ImageView) dialogView.findViewById(R.id.ivDatePickCancel);
                ivDatePickCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        calclulateBalanse(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd,LLL yyyy");
        toolbar.setSubtitle(dateFormat.format(date.getTime()));
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int side = 0;
        if (height*0.6 > width)
            side =  width;
        else
            side = (int)(height*0.6);
        expanseView = new RecordExpanseView(this, date);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(side, side);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        expanseView.setLayoutParams(lp);
        rlRecordsMain.removeAllViews();
        rlRecordsMain.addView(expanseView);
        incomeView = new RecordIncomesView(this, date);
        RelativeLayout.LayoutParams lpIncomes = new RelativeLayout.LayoutParams(width, width/4+(int)(getResources().getDimension(R.dimen.thirty_dp)));
        lpIncomes.addRule(RelativeLayout.CENTER_HORIZONTAL);
        incomeView.setLayoutParams(lpIncomes);
        rlRecordIncomes.removeAllViews();
        rlRecordIncomes.addView(incomeView);
    }
    private void calclulateBalanse(Calendar date) {
        Calendar begTime = (Calendar)date.clone();
        begTime.set(Calendar.HOUR_OF_DAY, 0);
        begTime.set(Calendar.MINUTE, 0);
        begTime.set(Calendar.SECOND, 0);
        begTime.set(Calendar.MILLISECOND, 0);
        Calendar endTime = (Calendar)date.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MILLISECOND, 59);
        ArrayList<FinanceRecord> records = new ArrayList<FinanceRecord>();
        for (int i=0; i<PocketAccounter.financeManager.getRecords().size(); i++) {
            if (PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(begTime)>=0 &&
                    PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(endTime)<=0)
                records.add(PocketAccounter.financeManager.getRecords().get(i));
        }
        double income = 0.0, expanse = 0.0, balanse = 0.0;
        for (int i=0; i<records.size(); i++) {
            if (records.get(i).getCategory().getType() == PocketAccounterGeneral.INCOME)
                income = income + PocketAccounterGeneral.getCost(records.get(i));
            else
                expanse = expanse + PocketAccounterGeneral.getCost(records.get(i));
        }
        balanse = income - expanse;
        String mainCurrencyAbbr = PocketAccounter.financeManager.getMainCurrency().getAbbr();
        DecimalFormat decFormat = new DecimalFormat("0.00");
        tvRecordIncome.setText(decFormat.format(income)+mainCurrencyAbbr);
        tvRecordExpanse.setText(decFormat.format(expanse)+mainCurrencyAbbr);
        tvRecordBalanse.setText(decFormat.format(balanse)+mainCurrencyAbbr);
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
        Bitmap bitmap = Bitmap.createScaledBitmap(temp, (int) getResources().getDimension(R.dimen.twentyfour_dp), (int) getResources().getDimension(R.dimen.twentyfour_dp), false);
        FABIcon fabIcon = (FABIcon) findViewById(R.id.fabDrawerNavIcon);
        fabIcon.setImageBitmap(bitmap);
        LeftMenuItem main = new LeftMenuItem(cats[0], R.drawable.drawer_home);
        main.setGroup(true);
        items.add(main);
        LeftMenuItem finance = new LeftMenuItem(cats[1], R.drawable.drawer_finance);
        finance.setGroup(true);
        items.add(finance);
        for (int i = 0; i < financeSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(financeSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(financeSubItemTitles[i], resId);
            subItem.setGroup(false);
            items.add(subItem);
        }
        LeftMenuItem debts = new LeftMenuItem(cats[3], R.drawable.drawer_debts);
        debts.setGroup(true);
        items.add(debts);
        for (int i = 0; i < debtSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(debtSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(debtSubItemTitles[i], resId);
            subItem.setGroup(false);
            items.add(subItem);
        }
        LeftMenuItem statistics = new LeftMenuItem(cats[2], R.drawable.drawer_statistics);
        statistics.setGroup(true);
        items.add(statistics);
        for (int i = 0; i < statisticsSubItemTitles.length; i++) {
            int resId = getResources().getIdentifier(statisticsSubItemIcons[i], "drawable", getPackageName());
            LeftMenuItem subItem = new LeftMenuItem(statisticsSubItemTitles[i], resId);
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
            public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
                findViewById(R.id.change).setVisibility(View.INVISIBLE);
                drawer.closeLeftSide();
                drawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        findViewById(R.id.changel).setVisibility(View.INVISIBLE);
                        switch (position) {
                            case 0:
                                for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
                                    fragmentManager.popBackStack();
                                }
                                findViewById(R.id.change).setVisibility(View.VISIBLE);
                                initialize(date);
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
                                replaceFragment(new ReportByCategory(), PockerTag.REPORT_CATEGORY);
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

                    }
                }, 180);
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
        initialize(date);
        if (!drawer.isClosed()) {
            drawer.closeLeftSide();
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        } else {
            if (temp00.getTag() != null) {
                if (temp00.getTag().equals(AddCreditFragment.OPENED_TAG) && AddCreditFragment.to_open_dialog) {
                    //Sardor
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
                    switch (getSupportFragmentManager().findFragmentById(R.id.flMain).getTag()) {
                        case PockerTag.ACCOUNT:
                        case PockerTag.CATEGORY:
                        case PockerTag.CURRENCY:
                        case PockerTag.CREDITS:
                        case PockerTag.DEBTS: {
                            findViewById(R.id.change).setVisibility(View.VISIBLE);
                            initialize(date);
                            break;
                        }
                    }
                }
            } else {
                getSupportFragmentManager().popBackStack();
                if (getSupportFragmentManager().findFragmentById(R.id.flMain) != null) {
                    tek = true;
                    if (fragmentManager.findFragmentById(R.id.flMain).getTag() == null) return;
                    if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.HOME)) {
                        findViewById(R.id.change).setVisibility(View.VISIBLE);
                        initialize(date);
                    } else if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.DEBTS)) {
                        replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
                    } else if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.CURRENCY)) {
                        replaceFragment(new CurrencyFragment(), PockerTag.CURRENCY);
                    } else if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.CATEGORY)) {
                        replaceFragment(new CategoryFragment(), PockerTag.CATEGORY);
                    } else if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.ACCOUNT)) {
                        replaceFragment(new AccountFragment(), PockerTag.ACCOUNT);
                    }
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openLeftSide();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void replaceFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .add(R.id.flMain, fragment)
                    .commit();
        }
    }

    public void replaceFragment(final Fragment fragment, final String tag) {
        if (fragment != null) {
            int size = fragmentManager.getBackStackEntryCount();
            if (fragmentManager.getFragments() != null)
                Log.d("fm", ""+fragmentManager.getFragments().size()+" "+size);
                for (int i = 0; i < size; i++) {
                    fragmentManager.popBackStack();
                }
            tek = false;
            current = fragment;
            fragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .add(R.id.flMain, fragment, tag)
                    .commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        financeManager.loadSaveDatabase(1);
    }
}