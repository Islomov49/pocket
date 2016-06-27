package com.jim.pocketaccounter.finance;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.RecordEditFragment;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

import java.text.DecimalFormat;
import java.util.List;

public class RecordDetailAdapter extends RecyclerView.Adapter<RecordDetailAdapter.DetailViewHolder>{
    List<FinanceRecord> result;
    Context context;
    int mode = PocketAccounterGeneral.NORMAL_MODE;
    boolean[] selections;
    public RecordDetailAdapter(Context context, List<FinanceRecord> result, Integer mode, boolean[] selections){
        this.context = context;
        this.result = result;
        this.mode = mode;
        this.selections = selections;
    }

    @Override
    public void onBindViewHolder(final DetailViewHolder holder, final int position) {
        holder.ivRecordDetail.setImageResource(result.get(position).getCategory().getIcon());
        holder.tvRecordDetailCategoryName.setText(result.get(position).getCategory().getName());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String sign = "";
        if (result.get(position).getCategory().getType() == PocketAccounterGeneral.EXPANCE) {
            holder.tvRecordDetailCategoryAmount.setTextColor(ContextCompat.getColor(context, R.color.red));
            sign = "-";
        }
        else {
            holder.tvRecordDetailCategoryAmount.setTextColor(ContextCompat.getColor(context, R.color.green_just));
            sign = "+";
        }
        holder.tvRecordDetailCategoryAmount.setText(sign + decimalFormat.format(result.get(position).getAmount())+result.get(position).getCurrency().getAbbr());
        boolean subCatIsNull = (result.get(position).getSubCategory() == null);
        if (subCatIsNull) {
            holder.llSubCategories.setVisibility(View.GONE);
            holder.ivRecordDetailBorder.setVisibility(View.GONE);
        }
        else {
            holder.llSubCategories.setVisibility(View.VISIBLE);
            holder.ivRecordDetailBorder.setVisibility(View.VISIBLE);
            holder.ivRecordDetailSubCategory.setImageResource(result.get(position).getSubCategory().getIcon());
            holder.tvRecordDetailSubCategory.setText(result.get(position).getSubCategory().getName());
        }
        if (mode == PocketAccounterGeneral.NORMAL_MODE) {
            holder.chbRecordDetail.setVisibility(View.GONE);
        }
        else {
            holder.chbRecordDetail.setVisibility(View.VISIBLE);
        }
        final FinanceRecord financeRecord= result.get(position);
        holder.ivRecordDetailBorder.setVisibility(View.GONE);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode ==PocketAccounterGeneral.NORMAL_MODE)
                    ((PocketAccounter)context).replaceFragment(new RecordEditFragment(null, financeRecord.getDate(), financeRecord, PocketAccounterGeneral.DETAIL));
                else {
                    holder.chbRecordDetail.setChecked(!holder.chbRecordDetail.isChecked());
                    selections[position] = holder.chbRecordDetail.isChecked();
                }
            }
        });
        holder.chbRecordDetail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                selections[position] = isChecked;
            }
        });
        Log.d("sss", "position "+position+" selected "+selections[position]);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }
    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
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
        public TextView tvRecordDetailSubCategory;
        public ImageView ivRecordDetailSubCategory;
        public CheckBox chbRecordDetail;
        public View root;
        public DetailViewHolder(View view) {
            super(view);
            ivRecordDetail = (ImageView) view.findViewById(R.id.ivRecordDetail);
            tvRecordDetailCategoryName = (TextView) view.findViewById(R.id.tvRecordDetailCategoryName);
            tvRecordDetailCategoryAmount = (TextView) view.findViewById(R.id.tvRecordDetailCategoryAmount);
            llSubCategories = (LinearLayout) view.findViewById(R.id.llSubCategories);
            ivRecordDetailBorder = (ImageView) view.findViewById(R.id.ivRecordDetailBorder);
            tvRecordDetailSubCategory = (TextView) view.findViewById(R.id.tvRecordDetailSubCategory);
            ivRecordDetailSubCategory = (ImageView) view.findViewById(R.id.ivRecordDetailSubCategory);
            chbRecordDetail = (CheckBox) view.findViewById(R.id.chbRecordFragmentDetail);
            root = view;
        }
    }
    public void setMode(int mode) {
        this.mode = mode;
    }
}
