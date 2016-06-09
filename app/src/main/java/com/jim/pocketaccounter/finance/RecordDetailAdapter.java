package com.jim.pocketaccounter.finance;

import android.app.ActionBar;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.RecordEditFragment;
import com.jim.pocketaccounter.credit.CreditComputeDate;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordDetailAdapter extends RecyclerView.Adapter<RecordDetailAdapter.DetailViewHolder>{
    List<FinanceRecord> result;
    Context context;
    public RecordDetailAdapter(Context context, List<FinanceRecord> result){
        Log.d("sss", "constructor");
        this.result = result;
        this.context = context;

    }
    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        Log.d("sss", "on bind");
        holder.ivRecordDetail.setImageResource(result.get(position).getCategory().getIcon());
        holder.tvRecordDetailCategoryName.setText(result.get(position).getCategory().getName());
        double totalAmount = 0.0;
        final ArrayList<FinanceRecord> records = new ArrayList<FinanceRecord>();
        for(int i=0; i<PocketAccounter.financeManager.getRecords().size(); i++) {
            if (PocketAccounter.financeManager.getRecords().get(i).getCategory().getId().matches(result.get(position).getCategory().getId()))
                records.add(PocketAccounter.financeManager.getRecords().get(i));
        }
        for (int i=0; i<records.size(); i++)
            totalAmount = totalAmount + PocketAccounterGeneral.getCost(records.get(i));
        holder.tvRecordDetailCategoryAmount.setText(Double.toString(totalAmount)+PocketAccounter.financeManager.getMainCurrency());
        boolean subCategoryFound = false;
        for (int i=0; i<records.size(); i++) {
            if (records.get(i).getSubCategory() != null) {
                subCategoryFound = true;
                break;
            }
        }
        for (int i=0; i<records.size(); i++) {
            FinanceRecord record = records.get(i);
            int pos;
            if (i != records.size()-1)
                pos = i+1;
            else
                pos = i;
            if (record.getSubCategory() == null) {
                for (int j = pos; j<records.size(); j++) {
                    if (records.get(j).getSubCategory() == null) {
                        record.setAmount(record.getAmount() + records.get(j).getAmount());
                        records.remove(j);
                        j--;
                    }
                }
            }
            else {
                for (int j = pos; j<records.size(); j++) {
                    if (records.get(j).getSubCategory().getId().matches(record.getSubCategory().getId())) {
                        record.setAmount(record.getAmount() + records.get(j).getAmount());
                        records.remove(j);
                        j--;
                    }
                }
            }
        }
        if (subCategoryFound) {
            ArrayList<LinearLayout> layouts = new ArrayList<LinearLayout>();
            holder.ivRecordDetailBorder.setVisibility(View.VISIBLE);
            for (int i=0; i<records.size(); i++) {
                final int posit = i;
                LinearLayout newLinLayout = new LinearLayout(context);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                newLinLayout.setLayoutParams(lp);
                ImageView ivSubCategory = new ImageView(context);
                LinearLayout.LayoutParams ivLp = new LinearLayout.LayoutParams((int)context.getResources().getDimension(R.dimen.twentyfive_dp), (int)context.getResources().getDimension(R.dimen.twentyfive_dp));
                ivLp.setMargins((int)context.getResources().getDimension(R.dimen.thirtyfive_dp), (int)context.getResources().getDimension(R.dimen.ten_dp), (int)context.getResources().getDimension(R.dimen.ten_dp), (int)context.getResources().getDimension(R.dimen.ten_dp));
                if (records.get(i).getSubCategory() == null)
                    ivSubCategory.setImageResource(R.drawable.no_category);
                else
                    ivSubCategory.setImageResource(records.get(i).getSubCategory().getIcon());
                ivSubCategory.setLayoutParams(ivLp);
                newLinLayout.addView(ivSubCategory);
                TextView tvSubCategoryName = new TextView(context);
                LinearLayout.LayoutParams tvLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvSubCategoryName.setTextSize(context.getResources().getDimension(R.dimen.fourteen_sp));
                tvLp.gravity = Gravity.CENTER_VERTICAL;
                tvLp.weight = 1;
                tvSubCategoryName.setLayoutParams(tvLp);
                tvSubCategoryName.setTextColor(ContextCompat.getColor(context, R.color.green_light));
                if (records.get(i).getSubCategory() == null)
                    tvSubCategoryName.setText(context.getResources().getString(R.string.no_category_name));
                else
                    tvSubCategoryName.setText(records.get(i).getSubCategory().getName());
                newLinLayout.addView(tvSubCategoryName);
                TextView tvSubCategoryAmount = new TextView(context);
                LinearLayout.LayoutParams tvSubAmount = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tvSubCategoryAmount.setTextSize(context.getResources().getDimension(R.dimen.fourteen_sp));
                tvSubAmount.gravity = Gravity.CENTER_VERTICAL;
                tvSubAmount.setMargins((int)context.getResources().getDimension(R.dimen.ten_dp), 0, (int)context.getResources().getDimension(R.dimen.fifteen_dp), 0);
                tvSubCategoryAmount.setLayoutParams(tvSubAmount);
                tvSubCategoryAmount.setText(Double.toString(records.get(i).getAmount()));
                newLinLayout.addView(tvSubCategoryAmount);
                newLinLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((PocketAccounter)context).replaceFragment(new RecordEditFragment(null, null, records.get(posit)));
                    }
                });
            }
        }
        else
            holder.ivRecordDetailBorder.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("sss", "onCreate");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_detail_list_item, parent, false);
        DetailViewHolder viewHolder = new DetailViewHolder(v);
        return viewHolder;
    }
    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivRecordDetail;
        public TextView tvRecordDetailCategoryName;
        public TextView tvRecordDetailCategoryAmount;
        public LinearLayout llSubCategories;
        public ImageView ivRecordDetailBorder;
        public View root;
        public DetailViewHolder(View view) {
            super(view);
            Log.d("sss", "view holder created");
            ivRecordDetail = (ImageView) view.findViewById(R.id.ivRecordDetail);
            tvRecordDetailCategoryName = (TextView) view.findViewById(R.id.tvRecordDetailCategoryName);
            tvRecordDetailCategoryAmount = (TextView) view.findViewById(R.id.tvRecordDetailCategoryAmount);
            llSubCategories = (LinearLayout) view.findViewById(R.id.llSubCategories);
            ivRecordDetailBorder = (ImageView) view.findViewById(R.id.ivRecordDetailBorder);
            root= view;
        }
    }
}
