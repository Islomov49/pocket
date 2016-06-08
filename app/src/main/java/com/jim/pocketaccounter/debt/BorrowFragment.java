package com.jim.pocketaccounter.debt;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.FinanceManager;

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
        financeManager = new FinanceManager(getContext());
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

        return view;
    }

    public ArrayList<DebtBorrow> getList() {
        ArrayList<DebtBorrow> list = new ArrayList<>();
        switch (TYPE) {
            case 0:
            case 1: {
                for (DebtBorrow debtBorrow : financeManager.getDebtBorrows()) {
                    if (debtBorrow.getType() == TYPE) {
                        list.add(debtBorrow);
                    }
                }
                break;
            }
            case 2: {
                return financeManager.getDebtBorrows();
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
            final DebtBorrow person = persons.get(position);
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
                view.BorrowPersonPhotoPath.setImageResource(R.drawable.credit_icon);
            } else {
                Glide
                        .with(getContext())
                        .load(person.getPerson().getPhoto())
                        .centerCrop()
                        .crossFade()
                        .into(view.BorrowPersonPhotoPath);
            }
            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((PocketAccounter) getContext()).replaceFragment(InfoDebtBorrowFragment.getInstance(persons.get(position).getId()));
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

        public ViewHolder(View view) {
            super(view);
            BorrowPersonName = (TextView) view.findViewById(R.id.tvBorrowPersonName);
            BorrowPersonNumber = (TextView) view.findViewById(R.id.tvBorrowPersonNumber);
            BorrowPersonSumm = (TextView) view.findViewById(R.id.tvBorrowPersonSumm);
            BorrowPersonDateGet = (TextView) view.findViewById(R.id.tvBorrowPersonDateGet);
            BorrowPersonDateRepeat = (TextView) view.findViewById(R.id.tvBorrowPersonDateRepeat);
            BorrowPersonPhotoPath = (CircleImageView) view.findViewById(R.id.imBorrowPerson);
        }
    }
}