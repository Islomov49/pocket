package com.jim.pocketaccounter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

@SuppressLint("ValidFragment")
public class RecordDetailFragment extends Fragment {
    private Calendar date;
    private RecyclerView rvRecordDetail;
    @SuppressLint("ValidFragment")
    public RecordDetailFragment(Calendar date) {
        this.date = (Calendar) date.clone();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.record_detail_layout, container, false);
        rvRecordDetail = (RecyclerView) rootView.findViewById(R.id.rvRecordDetail);
        refreshList();
        return rootView;
    }
    private void refreshList() {
        ArrayList<FinanceRecord> records = new ArrayList<FinanceRecord>();
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
        for (int i=0; i<size; i++) {
            if (PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(begin)>=0 &&
                    PocketAccounter.financeManager.getRecords().get(i).getDate().compareTo(end) <= 0)
                records.add(PocketAccounter.financeManager.getRecords().get(i));
        }
        RecordDetailAdapter adapter = new RecordDetailAdapter(getContext(), records);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecordDetail.setLayoutManager(llm);
        rvRecordDetail.setAdapter( adapter );
        rvRecordDetail.setAdapter(adapter);
    }
}