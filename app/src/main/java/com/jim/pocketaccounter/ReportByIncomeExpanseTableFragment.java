package com.jim.pocketaccounter;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jim.pocketaccounter.report.IncomeExpanseDataRow;
import com.jim.pocketaccounter.report.IncomeExpanseReport;
import com.jim.pocketaccounter.report.TableView;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportByIncomeExpanseTableFragment extends Fragment {
    private TableView table;
    private IncomeExpanseReport data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_income_expanse, container, false);
        String[] titles = getResources().getStringArray(R.array.report_by_income_expanse_table_titles);
        table = (TableView) rootView.findViewById(R.id.tvReportByIncomeExpanse);
        table.setClickable(true);
        table.setTitles(titles);
        table.setOnTableClickListener(new TableView.ClickableTable() {
            @Override
            public void onTableClick(String string) {

            }
        });
        Calendar begin = Calendar.getInstance();
        begin.set(2016, Calendar.JUNE, 1);
        Calendar end = Calendar.getInstance();
        data = new IncomeExpanseReport(getContext(), begin, end);
        drawTable(data);
        return rootView;
    }
    private void drawTable(IncomeExpanseReport data) {
        ArrayList<IncomeExpanseDataRow> datas = data.makeReport();
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
