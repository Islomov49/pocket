package com.jim.pocketaccounter;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.finance.SubCategory;
import com.jim.pocketaccounter.report.AccountDataRow;
import com.jim.pocketaccounter.report.FilterDialog;
import com.jim.pocketaccounter.report.FilterSelectable;
import com.jim.pocketaccounter.report.ReportByAccount;
import com.jim.pocketaccounter.report.TableView;

import net.objecthunter.exp4j.Expression;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by ismoi on 6/15/2016.
 */

public class ReportByAccountFragment extends Fragment implements View.OnClickListener {
    private ImageView ivToolbarMostRight;
    private Spinner spToolbar;
    private String[] titles;
    private String[][] tables;
    private int pos_account = 0,
            pos_currency = 0;

    private Calendar begin, end;
    private SimpleDateFormat simpleDateFormat;
    private DecimalFormat decimalFormat;
    private Account account;
    private Currency currency;
    private TableView tbReportByAccount;
    private FinanceManager financeManager;
    private FilterDialog filterDialog;
    private ReportByAccount reportByAccount;
    private ArrayList<AccountDataRow> sortReportByAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_account, container, false);
        rootView.setBackgroundColor(Color.WHITE);
        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.ic_filter);
        ivToolbarMostRight.setOnClickListener(this);
        PocketAccounter.toolbar.setTitle("");
        PocketAccounter.toolbar.setSubtitle("");
        filterDialog = new FilterDialog(getContext());
        begin = (Calendar) Calendar.getInstance().clone();
        end = (Calendar) Calendar.getInstance().clone();
        spToolbar = (Spinner) PocketAccounter.toolbar.findViewById(R.id.spToolbar);
        spToolbar.setVisibility(View.VISIBLE);
        final ArrayList<String> result = new ArrayList<>();
        financeManager = PocketAccounter.financeManager;
        for (int i = 0; i < financeManager.getAccounts().size(); i++) {
            for (int j = 0; j < financeManager.getCurrencies().size(); j++) {
                result.add(financeManager.getAccounts().get(i).getName() + ", " + financeManager.getCurrencies().get(j).getAbbr());
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, result);

        spToolbar.setAdapter(arrayAdapter);

        spToolbar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos_account = (int) (position / financeManager.getCurrencies().size());
                pos_currency = position % financeManager.getCurrencies().size();
                account = PocketAccounter.financeManager.getAccounts().get(pos_account);
                currency = PocketAccounter.financeManager.getCurrencies().get(pos_currency);

                begin.set(Calendar.DAY_OF_YEAR, end.get(Calendar.DAY_OF_YEAR) - 2);
                begin.set(Calendar.HOUR_OF_DAY, 0);
                begin.set(Calendar.MINUTE, 0);
                begin.set(Calendar.SECOND, 0);
                begin.set(Calendar.MILLISECOND, 0);

                reportByAccount = new ReportByAccount(getContext(), begin, end, account, currency);
                sortReportByAccount = reportByAccount.makeAccountReport();

                tables = new String[sortReportByAccount.size()][titles.length];

                simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                decimalFormat = new DecimalFormat("0.00##");

                for (int i = 0; i < sortReportByAccount.size(); i++) {
                    tables[i][0] = Integer.toString(sortReportByAccount.get(i).getType());
                    tables[i][1] = sortReportByAccount.get(i).getCurrency().getAbbr() + " " +
                            decimalFormat.format(sortReportByAccount.get(i).getAmount());

                    tables[i][2] = simpleDateFormat.format(sortReportByAccount.get(i).getDate().getTime());

                    if (!sortReportByAccount.get(i).getCategory().getName().equals(""))
                        tables[i][3] = sortReportByAccount.get(i).getCategory().getName();
                    if (!sortReportByAccount.get(i).getCategory().getName().equals("")
                            && sortReportByAccount.get(i).getSubCategory() != null)
                        tables[i][3] = sortReportByAccount.get(i).getCategory().getName()
                                + "," + sortReportByAccount.get(i).getSubCategory().getName();
                }
                tbReportByAccount.setTables(tables);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        titles = rootView.getResources().getStringArray(R.array.report_by_account_titles);

        tbReportByAccount = (TableView) rootView.findViewById(R.id.tbReportByAccount);
        tbReportByAccount.setClickable(false);

        tbReportByAccount.setTitles(titles);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolbarMostRight:
                filterDialog.show();
                filterDialog.setOnDateSelectedListener(new FilterSelectable() {
                    @Override
                    public void onDateSelected(Calendar begin, Calendar end) {
                        reportByAccount = new ReportByAccount(getContext(), begin, end, account, currency);
                        sortReportByAccount = reportByAccount.makeAccountReport();

                        tables = new String[sortReportByAccount.size()][titles.length];

                        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        decimalFormat = new DecimalFormat("0.00##");

                        for (int i = 0; i < sortReportByAccount.size(); i++) {
                            tables[i][0] = Integer.toString(sortReportByAccount.get(i).getType());
                            tables[i][1] = sortReportByAccount.get(i).getCurrency().getAbbr() + " " +
                                    decimalFormat.format(sortReportByAccount.get(i).getAmount());

                            tables[i][2] = simpleDateFormat.format(sortReportByAccount.get(i).getDate().getTime());

                            if (!sortReportByAccount.get(i).getCategory().getName().equals(""))
                                tables[i][3] = sortReportByAccount.get(i).getCategory().getName();
                            if (!sortReportByAccount.get(i).getCategory().getName().equals("")
                                    && sortReportByAccount.get(i).getSubCategory() != null)
                                tables[i][3] = sortReportByAccount.get(i).getCategory().getName()
                                        + "," + sortReportByAccount.get(i).getSubCategory().getName();
                        }
                        tbReportByAccount.setTables(tables);
                    }
                });
                break;
        }
    }
}
