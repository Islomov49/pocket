package com.jim.pocketaccounter.report;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class BarReportView extends LinearLayout {
    private HorizontalBarChart barChart;
    private ArrayList<IncomeExpanseDataRow> datas;
    public BarReportView(Context context) {
        super(context);
        barChart = new HorizontalBarChart(context);
        barChart.setDescription("");
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        Legend l = barChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_INSIDE);
        l.setYOffset(0f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setValueFormatter(new LargeValueFormatter());
        leftAxis.setDrawGridLines(true);
        leftAxis.setSpaceTop(30f);
        barChart.getAxisRight().setEnabled(false);
        Calendar begin = Calendar.getInstance();
        begin.set(2016, Calendar.JUNE, 15);
        Calendar end = Calendar.getInstance();
        IncomeExpanseReport report = new IncomeExpanseReport(getContext(), begin, end);
        datas = report.makeReport();
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        barChart.setLayoutParams(lp);
        addView(barChart);
        drawReport(datas);
    }
    public BarChart getBarChart() {return barChart;}
    public BarReportView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BarReportView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BarReportView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public ArrayList<IncomeExpanseDataRow> getDatas() {return datas;}
    public void drawReport(ArrayList<IncomeExpanseDataRow> datas) {
        ArrayList<String> xVals = new ArrayList<String>();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        for (int i=0; i<datas.size(); i++) {
            xVals.add(format.format(datas.get(i).getDate().getTime()));
        }
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals2 = new ArrayList<BarEntry>();
        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        for (int i = 0; i < datas.size(); i++) {
            yVals1.add(new BarEntry((float) datas.get(i).getTotalIncome(), i));
            yVals2.add(new BarEntry((float) datas.get(i).getTotalExpanse(), i));
            yVals3.add(new BarEntry((float) datas.get(i).getTotalProfit(), i));
        }
        BarDataSet set1, set2, set3;
         // create 3 datasets with different types
        set1 = new BarDataSet(yVals1, getResources().getString(R.string.income));
        // set1.setColors(ColorTemplate.createColors(getApplicationContext(),
        // ColorTemplate.FRESH_COLORS));
        set1.setColor(Color.rgb(104, 241, 175));
        set2 = new BarDataSet(yVals2, getResources().getString(R.string.expanse));
        set2.setColor(Color.rgb(164, 228, 251));
        set3 = new BarDataSet(yVals3, getResources().getString(R.string.profit));
        set3.setColor(Color.rgb(242, 247, 158));
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        BarData data = new BarData(xVals, dataSets);
        data.setValueFormatter(new LargeValueFormatter());
        // add space between the dataset groups in percent of bar-width
        data.setGroupSpace(80f);
        barChart.setData(data);
        barChart.invalidate();
    }
}