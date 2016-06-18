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

    private Calendar begin, end;
    private int pos_account = 0,
                pos_currency = 0;

    private Account account;
    private Currency currency;

    private TableView tbReportByAccount;
    private FinanceManager financeManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_account, container, false);
        rootView.setBackgroundColor(Color.WHITE);

        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.add_green);
        ivToolbarMostRight.setOnClickListener(this);

        PocketAccounter.toolbar.setTitle("Account");
        PocketAccounter.toolbar.setSubtitle("");

        spToolbar = (Spinner) PocketAccounter.toolbar.findViewById(R.id.spToolbar);
        spToolbar.setVisibility(View.VISIBLE);

        ArrayList<String> result = new ArrayList<>();
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
                FilterDialog filterDialog = new FilterDialog(getContext());
                filterDialog.show();
                filterDialog.setOnDateSelectedListener(new FilterSelectable() {
                    @Override
                    public void onDateSelected(Calendar begin, Calendar end) {
                        account = PocketAccounter.financeManager.getAccounts().get(pos_account);
                        currency = PocketAccounter.financeManager.getCurrencies().get(pos_currency);

                        ReportByAccount reportByAccount = new ReportByAccount(getContext(), begin, end, account, currency);
                        ArrayList<AccountDataRow> sortReportByAccount = reportByAccount.makeAccountReport();

                        String[][] tables = new String[sortReportByAccount.size()][titles.length];

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

                        for (int i = 0; i < sortReportByAccount.size(); i++) {
                            tables[i][0] = String.valueOf(sortReportByAccount.get(i).getType());
                            tables[i][1] = sortReportByAccount.get(i).getCurrency().getName();
                            tables[i][2] = String.valueOf(sortReportByAccount.get(i).getAmount());

                            tables[i][3] = simpleDateFormat.format(sortReportByAccount.get(i).getDate().getTime());

                            if (!sortReportByAccount.get(i).getCategory().getName().equals(""))
                                tables[i][4] = sortReportByAccount.get(i).getCategory().getName();
                            if (!sortReportByAccount.get(i).getCategory().getName().equals("")
                                    && sortReportByAccount.get(i).getSubCategory() != null)
                                tables[i][4] = sortReportByAccount.get(i).getCategory().getName()
                                        + "," + sortReportByAccount.get(i).getSubCategory().getName();
                        }
                        tbReportByAccount.setTables(tables);
                    }
                });
                break;
        }
    }
}
