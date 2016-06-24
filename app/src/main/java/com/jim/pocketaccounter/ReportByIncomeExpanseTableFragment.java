package com.jim.pocketaccounter;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jim.pocketaccounter.report.FilterDialog;
import com.jim.pocketaccounter.report.FilterSelectable;
import com.jim.pocketaccounter.report.IncomeExpanseDataRow;
import com.jim.pocketaccounter.report.IncomeExpanseReport;
import com.jim.pocketaccounter.report.ReportByCategoryDialogAdapter;
import com.jim.pocketaccounter.report.ReportByIncomeExpanseDialogAdapter;
import com.jim.pocketaccounter.report.TableView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportByIncomeExpanseTableFragment extends Fragment {
    private TableView table;
    private IncomeExpanseReport data;
    private ArrayList<IncomeExpanseDataRow> dataRows;
    private Calendar begin, end;
    private TextView tvTotalIncome, tvAverageIncome,
                     tvTotalExpanse, tvAverageExpanse,
                     tvTotalProfit, tvAverageProfit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_income_expanse, container, false);
        String[] titles = getResources().getStringArray(R.array.report_by_income_expanse_table_titles);
        table = (TableView) rootView.findViewById(R.id.tvReportByIncomeExpanse);
        table.setClickable(true);
        table.setTitles(titles);
        tvTotalIncome = (TextView) rootView.findViewById(R.id.tvTotalIncome);
        tvAverageIncome = (TextView) rootView.findViewById(R.id.tvAverageIncome);
        tvTotalExpanse = (TextView) rootView.findViewById(R.id.tvTotalExpanse);
        tvAverageExpanse = (TextView) rootView.findViewById(R.id.tvAverageExpanse);
        tvTotalProfit = (TextView) rootView.findViewById(R.id.tvTotalProfit);
        tvAverageProfit = (TextView) rootView.findViewById(R.id.tvAverageProfit);
        init();
        data = new IncomeExpanseReport(getContext(), begin, end);
        dataRows = data.makeReport();
        drawTable(dataRows);
        table.setOnTableClickListener(new TableView.ClickableTable() {
            @Override
            public void onTableClick(int row) {
                if (dataRows.get(row).getTotalIncome() == 0 && dataRows.get(row).getTotalExpanse() == 0) return;
                IncomeExpanseDataRow dataRow = dataRows.get(row);
                final Dialog dialog=new Dialog(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.report_by_income_expanse_info, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                TextView tvReportByIncomeExpanseDate = (TextView) dialogView.findViewById(R.id.tvReportByIncomeExpanseDate);
                SimpleDateFormat format = new SimpleDateFormat("dd LLL, yyyy");
                tvReportByIncomeExpanseDate.setText(format.format(dataRow.getDate().getTime()));
                ListView lvReportByIncomeExpanseInfo = (ListView) dialogView.findViewById(R.id.lvReportByIncomeExpanseInfo);
                ImageView ivReportByCategoryClose = (ImageView) dialogView.findViewById(R.id.ivReportByCategoryClose);
                ivReportByCategoryClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ReportByIncomeExpanseDialogAdapter adapter = new ReportByIncomeExpanseDialogAdapter(getContext(), dataRow.getDetails());
                lvReportByIncomeExpanseInfo.setAdapter(adapter);
                TextView tvReportByIncomeExpanseTotalIncome = (TextView) dialogView.findViewById(R.id.tvReportByIncomeExpanseTotalIncome);
                DecimalFormat decimalFormat = new DecimalFormat("0.00##");
                tvReportByIncomeExpanseTotalIncome.setText(decimalFormat.format(dataRow.getTotalExpanse())+PocketAccounter.financeManager.getMainCurrency().getAbbr());
                TextView tvReportByIncomeExpanseExpanse = (TextView) dialogView.findViewById(R.id.tvReportByIncomeExpanseExpanse);
                tvReportByIncomeExpanseExpanse.setText(decimalFormat.format(dataRow.getTotalIncome())+PocketAccounter.financeManager.getMainCurrency().getAbbr());
                TextView tvReportByIncomeExpanseProfit = (TextView) dialogView.findViewById(R.id.tvReportByIncomeExpanseProfit);
                tvReportByIncomeExpanseProfit.setText(decimalFormat.format(dataRow.getTotalProfit())+PocketAccounter.financeManager.getMainCurrency().getAbbr());
                dialog.show();
            }
        });
        calculateDatas();
        return rootView;
    }
    public void invalidate(Calendar begin, Calendar end) {
        this.begin = (Calendar) begin.clone();
        this.end = (Calendar) end.clone();
        data = new IncomeExpanseReport(getContext(), this.begin, this.end);
        dataRows = data.makeReport();
        drawTable(dataRows);
        this.table.invalidate();
        calculateDatas();
    }
    private void calculateDatas() {
        long aDay = 1000*60*60*24;
        long countOfDays = (end.getTimeInMillis()-begin.getTimeInMillis())/aDay;
        DecimalFormat format = new DecimalFormat("0.00##");
        String abbr = PocketAccounter.financeManager.getMainCurrency().getAbbr();
        double totalIncome = 0.0, totalExpanse = 0.0, totalProfit = 0.0,
                averageIncome = 0.0, averageExpanse = 0.0, averageProfit = 0.0;
        for (int i=0; i<dataRows.size(); i++) {
            totalIncome = totalIncome + dataRows.get(i).getTotalIncome();
            totalExpanse = totalExpanse + dataRows.get(i).getTotalExpanse();
            totalProfit = totalProfit + dataRows.get(i).getTotalProfit();
        }
        averageIncome = totalIncome/countOfDays;
        averageExpanse = totalExpanse/countOfDays;
        averageProfit = totalProfit/countOfDays;
        tvTotalIncome.setText(getString(R.string.report_income_expanse_total_income)+format.format(totalIncome)+abbr);
        tvAverageIncome.setText(getString(R.string.report_income_expanse_aver_income)+format.format(averageIncome)+abbr);
        tvTotalExpanse.setText(getString(R.string.report_income_expanse_total_expanse)+format.format(totalExpanse)+abbr);
        tvAverageExpanse.setText(getString(R.string.report_income_expanse_aver_expanse)+format.format(averageExpanse)+abbr);
        tvTotalProfit.setText(getString(R.string.report_income_expanse_total_profit)+format.format(totalProfit)+abbr);
        tvAverageProfit.setText(getString(R.string.report_income_expanse_aver_profit)+format.format(averageProfit)+abbr);
    }
    private void init() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String setting = sharedPreferences.getString("report_filter", "0");
        begin = Calendar.getInstance();
        end = Calendar.getInstance();
        switch (setting) {
            case "0":
                begin.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case "1":
                begin.add(Calendar.DAY_OF_MONTH, -2);
                break;
            case "2":
                begin.add(Calendar.DAY_OF_MONTH, -6);
                break;
        }
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 59);
    }
    private void drawTable(ArrayList<IncomeExpanseDataRow> datas) {
        String[][] table = new String[datas.size()][4];
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00##");
        String abbr = PocketAccounter.financeManager.getMainCurrency().getAbbr();
        for (int i = 0; i<datas.size(); i++) {
            table[i][0] = format.format(datas.get(i).getDate().getTime());
            table[i][1] = decimalFormat.format(datas.get(i).getTotalExpanse())+abbr;
            table[i][2] = decimalFormat.format(datas.get(i).getTotalIncome())+abbr;
            table[i][3] = decimalFormat.format(datas.get(i).getTotalProfit())+abbr;
        }
        this.table.setTables(table);
    }
}
