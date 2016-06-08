package com.jim.pocketaccounter.debt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.RC2ParameterSpec;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 6/7/2016.
 */

public class InfoDebtBorrowFragment extends Fragment implements View.OnClickListener {
    private TextView borrowName;
    private TextView leftAmount;
    private TextView borrowLeftDate;
    private TextView totalPayAmount;
    private FrameLayout borrowPay;
    private CircleImageView circleImageView;
    private android.support.v7.widget.RecyclerView recyclerView;
    private FinanceManager manager;
    private String id = "";
    private PeysAdapter peysAdapter;

    public static Fragment getInstance (String id){
        InfoDebtBorrowFragment fragment = new InfoDebtBorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_debt_borrow_fragment, container, false);
        borrowName = (TextView) view.findViewById(R.id.name_of_borrow);
        leftAmount = (TextView) view.findViewById(R.id.tvAmountDebtBorrowInfo);
        borrowLeftDate = (TextView) view.findViewById(R.id.tvLeftDayDebtBorrowInfo);
        borrowPay = (FrameLayout) view.findViewById(R.id.btPayDebtBorrowInfo);
        totalPayAmount = (TextView) view.findViewById(R.id.total_summ_debt_borrow);
        circleImageView = (CircleImageView) view.findViewById(R.id.iconDebtBorrowInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvDebtBorrowInfo);
        id = getArguments().getString("id");

        manager = new FinanceManager(getContext());
        DebtBorrow debtBorrow = new DebtBorrow();
        if (manager.getDebtBorrows() != null) {
            for (DebtBorrow db : manager.getDebtBorrows()) {
                if (db.getId().matches(id)) {
                    debtBorrow = db;
                    break;
                }
            }
        }
        peysAdapter = new PeysAdapter(debtBorrow.getReckings());
        Calendar currentDate = Calendar.getInstance();
        int delta = (debtBorrow.getReturnDate().get(Calendar.DAY_OF_YEAR) - currentDate.get(Calendar.DAY_OF_YEAR));
        ArrayList<Recking> list = debtBorrow.getReckings();
        double total = 0;
        for (Recking rc : list) {
            total += rc.getAmount();
        }

        borrowName.setText(debtBorrow.getPerson().getName());
        leftAmount.setText("" + delta + " day");
        borrowLeftDate.setText("" + delta);
        totalPayAmount.setText("" + total);
        if (!debtBorrow.getPerson().getPhoto().equals("")) {
            circleImageView.setImageDrawable(Drawable.createFromPath(debtBorrow.getPerson().getPhoto()));
        }

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(peysAdapter);
        borrowPay.setOnClickListener(this);
        return view;
    }

    private void openDialog () {
        final Dialog dialog=new Dialog(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_pay_debt_borrow_info, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        final EditText enterDate = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowDate);
        final EditText enterPay = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPaySumm);
        ImageView cancel = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowCancel);
        ImageView save = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowSave);
        final Calendar date = Calendar.getInstance();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                enterDate.setText(arg3 + "-" + arg2 + "-" + arg1);
                date.set(arg1, arg2, arg3);
            }
        };
        enterDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        getDatesetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
//                peysAdapter.setDataChanged("20.12.2016", Double.parseDouble(enterPay.getText().toString()));
                peysAdapter.setDataChanged("" + date.get(Calendar.DAY_OF_MONTH) + "." +
                                            date.get(Calendar.MONTH) + "." +
                                            date.get(Calendar.YEAR), Double.parseDouble(enterPay.getText().toString()));
                dialog.dismiss();
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(7*width/8, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        openDialog();
    }

    private class PeysAdapter extends RecyclerView.Adapter<InfoDebtBorrowFragment.ViewHolder> {
        private ArrayList<Recking> list;

        public PeysAdapter(ArrayList<Recking> list) {
            this.list = list;
        }

        public int getItemCount() {
            return list.size();
        }

        public void onBindViewHolder(InfoDebtBorrowFragment.ViewHolder view, int position) {
            view.infoDate.setText(list.get(position).getPayDate());
            view.infoSumm.setText("" + list.get(position).getAmount());
        }

        public InfoDebtBorrowFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_info_debt_borrow_layout, parent, false);
            return new ViewHolder(view);
        }

        public void setDataChanged(String clDate, double value) {
            Toast.makeText(getContext(), "212", Toast.LENGTH_LONG).show();
            for (DebtBorrow debtBorrow : manager.getDebtBorrows()) {
                if (debtBorrow.getReckings().equals(list)) {
                    Recking recking = new Recking(clDate, value, debtBorrow.getId());
                    debtBorrow.addRecking(recking);
                    list.add(0, recking);
                    Toast.makeText(getContext(), "" + clDate + " " + value, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
//            manager.saveDebtBorrows();
//            manager.loadDebtBorrows();
            notifyItemInserted(0);
        }

        public void setRemoveItem(int id) {
//            persons.remove(id);
//            notifyItemRemoved(id);
        }

    }
    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView infoDate;
        public TextView infoSumm;

        public ViewHolder(View view) {
            super(view);
            infoDate = (TextView) view.findViewById(R.id.tvInfoDebtBorrrowDate);
            infoSumm = (TextView) view.findViewById(R.id.tvInfoDebtBorrrowSumm);
        }
    }
}