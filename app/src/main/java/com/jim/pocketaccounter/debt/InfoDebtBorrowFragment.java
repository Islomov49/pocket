package com.jim.pocketaccounter.debt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 6/7/2016.
 */

public class InfoDebtBorrowFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView borrowName;
    private TextView leftAmount;
    private TextView borrowLeftDate;
    private TextView totalPayAmount;
    private TextView calculate;
    private TextView tvTotalsummInfo;
    private FrameLayout borrowPay;
    private CircleImageView circleImageView;
    private android.support.v7.widget.RecyclerView recyclerView;
    private FinanceManager manager;
    private String id = "";
    private PeysAdapter peysAdapter;
    private Spinner accountSp;
    private DebtBorrow debtBorrow;
    private FrameLayout deleteFrame;
    private ImageView info;
    private FrameLayout infoFrame;
    private FrameLayout isHaveReking;
    private TextView tvInfoDebtBorrowTakeDate;

    public static Fragment getInstance(String id) {
        InfoDebtBorrowFragment fragment = new InfoDebtBorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.info_debt_borrow_fragment_mod, container, false);
        borrowName = (TextView) view.findViewById(R.id.name_of_borrow);
        leftAmount = (TextView) view.findViewById(R.id.tvAmountDebtBorrowInfo);
        borrowLeftDate = (TextView) view.findViewById(R.id.tvLeftDayDebtBorrowInfo);
        borrowPay = (FrameLayout) view.findViewById(R.id.btPayDebtBorrowInfo);
        deleteFrame = (FrameLayout) view.findViewById(R.id.flInfoDebtBorrowDeleted);
        totalPayAmount = (TextView) view.findViewById(R.id.total_summ_debt_borrow);
        tvTotalsummInfo = (TextView) view.findViewById(R.id.tvInfoDebtBorrowTotalSumm);
        circleImageView = (CircleImageView) view.findViewById(R.id.iconDebtBorrowInfo);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvDebtBorrowInfo);
        info = (ImageView) view.findViewById(R.id.ivInfoDebtBorrowInfo);
        infoFrame = (FrameLayout) view.findViewById(R.id.flInfoDebtBorrowVisibl);
        tvInfoDebtBorrowTakeDate = (TextView) view.findViewById(R.id.tvInfoDebtBorrowTakeDate);
        infoFrame.setVisibility(View.GONE);
        calculate = (TextView) view.findViewById(R.id.tvInfoDebtBorrowIsCalculate);
        id = getArguments().getString("id");
        isHaveReking = (FrameLayout) view.findViewById(R.id.ifListHave);
        ((ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight)).setVisibility(View.VISIBLE);
        ((ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight)).setImageResource(R.drawable.trash);
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

        PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.getDebtBorrows().remove(debtBorrow);
                manager.saveDebtBorrows();
                manager.loadDebtBorrows();
                ((PocketAccounter) getContext()).getSupportFragmentManager().popBackStack();
                DebtBorrowFragment fragment = new DebtBorrowFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("pos", debtBorrow.getType());
                fragment.setArguments(bundle);
                ((PocketAccounter) getContext()).replaceFragment(fragment, PockerTag.DEBTS);
            }
        });
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        tvInfoDebtBorrowTakeDate.setText(format.format(debtBorrow.getTakenDate().getTime()));
        calculate.setText(debtBorrow.isCalculate() ? "calculate" : "no calculate");
        infoFrame.setVisibility(View.GONE);
        view.findViewById(R.id.with_wlyuzik).setVisibility(view.GONE);
        view.findViewById(R.id.infoooc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (infoFrame.getVisibility() == View.GONE) {
                    infoFrame.setVisibility(View.VISIBLE);
                    info.setImageResource(R.drawable.pasga_ochil);
                    view.findViewById(R.id.with_wlyuzik).setVisibility(View.VISIBLE);
                } else {
                    infoFrame.setVisibility(View.GONE);
                    info.setImageResource(R.drawable.infoo);
                    view.findViewById(R.id.with_wlyuzik).setVisibility(view.GONE);
                }
            }
        });

        if (debtBorrow.getReckings().isEmpty()) {
            isHaveReking.setVisibility(View.GONE);
        }

        peysAdapter = new PeysAdapter(debtBorrow.getReckings());
        Calendar currentDate = Calendar.getInstance();

        int day = 0;
        int mounth = 0;
        int year = 0;

        if (debtBorrow.getReturnDate() != null && currentDate.compareTo(debtBorrow.getReturnDate()) <= 0) {
            if (currentDate.get(Calendar.DAY_OF_MONTH) <= debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH)) {
                day = debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH) - currentDate.get(Calendar.DAY_OF_MONTH);
                if (currentDate.get(Calendar.MONTH) <= debtBorrow.getReturnDate().get(Calendar.MONTH)) {
                    mounth = debtBorrow.getReturnDate().get(Calendar.MONTH) - currentDate.get(Calendar.MONTH);
                    if (currentDate.get(Calendar.YEAR) <= debtBorrow.getReturnDate().get(Calendar.YEAR)) {
                        year = debtBorrow.getReturnDate().get(Calendar.YEAR) - currentDate.get(Calendar.YEAR);
                    }
                } else {
                    mounth = debtBorrow.getReturnDate().get(Calendar.MONTH) + 12 - currentDate.get(Calendar.MONTH);
                    year = debtBorrow.getReturnDate().get(Calendar.YEAR) - 1 - currentDate.get(Calendar.YEAR);
                }
            } else {
                debtBorrow.getReturnDate().add(Calendar.MONTH, -1);
                day = debtBorrow.getReturnDate().get(Calendar.DAY_OF_MONTH) + debtBorrow.getReturnDate().getActualMaximum(Calendar.MONTH) - currentDate.get(Calendar.DAY_OF_MONTH);
                if (debtBorrow.getReturnDate().get(Calendar.MONTH) >= currentDate.get(Calendar.MONTH)) {
                    mounth = debtBorrow.getReturnDate().get(Calendar.MONTH) - currentDate.get(Calendar.MONTH);
                    if (debtBorrow.getReturnDate().get(Calendar.YEAR) >= currentDate.get(Calendar.YEAR)) {
                        year = debtBorrow.getReturnDate().get(Calendar.YEAR) - currentDate.get(Calendar.YEAR);
                    }
                } else {
                    mounth = debtBorrow.getReturnDate().get(Calendar.MONTH) + 12 - currentDate.get(Calendar.MONTH);
                    if (debtBorrow.getReturnDate().get(Calendar.YEAR) > currentDate.get(Calendar.YEAR)) {
                        year = debtBorrow.getReturnDate().get(Calendar.YEAR) - 1 - currentDate.get(Calendar.YEAR);
                    }
                }
            }
        }

        ArrayList<Recking> list = debtBorrow.getReckings();
        double total = 0;
        for (Recking rc : list) {
            total += rc.getAmount();
        }

        if (debtBorrow.isTo_archive()) {
            deleteFrame.setVisibility(View.GONE);
            borrowPay.setVisibility(View.GONE);
        }

        borrowName.setText(debtBorrow.getPerson().getName());
        String qq = ((int) (debtBorrow.getAmount() - total)) == (debtBorrow.getAmount() - total)
                ? "" + ((int) (debtBorrow.getAmount() - total)) : "" + (debtBorrow.getAmount() - total);
        leftAmount.setText(qq + debtBorrow.getCurrency().getAbbr());
        String sana = (year != 0 ? year + " " + getString(R.string.year) : "")
                + (mounth != 0 ? mounth + " " + getString(R.string.moth) : "")
                + (day != 0 ? day + " " + getString(R.string.day) : "");
        if (debtBorrow.getReturnDate() == null) {
            borrowLeftDate.setText("no date");
        } else {
            borrowLeftDate.setText(sana);
        }
        totalPayAmount.setText(total == ((int) total) ? "" + ((int) total) : "" + total + debtBorrow.getCurrency().getAbbr());

        if (!debtBorrow.getPerson().getPhoto().equals("")) {
            try {
                circleImageView.setImageBitmap(queryContactImage(Integer.parseInt(debtBorrow.getPerson().getPhoto())));
            } catch (NumberFormatException e) {
                circleImageView.setImageDrawable(Drawable.createFromPath(debtBorrow.getPerson().getPhoto()));
            }
        } else {
            circleImageView.setImageResource(R.drawable.no_photo);
        }
        Log.d("sss", debtBorrow.getCurrency().getAbbr());
        tvTotalsummInfo.setText("" + (debtBorrow.getAmount() == ((int) debtBorrow.getAmount())
                ? ((int) debtBorrow.getAmount()) : debtBorrow.getAmount()) + debtBorrow.getCurrency().getAbbr());

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(peysAdapter);
        borrowPay.setOnClickListener(this);

        deleteFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < peysAdapter.getItemCount(); i++) {
                    View item = recyclerView.getChildAt(i);
                    if (item.findViewById(R.id.for_delete_check_box).getVisibility() == View.GONE) {
                        item.findViewById(R.id.for_delete_check_box).setVisibility(View.VISIBLE);
                    } else {
                        if (((CheckBox) item.findViewById(R.id.for_delete_check_box)).isChecked()) {
                            peysAdapter.itemDeleted(i);
                        }
                        item.findViewById(R.id.for_delete_check_box).setVisibility(View.GONE);
                    }
                }
            }
        });
        return view;
    }

    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[]{
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

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

        if (!debtBorrow.isCalculate()) {
            dialogView.findViewById(R.id.is_calc).setVisibility(View.GONE);
        }

        ImageView cancel = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowCancel);
        ImageView save = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowSave);
        final Calendar date = Calendar.getInstance();
        enterDate.setText(simpleDateFormat.format(date.getTime()));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
                date.set(arg1, arg2, arg3);
                enterDate.setText(simpleDateFormat.format(date.getTime()));
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

        public void onBindViewHolder(final InfoDebtBorrowFragment.ViewHolder view, final int position) {
            view.infoDate.setText("Date of Pay: " + list.get(position).getPayDate());
            view.infoSumm.setText((list.get(position).getAmount() == ((int) list.get(position).getAmount()))
                    ? "" + ((int) list.get(position).getAmount()) : "" + list.get(position).getAmount()
                    + "" + debtBorrow.getCurrency().getAbbr());
            view.infoAccount.setText("Via: " + list.get(position).getAccountId());
            if (!list.get(position).getComment().matches("")) {
                view.comment.setText("Comment: " + list.get(position).getComment());
            } else {
                view.comment.setVisibility(View.GONE);
            }
            view.checkBox.setVisibility(View.GONE);
            view.checkBox.setChecked(false);
            view.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (view.checkBox.getVisibility() == View.VISIBLE)
                    view.checkBox.setChecked(!view.checkBox.isChecked());
                }
            });
        }

        public InfoDebtBorrowFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.payed_item, parent, false);
            return new ViewHolder(view);
        }

        public void itemDeleted(int position) {
            list.remove(position);
            notifyItemRemoved(position);
            debtBorrow.setReckings(list);
            double total = 0;
            for (Recking rc : list) {
                total += rc.getAmount();
            }
            totalPayAmount.setText(total == ((int) total) ? "" + ((int) total) : "" + total + debtBorrow.getCurrency().getAbbr());
            String qq = ((int) (debtBorrow.getAmount() - total)) == (debtBorrow.getAmount() - total)
                    ? "" + ((int) (debtBorrow.getAmount() - total)) : "" + (debtBorrow.getAmount() - total);
            leftAmount.setText(qq + "" + debtBorrow.getCurrency().getAbbr());
            if (debtBorrow.getReckings().isEmpty()) {
                isHaveReking.setVisibility(View.GONE);
            }
        }

        public void setDataChanged(String clDate, double value, String accountId, String comment) {
            Recking recking = new Recking(clDate, value, debtBorrow.getId(), accountId, comment);
            list.add(0, recking);
            double qoldiq = 0;
            for (int i = 0; i < list.size(); i++) {
                qoldiq += list.get(i).getAmount();
            }
            String qq = ((int) (debtBorrow.getAmount() - qoldiq)) == (debtBorrow.getAmount() - qoldiq)
                    ? "" + ((int) (debtBorrow.getAmount() - qoldiq)) : "" + (debtBorrow.getAmount() - qoldiq);
            leftAmount.setText(qq + "" + debtBorrow.getCurrency().getAbbr());
            totalPayAmount.setText("" + (qoldiq == ((int) qoldiq) ? ((int) qoldiq) : qoldiq) + "" + debtBorrow.getCurrency().getAbbr());
            debtBorrow.setReckings(list);
            manager.setDebtBorrows(manager.getDebtBorrows());
            isHaveReking.setVisibility(View.VISIBLE);
            notifyItemInserted(0);
        }
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView infoDate;
        public TextView infoSumm;
        public TextView infoAccount;
        public TextView comment;
        public CheckBox checkBox;
        public View rootView;

        public ViewHolder(View view) {
            super(view);
            infoDate = (TextView) view.findViewById(R.id.date_of_trans);
            infoAccount = (TextView) view.findViewById(R.id.via_acount);
            comment = (TextView) view.findViewById(R.id.comment_trans);
            infoSumm = (TextView) view.findViewById(R.id.paid_value);
            checkBox = (CheckBox) view.findViewById(R.id.for_delete_check_box);
            rootView = view.findViewById(R.id.rlRootView);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        manager.saveDebtBorrows();
        manager.loadDebtBorrows();
    }
}