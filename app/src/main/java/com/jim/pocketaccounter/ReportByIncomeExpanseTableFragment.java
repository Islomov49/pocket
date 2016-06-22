package com.jim.pocketaccounter;


import android.app.Dialog;
import android.os.Bundle;
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

import com.jim.pocketaccounter.report.IncomeExpanseDataRow;
import com.jim.pocketaccounter.report.IncomeExpanseReport;
import com.jim.pocketaccounter.report.ReportByCategoryDialogAdapter;
import com.jim.pocketaccounter.report.ReportByIncomeExpanseDialogAdapter;
import com.jim.pocketaccounter.report.TableView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportByIncomeExpanseTableFragment extends Fragment {
    private TableView table;
    private IncomeExpanseReport data;
    private ArrayList<IncomeExpanseDataRow> dataRows;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_income_expanse, container, false);
        String[] titles = getResources().getStringArray(R.array.report_by_income_expanse_table_titles);
        table = (TableView) rootView.findViewById(R.id.tvReportByIncomeExpanse);
        table.setClickable(true);
        table.setTitles(titles);
        Calendar begin = Calendar.getInstance();
        begin.set(2016, Calendar.JUNE, 1);
        Calendar end = Calendar.getInstance();
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
        return rootView;
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
