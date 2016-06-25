package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceRecord;
import com.jim.pocketaccounter.finance.RecordAccountAdapter;
import com.jim.pocketaccounter.finance.RecordCategoryAdapter;
import com.jim.pocketaccounter.finance.RecordDetailAdapter;
import com.jim.pocketaccounter.finance.RecordSubCategoryAdapter;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.finance.SubCategory;
import com.jim.pocketaccounter.helper.PockerTag;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class RecordDetailFragment extends Fragment implements OnClickListener {
    private Calendar date;
    private RecyclerView rvRecordDetail;
    private ImageView ivToolbarMostRight, ivToolbarExcel;
    private int mode = PocketAccounterGeneral.NORMAL_MODE;
    private ArrayList<FinanceRecord> records;

    @SuppressLint("ValidFragment")
    public RecordDetailFragment(Calendar date) {
        this.date = (Calendar) date.clone();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.record_detail_layout, container, false);
        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setVisibility(View.VISIBLE);
        ivToolbarExcel = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarExcel);
        ivToolbarExcel.setVisibility(View.GONE);
        ivToolbarMostRight.setOnClickListener(this);
        ((PocketAccounter) getContext()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((PocketAccounter) getContext()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button);
        PocketAccounter.toolbar.setTitle(getResources().getString(R.string.records));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd,LLL yyyy");
        PocketAccounter.toolbar.setSubtitle(dateFormat.format(date.getTime()));
        ((Spinner) PocketAccounter.toolbar.findViewById(R.id.spToolbar)).setVisibility(View.GONE);
        PocketAccounter.toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PocketAccounter) getContext()).initialize(date);
                ((PocketAccounter)getContext()).getSupportFragmentManager().popBackStack();
            }
        });
        rvRecordDetail = (RecyclerView) rootView.findViewById(R.id.rvRecordDetail);
        refreshList();
        setMode(mode);
        return rootView;
    }

    private void refreshList() {
        records = new ArrayList<FinanceRecord>();
        int size = PocketAccounter.financeManager.getRecords().size();
        Calendar begin = (Calendar) date.clone();
        begin.set(Calendar.HOUR_OF_DAY, 0);
        begin.set(Calendar.MINUTE, 0);
        begin.set(Calendar.SECOND, 0);
        begin.set(Calendar.MILLISECOND, 0);
        Calendar end = (Calendar) date.clone();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 59);
        for (int i = 0; i < size; i++) {
            if (PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(begin) >= 0 &&
                    PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(end) <= 0)
                records.add(PocketAccounter.financeManager.getRecords().get(i));
        }
        RecordDetailAdapter adapter = new RecordDetailAdapter(getContext(), records);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecordDetail.setLayoutManager(llm);
        rvRecordDetail.setAdapter(adapter);
        rvRecordDetail.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolbarMostRight:
                mode = (mode == PocketAccounterGeneral.NORMAL_MODE ? PocketAccounterGeneral.EDIT_MODE : PocketAccounterGeneral.NORMAL_MODE);
                setMode(mode);
                break;
        }
    }

    private void setMode(int mode) {
        RecordDetailAdapter adapter = (RecordDetailAdapter) rvRecordDetail.getAdapter();
        adapter.setMode(mode);
        if (mode == PocketAccounterGeneral.NORMAL_MODE) {
            ivToolbarMostRight.setImageResource(R.drawable.pencil);
            for (int i = adapter.getItemCount()-1; i >= 0; i--) {
                View view = rvRecordDetail.getChildAt(i);
                if (view != null) {
                    if (((CheckBox) view.findViewById(R.id.chbRecordFragmentDetail)).isChecked()) {
                        adapter.removingItem(records.get(i));
                    }
                }
            }
            adapter.notifyDataSetChanged();
            for (int i = adapter.getItemCount(); i >= 0; i--) {
                View view = rvRecordDetail.getChildAt(i);
                if (view != null) {
                    view.findViewById(R.id.chbRecordFragmentDetail).setVisibility(View.GONE);
                }
            }
        } else {
            ivToolbarMostRight.setImageResource(R.drawable.ic_trash);
            for (int i = 0; i < adapter.getItemCount(); i++) {
                View view = rvRecordDetail.getChildAt(i);
                if (view != null) {
                    view.findViewById(R.id.chbRecordFragmentDetail).setVisibility(View.VISIBLE);
                    ((CheckBox)view.findViewById(R.id.chbRecordFragmentDetail)).setChecked(false);
                }
            }
        }
    }
}