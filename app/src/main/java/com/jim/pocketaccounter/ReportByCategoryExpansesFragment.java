package com.jim.pocketaccounter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import com.jim.pocketaccounter.report.CategoryReportView;
public class ReportByCategoryExpansesFragment extends Fragment implements OnChartValueSelectedListener {
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
        categoryReportView = new CategoryReportView(getContext(), PocketAccounterGeneral.EXPANCE);
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
        categoryReportView.getPieChart().getLegend().setEnabled(false);
    }
    @Override
    public void onNothingSelected() {
        btnReportByCategoryDetail.startAnimation(goToDown);
        categoryReportView.getPieChart().getLegend().setEnabled(true);
    }
}
