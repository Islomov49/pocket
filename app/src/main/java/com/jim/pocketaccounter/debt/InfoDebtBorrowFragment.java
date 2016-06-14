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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.PocketAccounter;
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

public class InfoDebtBorrowFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
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
    private Spinner accountSp;
    private DebtBorrow debtBorrow;

    public static Fragment getInstance(String id) {
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

        manager = PocketAccounter.financeManager;
        debtBorrow = new DebtBorrow();
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

        int day = 0;
        int mounth = 0;
        int year = 0;

        if(currentDate.compareTo(debtBorrow.getReturnDate()) >= 0) {
            if (currentDate.get(Calendar.DAY_OF_MONTH) >= debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH)) {
                day = currentDate.get(Calendar.DAY_OF_MONTH) - debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH);
                if (currentDate.get(Calendar.MONTH) >= debtBorrow.getReturnDate().get(Calendar.MONTH)) {
                    mounth = currentDate.get(Calendar.MONTH) - debtBorrow.getReturnDate().get(Calendar.MONTH);
                    if (currentDate.get(Calendar.YEAR) >= debtBorrow.getReturnDate().get(Calendar.YEAR)) {
                        year = currentDate.get(Calendar.YEAR) - debtBorrow.getReturnDate().get(Calendar.YEAR);
                    }
                } else {
                    mounth = currentDate.get(Calendar.MONTH) + 12 - debtBorrow.getReturnDate().get(Calendar.MONTH);
                    year = currentDate.get(Calendar.YEAR) - 1 - debtBorrow.getReturnDate().get(Calendar.YEAR);
                }
            } else {
                currentDate.add(Calendar.MONTH, -1);
                day = currentDate.get(Calendar.DAY_OF_MONTH) + currentDate.getActualMaximum(Calendar.MONTH) - debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH);
                if (currentDate.get(Calendar.MONTH) >= debtBorrow.getReturnDate().get(Calendar.MONTH)) {
                    mounth = currentDate.get(Calendar.MONTH) - debtBorrow.getReturnDate().get(Calendar.MONTH);
                    if (currentDate.get(Calendar.YEAR) >= debtBorrow.getReturnDate().get(Calendar.YEAR)) {
                        year = currentDate.get(Calendar.YEAR) - debtBorrow.getReturnDate().get(Calendar.YEAR);
                    }
                } else {
                    mounth = currentDate.get(Calendar.MONTH) + 12 - debtBorrow.getReturnDate().get(Calendar.MONTH);
                    if (currentDate.get(Calendar.YEAR) > debtBorrow.getReturnDate().get(Calendar.YEAR)) {
                        year = currentDate.get(Calendar.YEAR) - 1 - debtBorrow.getReturnDate().get(Calendar.YEAR);
                    }
                }
            }
        }


        ArrayList<Recking> list = debtBorrow.getReckings();
        double total = 0;
        for (Recking rc : list) {
            total += rc.getAmount();
        }

        borrowName.setText(debtBorrow.getPerson().getName());
        leftAmount.setText("" + (debtBorrow.getAmount() - total));
        borrowLeftDate.setText("year " + year + "\n" + "month " + mounth + "\n" + "day " + day);
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

    private void openDialog() {
        final Dialog dialog = new Dialog(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_pay_debt_borrow_info_mod, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        final EditText enterDate = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowDate);
        final EditText enterPay = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPaySumm);
        final EditText comment = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPayComment);
        accountSp = (Spinner) dialogView.findViewById(R.id.spInfoDebtBorrowAccount);

        String[] accaounts = new String[manager.getAccounts().size()];
        for (int i = 0; i < accaounts.length; i++) {
            accaounts[i] = manager.getAccounts().get(i).getName();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, accaounts);

        accountSp.setAdapter(arrayAdapter);

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
                arg2 = arg2 + 1;
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
                peysAdapter.setDataChanged(format.format(date.getTime()), Double.parseDouble(enterPay.getText().toString()),
                        "" + accountSp.getSelectedItem(), comment.getText().toString());
                dialog.dismiss();
            }
        });
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(7 * width / 8, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        openDialog();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
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
            view.infoDate.setText("Date of Pay: " + list.get(position).getPayDate());
            view.infoSumm.setText("" + list.get(position).getAmount() + "");
            view.infoAccount.setText("Via: " + list.get(position).getAccountId());
            if (!list.get(position).getComment().matches("")) {
                view.comment.setText("Comment: " + list.get(position).getComment());
            } else {
                view.comment.setVisibility(View.GONE);
            }
        }

        public InfoDebtBorrowFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payed_item, parent, false);
            return new ViewHolder(view);
        }

        public void setDataChanged(String clDate, double value, String accountId, String comment) {
            Recking recking = new Recking(clDate, value, debtBorrow.getId(), accountId, comment);
            list.add(0, recking);
            double qoldiq = 0;
            for (int i = 0; i < list.size(); i++) {
                qoldiq += list.get(i).getAmount();
            }
            leftAmount.setText("" + (debtBorrow.getAmount() - qoldiq));
            totalPayAmount.setText("" + qoldiq);
            debtBorrow.setReckings(list);
            manager.setDebtBorrows(manager.getDebtBorrows());
            notifyItemInserted(0);
        }
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView infoDate;
        public TextView infoSumm;
        public TextView infoAccount;
        public TextView comment;

        public ViewHolder(View view) {
            super(view);
            infoDate = (TextView) view.findViewById(R.id.date_of_trans);
            infoAccount = (TextView) view.findViewById(R.id.via_acount);
            comment = (TextView) view.findViewById(R.id.comment_trans);
            infoSumm = (TextView) view.findViewById(R.id.paid_value);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        manager.saveDebtBorrows();
        manager.loadDebtBorrows();
    }
}