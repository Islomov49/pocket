package com.jim.pocketaccounter.debt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by user on 6/4/2016.
 */

public class BorrowFragment extends Fragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter myAdapter;
    private FinanceManager financeManager;
    private int TYPE = 0;

    public static BorrowFragment getInstance(int type) {
        BorrowFragment fragment = new BorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE = getArguments().getInt("type", 0);
        financeManager = PocketAccounter.financeManager;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.borrow_fragment_layout, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lvBorrowFragment);
        myAdapter = new MyAdapter(getList());
        mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myAdapter);

        registerForContextMenu(recyclerView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Delete");
    }

    public ArrayList<DebtBorrow> getList() {
        ArrayList<DebtBorrow> list = new ArrayList<>();
        switch (TYPE) {
            case 0:
            case 1: {
                for (DebtBorrow debtBorrow : financeManager.getDebtBorrows()) {
                    if (debtBorrow.getType() == TYPE && !debtBorrow.isTo_archive()) {
                        list.add(debtBorrow);
                    }
                }
                break;
            }
            case 2: {
                for (DebtBorrow debtBorrow : financeManager.getDebtBorrows()) {
                    if (debtBorrow.isTo_archive()) {
                        list.add(debtBorrow);
                    }
                }
                break;
            }
        }
        return list;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<DebtBorrow> persons;

        public MyAdapter(List<DebtBorrow> contactList) {
            persons = contactList;
        }

        public int getItemCount() {
            return persons.size();
        }

        public void onBindViewHolder(BorrowFragment.ViewHolder view, final int position) {
            final int t = TYPE == 2?persons.size() - 1:0;
            final DebtBorrow person = persons.get(Math.abs(t - position));

            view.BorrowPersonName.setText(person.getPerson().getName());
            view.BorrowPersonNumber.setText(person.getPerson().getPhoneNumber());
            view.BorrowPersonDateGet.setText(
                    " " + person.getTakenDate().get(Calendar.DAY_OF_MONTH)
                            + " " + person.getTakenDate().get(Calendar.MONTH)
                            + " " + person.getTakenDate().get(Calendar.YEAR));
            view.BorrowPersonDateRepeat.setText(
                    " " + person.getReturnDate().get(Calendar.DAY_OF_MONTH)
                            + " " + person.getReturnDate().get(Calendar.MONTH)
                            + " " + person.getReturnDate().get(Calendar.YEAR));
            view.BorrowPersonSumm.setText("" + person.getAmount());
            if (person.getPerson().getPhoto().equals("")) {
//                view.BorrowPersonPhotoPath.setImageResource(R.drawable.);
            } else {
                view.BorrowPersonPhotoPath.setImageDrawable(Drawable.createFromPath(person.getPerson().getPhoto()));
            }
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ((PocketAccounter) getContext()).replaceFragment(InfoDebtBorrowFragment.getInstance(persons.get(Math.abs(t-position)).getId()));
                }
            });
            view.pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        final Dialog dialog = new Dialog(getActivity());
                        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.add_pay_debt_borrow_info, null);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(dialogView);
                        final EditText enterDate = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowDate);
                        final EditText enterPay = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPaySumm);
                        final EditText comment = (EditText) dialogView.findViewById(R.id.etInfoDebtBorrowPayComment);
                        final Spinner accountSp = (Spinner) dialogView.findViewById(R.id.spInfoDebtBorrowAccount);

                        String[] accaounts = new String[financeManager.getAccounts().size()];
                        for (int i = 0; i < accaounts.length; i++) {
                            accaounts[i] = financeManager.getAccounts().get(i).getName();
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
                                Recking recking = new Recking(format.format(date.getTime()),
                                        Double.parseDouble(enterPay.getText().toString()),
                                        persons.get(position).getId(), ""+accountSp.getSelectedItem(),
                                        comment.getText().toString());

                                persons.get(position).getReckings().add(recking);
                                dialog.dismiss();
                            }
                        });
                        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                        int width = displayMetrics.widthPixels;
                        dialog.getWindow().setLayout(7 * width / 8, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        dialog.show();
                }
            });
        }

        public BorrowFragment.ViewHolder onCreateViewHolder(ViewGroup parent, int var2) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_borrow_fragment, parent, false);
            return new ViewHolder(view);
        }

        public void setDataChanged(DebtBorrow person) {
            persons.add(0, person);
            notifyItemInserted(0);
        }

        public void setRemoveItem(int id) {
            persons.remove(id);
            notifyItemRemoved(id);
        }
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public TextView BorrowPersonName;
        public TextView BorrowPersonNumber;
        public TextView BorrowPersonSumm;
        public TextView BorrowPersonDateGet;
        public TextView BorrowPersonDateRepeat;
        public CircleImageView BorrowPersonPhotoPath;
        public Button pay;

        public ViewHolder(View view) {
            super(view);
            BorrowPersonName = (TextView) view.findViewById(R.id.tvBorrowPersonName);
            BorrowPersonNumber = (TextView) view.findViewById(R.id.tvBorrowPersonNumber);
            BorrowPersonSumm = (TextView) view.findViewById(R.id.tvBorrowPersonSumm);
            BorrowPersonDateGet = (TextView) view.findViewById(R.id.tvBorrowPersonDateGet);
            BorrowPersonDateRepeat = (TextView) view.findViewById(R.id.tvBorrowPersonDateRepeat);
            BorrowPersonPhotoPath = (CircleImageView) view.findViewById(R.id.imBorrowPerson);
            pay = (Button) view.findViewById(R.id.btBorrowPersonPay);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        financeManager.saveDebtBorrows();
    }
}