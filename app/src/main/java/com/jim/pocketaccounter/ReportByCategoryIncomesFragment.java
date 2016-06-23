package com.jim.pocketaccounter;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.report.CategoryDataRow;
import com.jim.pocketaccounter.report.CategoryReportView;
import com.jim.pocketaccounter.report.ReportByCategoryDialogAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ReportByCategoryIncomesFragment extends Fragment implements OnChartValueSelectedListener {
    private LinearLayout llReportByCategory;
    private Button btnReportByCategoryDetail;
    private Animation inFromDown, goToDown;
    private CategoryReportView categoryReportView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_by_category_expanse, container, false);
        btnReportByCategoryDetail = (Button) rootView.findViewById(R.id.btnReportByCategoryDetail);
        inFromDown = AnimationUtils.loadAnimation(getContext(), R.anim.in_from_down);
        goToDown = AnimationUtils.loadAnimation(getContext(), R.anim.go_to_down);
        inFromDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {btnReportByCategoryDetail.setVisibility(View.VISIBLE);}
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        goToDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {btnReportByCategoryDetail.setVisibility(View.GONE);}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        llReportByCategory = (LinearLayout) rootView.findViewById(R.id.llReportByCategory);
        categoryReportView = new CategoryReportView(getContext(), PocketAccounterGeneral.INCOME);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        categoryReportView.setLayoutParams(lp);
        categoryReportView.getPieChart().setOnChartValueSelectedListener(this);
        llReportByCategory.addView(categoryReportView);
        return rootView;
    }
    @Override
    public void onValueSelected(final Entry e, int dataSetIndex, Highlight h) {
        if (btnReportByCategoryDetail.getVisibility() != View.VISIBLE)
            btnReportByCategoryDetail.startAnimation(inFromDown);
        btnReportByCategoryDetail.setText(getResources().getString(R.string.detail)+": "+categoryReportView.getDatas().get(e.getXIndex()).getCategory().getName());
        final CategoryDataRow row = categoryReportView.getDatas().get(e.getXIndex());
        categoryReportView.getPieChart().getLegend().setEnabled(false);
        btnReportByCategoryDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(getActivity());
                View dialogView = getActivity().getLayoutInflater().inflate(R.layout.report_by_category_info, null);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(dialogView);
                TextView tvReportByCategoryRootCatName = (TextView) dialogView.findViewById(R.id.tvReportByCategoryRootCatName);
                tvReportByCategoryRootCatName.setText(row.getCategory().getName());
                ImageView ivReportByCategoryRootCat = (ImageView) dialogView.findViewById(R.id.ivReportByCategoryRootCat);
                ivReportByCategoryRootCat.setImageResource(row.getCategory().getIcon());
                ListView lvReportByCategoryInfo = (ListView) dialogView.findViewById(R.id.lvReportByCategoryInfo);
                ImageView ivReportByCategoryClose = (ImageView) dialogView.findViewById(R.id.ivReportByCategoryClose);
                ivReportByCategoryClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView tvReportByCategoryPeriod = (TextView) dialogView.findViewById(R.id.tvReportByCategoryPeriod);
                Calendar begin = (Calendar)categoryReportView.getBeginTime().clone();
                Calendar end = (Calendar)categoryReportView.getEndTime().clone();
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                String text = format.format(begin.getTime())+" - "+format.format(end.getTime());
                tvReportByCategoryPeriod.setText(text);
                if (row.getSubCats().size() == 1 && row.getSubCats().get(0).getSubCategory().getId().matches(getResources().getString(R.string.no_category)))
                    lvReportByCategoryInfo.setVisibility(View.GONE);
                else {
                    ReportByCategoryDialogAdapter adapter = new ReportByCategoryDialogAdapter(getContext(), row.getSubCats());
                    lvReportByCategoryInfo.setAdapter(adapter);
                }
                TextView tvReportByCategoryInfoTotal = (TextView) dialogView.findViewById(R.id.tvReportByCategoryInfoTotal);
                DecimalFormat decimalFormat = new DecimalFormat("0.00##");
                tvReportByCategoryInfoTotal.setText(decimalFormat.format(row.getTotalAmount())+PocketAccounter.financeManager.getMainCurrency().getAbbr());
                TextView tvReportByCategoryInfoAverage = (TextView) dialogView.findViewById(R.id.tvReportByCategoryInfoAverage);
                long countOfDays = (end.getTimeInMillis()-begin.getTimeInMillis())/(1000*60*60*24);
                double average = row.getTotalAmount()/countOfDays;
                tvReportByCategoryInfoAverage.setText(decimalFormat.format(average));
                dialog.show();
            }
        });
    }
    @Override
    public void onNothingSelected() {btnReportByCategoryDetail.startAnimation(goToDown);}
}
