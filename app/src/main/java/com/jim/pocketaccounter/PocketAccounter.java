package com.jim.pocketaccounter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jim.pocketaccounter.credit.notificat.AlarmReceiver;
import com.jim.pocketaccounter.credit.notificat.NotificationManagerCredit;
import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.helper.CircleImageView;
import com.jim.pocketaccounter.helper.FABIcon;
import com.jim.pocketaccounter.helper.PockerTag;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.helper.record.RecordExpanseView;
import com.jim.pocketaccounter.helper.record.RecordIncomesView;
import com.jim.pocketaccounter.intropage.IntroIndicator;
import com.jim.pocketaccounter.report.FilterFragment;
import com.jim.pocketaccounter.debt.DebtBorrowFragment;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.helper.LeftMenuAdapter;
import com.jim.pocketaccounter.helper.LeftMenuItem;
import com.jim.pocketaccounter.helper.LeftSideDrawer;
import com.jim.pocketaccounter.syncbase.SignInGoogleMoneyHold;
import com.jim.pocketaccounter.syncbase.SyncBase;
import com.jim.pocketaccounter.report.ReportByAccount;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.jim.pocketaccounter.R.color.toolbar_text_color;

public class PocketAccounter extends AppCompatActivity {
    TextView userName, userEmail;
    CircleImageView userAvatar;
    public static Toolbar toolbar;
    public static LeftSideDrawer drawer;
    private ListView lvLeftMenu;
    public static FinanceManager financeManager;
    private FragmentManager fragmentManager;
    SharedPreferences spref;
    SharedPreferences.Editor ed;
    private RelativeLayout rlRecordsMain, rlRecordIncomes;
    private TextView tvRecordIncome, tvRecordBalanse, tvRecordExpanse;
    private ImageView ivToolbarMostRight;
    private RecordExpanseView expanseView;
    private RecordIncomesView incomeView;
    private Calendar date;
    private Spinner spToolbar;
    private RelativeLayout rlRecordTable;
    SignInGoogleMoneyHold reg;
    boolean downloadnycCanRest = true;
    SyncBase mySync;
    Uri imageUri;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://pocket-accounter.appspot.com");
    DownloadImageTask imagetask;

    public static boolean PRESSED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pocket_accounter);
        spref = getSharedPreferences("infoFirst", MODE_PRIVATE);
        ed = spref.edit();
        if (spref.getBoolean("FIRST_KEY", true)) {
            try {
                Intent first = new Intent(this, IntroIndicator.class);
                startActivity(first);
                finish();
            } finally {

            }
        }
        financeManager = new FinanceManager(this);
        fragmentManager = getSupportFragmentManager();
        mySync = new SyncBase(storageRef, this, "PocketAccounterDatabase");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, toolbar_text_color));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = new LeftSideDrawer(this);
        drawer.setLeftBehindContentView(R.layout.activity_behind_left_simple);
        lvLeftMenu = (ListView) findViewById(R.id.lvLeftMenu);
        fillLeftMenu();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
            if (user.getPhotoUrl() != null) {
                imagetask.execute(user.getPhotoUrl().toString());
                imageUri = user.getPhotoUrl();

            }
        }

        rlRecordsMain = (RelativeLayout) findViewById(R.id.rlRecordExpanses);
        tvRecordIncome = (TextView) findViewById(R.id.tvRecordIncome);
        tvRecordBalanse = (TextView) findViewById(R.id.tvRecordBalanse);
        rlRecordIncomes = (RelativeLayout) findViewById(R.id.rlRecordIncomes);
        ivToolbarMostRight = (ImageView) findViewById(R.id.ivToolbarMostRight);
        spToolbar = (Spinner) toolbar.findViewById(R.id.spToolbar);
        rlRecordTable = (RelativeLayout) findViewById(R.id.rlRecordTable);
        rlRecordTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PRESSED) return;
                replaceFragment(new RecordDetailFragment(date));
                PRESSED = true;
            }
        });
        tvRecordExpanse = (TextView) findViewById(R.id.tvRecordExpanse);
        date = Calendar.getInstance();
        initialize(date);
        //Bu notifikatsiyani bosib prilojeniyaga kirganda fragmenti ochvorishga
        switch (getIntent().getIntExtra("TIP", 0)) {
            case AlarmReceiver.TO_DEBT:
                replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
                break;
            case AlarmReceiver.TO_CRIDET:
                replaceFragment(new CreditTabLay(), PockerTag.CREDITS);
                break;
        }


        (new Thread(new Runnable() {
            @Override
            public void run() {
                NotificationManagerCredit notific = new NotificationManagerCredit(PocketAccounter.this);
                notific.notificSetCredit();
            }
        })).start();

    }

    public Calendar getDate() {
        return date;
    }

    public void initialize(Calendar date) {
        PRESSED = false;
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
                final Dialog dialog = new Dialog(PocketAccounter.this);
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
        if (height * 0.6 > width)
            side = width;
        else
            side = (int) (height * 0.6);
        expanseView = new RecordExpanseView(this, date);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(side, side);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        expanseView.setLayoutParams(lp);
        rlRecordsMain.removeAllViews();
        rlRecordsMain.addView(expanseView);
        incomeView = new RecordIncomesView(this, date);
        RelativeLayout.LayoutParams lpIncomes = new RelativeLayout.LayoutParams(width, width / 4 + (int) (getResources().getDimension(R.dimen.thirty_dp)));
        lpIncomes.addRule(RelativeLayout.CENTER_HORIZONTAL);
        incomeView.setLayoutParams(lpIncomes);
        rlRecordIncomes.removeAllViews();
        rlRecordIncomes.addView(incomeView);
    }

    private void calclulateBalanse(Calendar date) {
        Calendar begTime = (Calendar) date.clone();
        begTime.set(Calendar.HOUR_OF_DAY, 0);
        begTime.set(Calendar.MINUTE, 0);
        begTime.set(Calendar.SECOND, 0);
        begTime.set(Calendar.MILLISECOND, 0);
        Calendar endTime = (Calendar) date.clone();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        endTime.set(Calendar.MILLISECOND, 59);
        ArrayList<FinanceRecord> records = new ArrayList<FinanceRecord>();
        for (int i = 0; i < PocketAccounter.financeManager.getRecords().size(); i++) {
            if (PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(begTime) >= 0 &&
                    PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(endTime) <= 0)
                records.add(PocketAccounter.financeManager.getRecords().get(i));
        }
        double income = 0.0, expanse = 0.0, balanse = 0.0;
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getCategory().getType() == PocketAccounterGeneral.INCOME)
                income = income + PocketAccounterGeneral.getCost(records.get(i));
            else
                expanse = expanse + PocketAccounterGeneral.getCost(records.get(i));
        }
        balanse = income - expanse;
        String mainCurrencyAbbr = PocketAccounter.financeManager.getMainCurrency().getAbbr();
        DecimalFormat decFormat = new DecimalFormat("0.00");
        tvRecordIncome.setText(decFormat.format(income) + mainCurrencyAbbr);
        tvRecordExpanse.setText(decFormat.format(expanse) + mainCurrencyAbbr);
        tvRecordBalanse.setText(decFormat.format(balanse) + mainCurrencyAbbr);
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
        userAvatar = (CircleImageView) findViewById(R.id.userphoto);
        userName = (TextView) findViewById(R.id.tvToolbarName);
        userEmail = (TextView) findViewById(R.id.tvGoogleMail);
        imagetask = new DownloadImageTask(userAvatar);

        //TODO agar upload sync settingsi ichiga qoyilganda reg if usloviyani ichiga qoyiladi
        reg = new SignInGoogleMoneyHold(PocketAccounter.this, new SignInGoogleMoneyHold.UpdateSucsess() {
            @Override
            public void updateToSucsess() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    mySync.uploadBASE(user.getUid(), new SyncBase.ChangeStateLis() {
                        @Override
                        public void onSuccses() {
                            //uploaded base action with UI
                        }

                        @Override
                        public void onFailed(Exception e) {

                        }
                    });
                    userName.setText(user.getDisplayName());
                    userEmail.setText(user.getEmail());
                    if (user.getPhotoUrl() != null) {
                        imagetask.execute(user.getPhotoUrl().toString());
                        imageUri = user.getPhotoUrl();
                    }
                    if (user.getPhotoUrl() != null)
                        Log.d("GOOGLEPHOTO", user.getPhotoUrl() + "");
                }
            }

            @Override
            public void updateToFailed() {
                userName.setText(R.string.try_later);
                userEmail.setText(R.string.err_con);

            }
        });

        FABIcon fabIcon = (FABIcon) findViewById(R.id.fabDrawerNavIcon);
        fabIcon.setImageBitmap(bitmap);
        fabIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FirebaseUser userim = FirebaseAuth.getInstance().getCurrentUser();
                if (userim != null) {
                    drawer.close();
                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PocketAccounter.this);
                            builder.setMessage(R.string.sync_message)
                                    .setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            mySync.uploadBASE(userim.getUid(), new SyncBase.ChangeStateLis() {
                                                @Override
                                                public void onSuccses() {

                                                }

                                                @Override
                                                public void onFailed(Exception e) {
                                                    //oshibka chiqaramiz
                                                }
                                            });

                                        }
                                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                            builder.create().show();
                        }
                    }, 150);
                } else {
                    drawer.close();


                    (new Handler()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (spref.getBoolean("FIRSTSYNC", true)) {
                                reg.openDialog();
                            } else
                                reg.regitUser();
                        }
                    }, 150);
                }
            }
        });
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
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0 && position == 0) {
                    findViewById(R.id.change).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.change).setVisibility(View.GONE);
                }
                drawer.closeLeftSide();
                drawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (position) {
                            case 0:
                                findViewById(R.id.change).setVisibility(View.VISIBLE);
                                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                                    Log.d("sss", "qayta");
                                    FragmentManager fm = getSupportFragmentManager();
                                    for (int i = 0; i < fm.getBackStackEntryCount(); i++)
                                        fm.popBackStack();
                                    initialize(date);
                                }
                                break;
                            case 1:
                                replaceFragment(new CurrencyFragment(), PockerTag.CURRENCY);
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
                            case 5:
                                replaceFragment(new CreditTabLay(), PockerTag.CREDITS);
                                break;
                            case 6:
                                replaceFragment(new CreditTabLay(), PockerTag.CREDITS);
                                //Statistics by account
                                break;
                            case 7:
                                replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
                                //Statistics by income/expanse
                                break;
                            case 8:
                                replaceFragment(new ReportByAccountFragment(), PockerTag.REPORT_ACCOUNT);
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
    public void onBackPressed() {
        android.support.v4.app.Fragment temp00 = getSupportFragmentManager().
                findFragmentById(R.id.flMain);
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
                            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }
                    });
                    builder.create().show();
                } else {
                    AddCreditFragment.to_open_dialog = true;
                    getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    initialize(date);
                    Log.d("sss", "" + getSupportFragmentManager().findFragmentById(R.id.flMain).getTag());
                    String tag = getSupportFragmentManager().findFragmentById(R.id.flMain).getTag();
                    if (tag.matches("Addcredit")
                            || tag.matches("InfoFragment")) {
                        replaceFragment(new CreditTabLay(), com.jim.pocketaccounter.debt.PockerTag.CREDITS);
                    }
                    switch (tag) {
                        case PockerTag.ACCOUNT:
                        case PockerTag.CATEGORY:
                        case PockerTag.CURRENCY:
                        case PockerTag.CREDITS:
                        case PockerTag.REPORT_ACCOUNT:
                        case PockerTag.REPORT_INCOM_EXPENSE:
                        case PockerTag.REPORT_CATEGORY:
                        case PockerTag.DEBTS: {
                            findViewById(R.id.change).setVisibility(View.VISIBLE);
                            initialize(date);
                            break;
                        }
                    }
                }
            } else {
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Log.d("sss", "ll");
                if (getSupportFragmentManager().findFragmentById(R.id.flMain) != null) {
                    if (fragmentManager.findFragmentById(R.id.flMain).getTag() == null) {
                        Log.d("ssss", fragmentManager.findFragmentById(R.id.flMain).getClass().getName());
                        switch (fragmentManager.findFragmentById(R.id.flMain).getClass().getName()) {
                            case "com.jim.pocketaccounter.RecordEditFragment":
                                initialize(date);
                                break;
                            case "com.jim.pocketaccounter.CurrencyEditFragment":
                            case "com.jim.pocketaccounter.CurrencyChooseFragment":
                                findViewById(R.id.change).setVisibility(View.VISIBLE);
                                replaceFragment(new CurrencyFragment(), PockerTag.CURRENCY);
                                break;
                            case "com.jim.pocketaccounter.RootCategoryEditFragment": {
                                replaceFragment(new CategoryFragment(), PockerTag.CATEGORY);
                                break;
                            }
                            case "com.jim.pocketaccounter.debt.InfoDebtBorrowFragment":
                            case "com.jim.pocketaccounter.debt.AddBorrowFragment": {
                                DebtBorrowFragment fragment = new DebtBorrowFragment();
                                fragment.setArguments(fragmentManager.findFragmentById(R.id.flMain).getArguments());
                                replaceFragment(fragment, PockerTag.DEBTS);
                                break;
                            }
                        }
                        return;
                    }
                    if (fragmentManager.findFragmentById(R.id.flMain).getTag().matches(PockerTag.ACCOUNT)) {
                        replaceFragment(new AccountFragment(), PockerTag.ACCOUNT);
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
            for (int i = 0; i < size; i++) {
                fragmentManager.popBackStack();
            }
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
        financeManager.saveAllDatas();
        if (imagetask != null)
            imagetask.cancel(true);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (downloadnycCanRest && imageUri != null) {
            imagetask.execute(imageUri.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        findViewById(R.id.change).setVisibility(View.VISIBLE);
        if (requestCode == SignInGoogleMoneyHold.RC_SIGN_IN) {
            reg.regitRequstGet(data);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;

            File file = new File(getFilesDir(), "userphoto.jpg");
            Log.d("UPDATEP", "opened " + file.getAbsolutePath() + "");

            if (file.exists()) {

                Log.d("UPDATEP", "opened " + file.getAbsolutePath() + "");

                bmImage.setImageURI(Uri.parse(file.getAbsolutePath()));
            }
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;

            for (; true; ) {
                if (isCancelled()) break;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                    break;
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }

            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                downloadnycCanRest = false;
                bmImage.setImageBitmap(result);
                File file = new File(getFilesDir(), "userphoto.jpg");
                FileOutputStream out = null;

                try {
                    out = new FileOutputStream(file);
                    result.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Log.d("UPDATEP", "saved " + file.getAbsolutePath() + "");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

        }
    }

    private ProgressDialog mProgressDialog;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}